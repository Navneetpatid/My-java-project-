package com.hsbc.hap.cer.service;

import com.hsbc.hap.cer.model.QueryResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HapCERServiceImplTest {

    @InjectMocks
    private HapCERServiceImpl service;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query mockedQuery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteQueries_withValidUpdateQuery_shouldInjectUpdatedDate() {
        String updateQuery = "UPDATE users SET name = 'John' WHERE id = 1";
        String expectedInjected = "UPDATE users SET updated_date = now(), name = 'John' WHERE id = 1";

        when(entityManager.createNativeQuery(anyString())).thenReturn(mockedQuery);
        when(mockedQuery.executeUpdate()).thenReturn(1);

        List<QueryResult> results = service.executeQueries(List.of(updateQuery));
        assertEquals(1, results.size());
        assertTrue(results.get(0).isSuccess());
        assertNull(results.get(0).getErrorMessage());

        verify(entityManager).createNativeQuery(argThat(actualQuery ->
                actualQuery.toLowerCase().contains("set updated_date = now()")
        ));
    }

    @Test
    void testExecuteQueries_withDisallowedSemicolon_shouldFail() {
        String invalidQuery = "UPDATE users SET name = 'John';";

        List<QueryResult> results = service.executeQueries(List.of(invalidQuery));

        assertEquals(1, results.size());
        assertFalse(results.get(0).isSuccess());
        assertEquals("Semicolons are not allowed in the query.", results.get(0).getErrorMessage());
    }

    @Test
    void testExecuteQueries_withUnsupportedSQL_shouldFail() {
        String selectQuery = "SELECT * FROM users";

        List<QueryResult> results = service.executeQueries(List.of(selectQuery));

        assertEquals(1, results.size());
        assertFalse(results.get(0).isSuccess());
        assertEquals("Only UPDATE, INSERT, DELETE queries are allowed.", results.get(0).getErrorMessage());
    }

    @Test
    void testExecuteQueries_withEmptyQuery_shouldReturnError() {
        List<QueryResult> results = service.executeQueries(List.of("  "));

        assertEquals(0, results.size());
    }

    @Test
    void testExecuteQueries_withPersistenceException_shouldCatchIt() {
        String validQuery = "UPDATE users SET name = 'Jane' WHERE id = 2";

        when(entityManager.createNativeQuery(anyString())).thenThrow(new PersistenceException());

        List<QueryResult> results = service.executeQueries(List.of(validQuery));

        assertEquals(1, results.size());
        assertFalse(results.get(0).isSuccess());
        assertEquals("Invalid SQL syntax or unknown column/table.", results.get(0).getErrorMessage());
    }
    }
