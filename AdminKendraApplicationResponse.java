@ExtendWith(MockitoExtension.class)
class HapCERServiceImplTest {

    @InjectMocks
    private HapCERServiceImpl hapCERService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query mockQuery;

    @Test
    void testEmptyQueryListReturnsError() {
        List<QueryResult> results = hapCERService.executeQueries(new ArrayList<>());

        assertEquals(1, results.size());
        assertFalse(results.get(0).isSuccess());
        assertEquals("Query list is empty or null", results.get(0).getErrorMessage());
    }

    @Test
    void testNullQueryListReturnsError() {
        List<QueryResult> results = hapCERService.executeQueries(null);

        assertEquals(1, results.size());
        assertFalse(results.get(0).isSuccess());
        assertEquals("Query list is empty or null", results.get(0).getErrorMessage());
    }

    @Test
    void testDisallowedQueryTypeReturnsError() {
        List<String> queries = List.of("SELECT * FROM users");
        List<QueryResult> results = hapCERService.executeQueries(queries);

        assertEquals(1, results.size());
        assertFalse(results.get(0).isSuccess());
        assertEquals("Only UPDATE, INSERT, DELETE queries are allowed.", results.get(0).getErrorMessage());
    }

    @Test
    void testQueryWithSemicolonReturnsError() {
        List<String> queries = List.of("UPDATE users SET name='John';");
        List<QueryResult> results = hapCERService.executeQueries(queries);

        assertEquals(1, results.size());
        assertFalse(results.get(0).isSuccess());
        assertEquals("Semicolons are not allowed in the query.", results.get(0).getErrorMessage());
    }

    @Test
    void testValidUpdateQuerySuccess() {
        String query = "UPDATE users SET name='John' WHERE id=1";

        Mockito.when(entityManager.createNativeQuery(query)).thenReturn(mockQuery);
        Mockito.when(mockQuery.executeUpdate()).thenReturn(1);

        List<QueryResult> results = hapCERService.executeQueries(List.of(query));

        assertEquals(1, results.size());
        assertTrue(results.get(0).isSuccess());
        assertNull(results.get(0).getErrorMessage());
    }

    @Test
    void testUpdateQueryNoRowsAffected() {
        String query = "UPDATE users SET name='Jane' WHERE id=999";

        Mockito.when(entityManager.createNativeQuery(query)).thenReturn(mockQuery);
        Mockito.when(mockQuery.executeUpdate()).thenReturn(0);

        List<QueryResult> results = hapCERService.executeQueries(List.of(query));

        assertEquals(1, results.size());
        assertFalse(results.get(0).isSuccess());
        assertEquals("Query executed but no rows affected.", results.get(0).getErrorMessage());
    }

    @Test
    void testQueryThrowsPersistenceException() {
        String query = "UPDATE invalid_table SET x='y'";

        Mockito.when(entityManager.createNativeQuery(query)).thenThrow(new PersistenceException("DB error"));

        List<QueryResult> results = hapCERService.executeQueries(List.of(query));

        assertEquals(1, results.size());
        assertFalse(results.get(0).isSuccess());
        assertEquals("Invalid SQL syntax or unknown column/table.", results.get(0).getErrorMessage());
    }

    @Test
    void testQueryThrowsGenericException() {
        String query = "UPDATE users SET name='Error' WHERE id=1";

        Mockito.when(entityManager.createNativeQuery(query)).thenThrow(new RuntimeException("Something broke"));

        List<QueryResult> results = hapCERService.executeQueries(List.of(query));

        assertEquals(1, results.size());
        assertFalse(results.get(0).isSuccess());
        assertTrue(results.get(0).getErrorMessage().startsWith("Unexpected error occurred:"));
    }
                                                                 }
