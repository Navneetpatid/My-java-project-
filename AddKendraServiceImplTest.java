//package com.janaushadhi.adminservice.serviceimpl;
//
//import com.janaushadhi.adminservice.entity.AddKendra;
//import com.janaushadhi.adminservice.externalservices.KendraService;
//import com.janaushadhi.adminservice.repository.AddKendraRepository;
//import com.janaushadhi.adminservice.requestpayload.AddKendraRequest;
//import com.janaushadhi.adminservice.responsepayload.DistrictOfIndiaResponse;
//import com.janaushadhi.adminservice.responsepayload.GetAllKendra;
//import com.janaushadhi.adminservice.responsepayload.GetKendra;
//import com.janaushadhi.adminservice.util.CsvReadder;
//import com.janaushadhi.adminservice.util.DataConstant;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.dao.DataAccessResourceFailureException;
//import org.springframework.data.domain.*;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.io.IOException;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ActiveProfiles("test")
//class AddKendraServiceImplTest {
//@Mock
//     AddKendraRepository addKendraRepository;
//    @Mock
//    KendraService kendraService;
//@InjectMocks
//     AddKendraServiceImpl addKendraServiceImpl;
//@Mock
// CsvReadder csvReadder;
//    @BeforeEach
//    void serviceSetup() {
//        addKendraRepository = mock(AddKendraRepository.class);
//        kendraService = mock(KendraService.class);
//
//
//        addKendraServiceImpl = new AddKendraServiceImpl(addKendraRepository,kendraService);
//    }
//
//    @Test
//    void testUpdateExistingKendra() throws IOException {
//        AddKendraRequest request = new AddKendraRequest();
//        request.setId(1L);
//        request.setStoreCode("SC001");
//        request.setContactPerson("John Doe");
//        request.setKendraAddress("123 Street");
//        request.setStateId(1L);
//        request.setDistrictId(1L);
//        request.setPinCode(123456L);
//
//        AddKendra existingKendra = new AddKendra();
//        existingKendra.setId(1L);
//
//        when(addKendraRepository.findById(request.getId())).thenReturn(Optional.of(existingKendra));
//        when(addKendraRepository.save(any(AddKendra.class))).thenReturn(existingKendra);
//
//        Map<String, Object> response = addKendraServiceImpl.addKendra(request);
//
//        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.KENDRA_UPDATED, response.get(DataConstant.MESSAGE));
//    }
//
//    @Test
//    void testUpdateNonExistingKendra() throws IOException {
//        AddKendraRequest request = new AddKendraRequest();
//        request.setId(1L);
//        when(addKendraRepository.findById(request.getId())).thenReturn(Optional.empty());
//        Map<String, Object> response = addKendraServiceImpl.addKendra(request);
//        assertEquals(DataConstant.NOT_FOUND, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.RECORD_NOT_FOUND_MESSAGE, response.get(DataConstant.MESSAGE));
//    }
//
//    @Test
//    void testAddNewKendra() throws IOException {
//        AddKendraRequest request = new AddKendraRequest();
//        request.setStoreCode("SC002");
//        request.setContactPerson("Jane Doe");
//        request.setKendraAddress("456 Avenue");
//        request.setStateId(1L);
//        request.setDistrictId(1L);
//        request.setPinCode(654321L);
//
//        AddKendra newKendra = new AddKendra();
//        newKendra.setId(2L);
//
//        when(addKendraRepository.findByStoreCode(request.getStoreCode())).thenReturn(Optional.empty());
//        when(addKendraRepository.save(any(AddKendra.class))).thenReturn(newKendra);
//
//        Map<String, Object> response = addKendraServiceImpl.addKendra(request);
//
//        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.KENDRA_ADDED_SUCCESSFULLY, response.get(DataConstant.MESSAGE));
//    }
//
//    @Test
//    void testAddNewKendraWithDuplicateStoreCode() throws IOException {
//        AddKendraRequest request = new AddKendraRequest();
//        request.setStoreCode("SC002");
//        request.setContactPerson("Jane Doe");
//        request.setKendraAddress("456 Avenue");
//        request.setStateId(1L);
//        request.setDistrictId(1L);
//        request.setPinCode(654321L);
//
//        AddKendra existingKendra = new AddKendra();
//        existingKendra.setId(2L);
//
//        when(addKendraRepository.findByStoreCode(request.getStoreCode())).thenReturn(Optional.of(existingKendra));
//
//        Map<String, Object> response = addKendraServiceImpl.addKendra(request);
//
//        assertEquals(DataConstant.BAD_REQUEST, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.CODE_ALREADY_EXIST, response.get(DataConstant.MESSAGE));
//    }
//
//    @Test
//    void testAddNewKendraWithMissingFields() throws IOException {
//        AddKendraRequest request = new AddKendraRequest();
//        request.setStoreCode("");
//        request.setContactPerson("");
//        request.setKendraAddress("");
//        request.setStateId(0L);
//        request.setDistrictId(0L);
//        request.setPinCode(0L);
//
//        Map<String, Object> response = addKendraServiceImpl.addKendra(request);
//
//        assertEquals(DataConstant.BAD_REQUEST, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.ALL_FIELDS_ARE_MANDATORY, response.get(DataConstant.MESSAGE));
//    }
//
//    @Test
//    void testHandleException() throws IOException {
//        AddKendraRequest request = new AddKendraRequest();
//        request.setId(1L);
//
//        when(addKendraRepository.findById(request.getId())).thenThrow(RuntimeException.class);
//
//        Map<String, Object> response = addKendraServiceImpl.addKendra(request);
//
//        assertEquals(DataConstant.SERVER_ERROR, response.get(DataConstant.RESPONSE_CODE));
//  //      assertEquals(DataConstant.SERVER_MESSAGE, response.get(DataConstant.MESSAGE));
//    }
//
//    @Test
//    void kendraStatusUpdate() {
//    }
//
//    private final Long validId = 1L;
//    private final Long invalidId = 2L;
//    private AddKendra addKendra;
//
//    @BeforeEach
//    void setUp() {
//        addKendra = new AddKendra(); // Initialize the AddKendra object as needed
//        addKendra.setId(validId);
//    }
//
//    @Test
//    void testGetByKendraId_Found() {
//        when(addKendraRepository.findById(validId)).thenReturn(Optional.of(addKendra));
//
//        Map<String, Object> result = addKendraServiceImpl.getByKendraId(validId);
//
//        assertEquals(DataConstant.OK, result.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.KENDRA_FOUNDED_SUCCESSFULLY, result.get(DataConstant.MESSAGE));
//        assertTrue(result.containsKey(DataConstant.RESPONSE_BODY));
//        assertEquals(addKendra, ((Optional<AddKendra>) result.get(DataConstant.RESPONSE_BODY)).get());
//    }
//
//    @Test
//    void testGetByKendraId_NotFound() {
//        when(addKendraRepository.findById(invalidId)).thenReturn(Optional.empty());
//
//        Map<String, Object> result = addKendraServiceImpl.getByKendraId(invalidId);
//
//        assertEquals(DataConstant.BAD_REQUEST, result.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.RECORD_NOT_FOUND_MESSAGE, result.get(DataConstant.MESSAGE));
//        assertNull(result.get(DataConstant.RESPONSE_BODY));
//    }
//
//    @Test
//    void testGetByKendraId_Exception() {
//        when(addKendraRepository.findById(validId)).thenThrow(new RuntimeException("Database error"));
//
//        Map<String, Object> result = addKendraServiceImpl.getByKendraId(validId);
//
//        assertEquals(DataConstant.SERVER_ERROR, result.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.SERVER_MESSAGE, result.get(DataConstant.MESSAGE));
//    }
//
//    @Test
//    void uploadCsvFileToDataBase_NullFile() throws IOException {
//        Map<Object, Object> response = addKendraServiceImpl.uploadCsvFileToDataBase(null);
//
//        assertNotNull(response);
//        assertEquals(DataConstant.BAD_REQUEST, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.RECORD_NOT_FOUND_MESSAGE, response.get(DataConstant.MESSAGE));
//        assertNull(response.get(DataConstant.RESPONSE_BODY));
//    }
//
////    @Test
////    void uploadCsvFileToDataBase_Success() throws IOException {
////        // Mock the CSV content
////        String csvContent = "id,contact_person,store_code,pin_code,state_id,district_id,kendra_address,created_date,updated_date,status\n" +
////                "1,John Doe,STORE001,123456,1,1,123 Main St,2024-01-01,2024-01-01,1";
////        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());
////
////        // Create the expected Kendra list
////        List<AddKendra> kendraList = new ArrayList<>();
////        AddKendra kendra = new AddKendra(1L, "John Doe", "STORE001", "123456", 1L, 1L, "123 Main St", new Date(), new Date(), (short) 1);
////        kendraList.add(kendra);
////
////        // Mock the csvReadder to return the kendraList when readCsvKendra is called
////        when(csvReadder.readCsvKendra(file.getInputStream())).thenReturn(kendraList);
////
////        // Mock the repository save method to return the kendra object
////        when(addKendraRepository.save(any(AddKendra.class))).thenReturn(kendra);
////
////        // Call the service method
////        Map<Object, Object> response = addKendraServiceImpl.uploadCsvFileToDataBase(file);
////
////        // Assertions
////        assertNotNull(response);
////        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
////        assertEquals(DataConstant.DATA_STORE_SUCCESSFULLY, response.get(DataConstant.MESSAGE));
////        assertNotNull(response.get(DataConstant.RESPONSE_BODY));
////
////        // Verify that the save method was called once
////        verify(addKendraRepository, times(1)).save(any(AddKendra.class));
////    }
//
//
////    @Test
////    void uploadCsvFileToDataBase_EmptyFile() throws IOException {
////        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "".getBytes());
////
////        when(csvReadder.readCsvKendra(file.getInputStream())).thenReturn(new ArrayList<>());
////
////        Map<Object, Object> response = addKendraServiceImpl.uploadCsvFileToDataBase(file);
////
////        assertNotNull(response);
////        assertEquals(DataConstant.NOT_FOUND, response.get(DataConstant.RESPONSE_CODE));
////        assertEquals(DataConstant.FILE_NOT_FOUND, response.get(DataConstant.MESSAGE));
////        assertNull(response.get(DataConstant.RESPONSE_BODY));
////    }
//
////    @Test
////    void uploadCsvFileToDataBase_Exception() throws IOException {
////        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "invalid content".getBytes());
////
////        when(csvReadder.readCsvKendra(file.getInputStream())).thenThrow(new RuntimeException("Invalid data"));
////
////        Map<Object, Object> response = addKendraServiceImpl.uploadCsvFileToDataBase(file);
////
////        assertNotNull(response);
////        assertEquals(DataConstant.NOT_FOUND, response.get(DataConstant.RESPONSE_CODE));
////        assertEquals("Csv file content improper data, Please enter proper image url and corresponding data !!", response.get(DataConstant.MESSAGE));
////        assertNull(response.get(DataConstant.RESPONSE_BODY));
////    }
//
//
//
//    @Test
//    void testGetAllKendra_PageIndexAndSizeNull() {
//        GetAllKendra getAllKendra = new GetAllKendra();
//        getAllKendra.setPageIndex(null);
//        getAllKendra.setPageSize(null);
//
//        Map<String, Object> result = addKendraServiceImpl.getAllKendra(getAllKendra);
//
//        assertEquals(DataConstant.BAD_REQUEST, result.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE, result.get(DataConstant.MESSAGE));
//        assertNull(result.get(DataConstant.RESPONSE_BODY));
//    }
//
//    @Test
//    void testGetAllKendra_NoResultsFound() {
//        GetAllKendra getAllKendra = new GetAllKendra();
//        getAllKendra.setPageIndex(0);
//        getAllKendra.setPageSize(0);
//        getAllKendra.setSearchText(null);
//        getAllKendra.setColumnName(null);
//        getAllKendra.setOrderBy(null);
//
//        when(addKendraRepository.findAllWhereStatusNotTwo()).thenReturn(Collections.emptyList());
//
//        Map<String, Object> result = addKendraServiceImpl.getAllKendra(getAllKendra);
//
//        assertEquals(DataConstant.NOT_FOUND, result.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.RECORD_NOT_FOUND_MESSAGE, result.get(DataConstant.MESSAGE));
//        assertNotNull(result.get(DataConstant.RESPONSE_BODY));
//    }
//
//    @Test
//    void testGetAllKendra_ResultsFound() {
//        GetAllKendra getAllKendra = new GetAllKendra();
//        getAllKendra.setPageIndex(0);
//        getAllKendra.setPageSize(0);
//        getAllKendra.setSearchText(null);
//        getAllKendra.setColumnName(null);
//        getAllKendra.setOrderBy(null);
//        DistrictOfIndiaResponse districtOfIndiaResponse=new DistrictOfIndiaResponse();
//        districtOfIndiaResponse.setId(1L);
//        List<AddKendra> kendras = Arrays.asList(new AddKendra());
//        when(addKendraRepository.findAllWhereStatusNotTwo()).thenReturn(kendras);
//        when(kendraService.getDistrictOfIndiaByDistrictId(districtOfIndiaResponse.getId())).thenReturn(new HashMap<>());
//
//        Map<String, Object> result = addKendraServiceImpl.getAllKendra(getAllKendra);
//
//        assertEquals(DataConstant.OK, result.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.RECORD_FOUND_MESSAGE, result.get(DataConstant.MESSAGE));
//        assertNotNull(result.get(DataConstant.RESPONSE_BODY));
//    }
//
//    @Test
//    void testGetAllKendra_WithPagination() {
//        GetAllKendra getAllKendra = new GetAllKendra();
//        getAllKendra.setPageIndex(1);
//        getAllKendra.setPageSize(10);
//        DistrictOfIndiaResponse districtOfIndiaResponse=new DistrictOfIndiaResponse();
//        districtOfIndiaResponse.setId(1L);
//
//        Pageable pageable = PageRequest.of(1, 10);
//        Page<AddKendra> page = new PageImpl<>(Arrays.asList(new AddKendra()), pageable, 1);
//        when(addKendraRepository.findAllWhereStatusNotTwo(pageable)).thenReturn(page);
//        when(kendraService.getDistrictOfIndiaByDistrictId(districtOfIndiaResponse.getId())).thenReturn(new HashMap<>());
//
//        Map<String, Object> result = addKendraServiceImpl.getAllKendra(getAllKendra);
//
//        assertEquals(DataConstant.OK, result.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.RECORD_FOUND_MESSAGE, result.get(DataConstant.MESSAGE));
//        assertNotNull(result.get(DataConstant.RESPONSE_BODY));
//    }
//
//    @Test
//    void testGetAllKendra_DBConnectionError() {
//        GetAllKendra getAllKendra = new GetAllKendra();
//        getAllKendra.setPageIndex(0);
//        getAllKendra.setPageSize(0);
//
//        when(addKendraRepository.findAllWhereStatusNotTwo()).thenThrow(new DataAccessResourceFailureException("DB connection error"));
//
//        Map<String, Object> result = addKendraServiceImpl.getAllKendra(getAllKendra);
//
//        assertEquals(DataConstant.DB_CONNECTION_ERROR, result.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.INVALID_CREDENTIAL, result.get(DataConstant.MESSAGE));
//    }
//
//    @Test
//    void testGetAllKendra_ServerError() {
//        GetAllKendra getAllKendra = new GetAllKendra();
//        getAllKendra.setPageIndex(0);
//        getAllKendra.setPageSize(0);
//
//        when(addKendraRepository.findAllWhereStatusNotTwo()).thenThrow(new RuntimeException("Unexpected error"));
//
//        Map<String, Object> result = addKendraServiceImpl.getAllKendra(getAllKendra);
//
//        assertEquals(DataConstant.SERVER_ERROR, result.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.SERVER_MESSAGE, result.get(DataConstant.MESSAGE));
//    }
//    @Test
//    void testGetAllKendraByStateDistrict_pageIndexAndPageSizeNull() {
//        GetKendra getKendra = new GetKendra();
//        getKendra.setPageIndex(null);
//        getKendra.setPageSize(null);
//
//        Map<String, Object> response = addKendraServiceImpl.getAllKendraByStateDistrict(getKendra);
//
//        assertEquals(DataConstant.BAD_REQUEST, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE, response.get(DataConstant.MESSAGE));
//    }
//
//    @Test
//    void testGetAllKendraByStateDistrict_pageIndexAndPageSizeZero() {
//        GetKendra getKendra = new GetKendra();
//        getKendra.setPageIndex(0);
//        getKendra.setPageSize(0);
//        getKendra.setStateId(0L);
//        getKendra.setDistrictId(0L);
//        List<AddKendra> kendras = Arrays.asList(new AddKendra(), new AddKendra());
//        when(addKendraRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(kendras);
//
//        Map<String, Object> response = addKendraServiceImpl.getAllKendraByStateDistrict(getKendra);
//
//        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.RECORD_FOUND_MESSAGE, response.get(DataConstant.MESSAGE));
//    }
//
//    @Test
//    void testGetAllKendraByStateDistrict_stateIdAndDistrictIdSpecified() {
//        GetKendra getKendra = new GetKendra();
//        getKendra.setPageIndex(0);
//        getKendra.setPageSize(0);
//        getKendra.setStateId(1L);
//        getKendra.setDistrictId(1L);
//
//        List<AddKendra> kendras = Arrays.asList(new AddKendra(), new AddKendra());
//        when(addKendraRepository.findAllByStateIdAndDistrictId(getKendra.getStateId(), getKendra.getDistrictId(), Sort.by(Sort.Direction.ASC, "id"))).thenReturn(kendras);
//
//        Map<String, Object> response = addKendraServiceImpl.getAllKendraByStateDistrict(getKendra);
//
//        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.RECORD_FOUND_MESSAGE, response.get(DataConstant.MESSAGE));
//    }
//
//    @Test
//    void testGetAllKendraByStateDistrict_onlyStateIdSpecified() {
//        GetKendra getKendra = new GetKendra();
//        getKendra.setPageIndex(0);
//        getKendra.setPageSize(0);
//        getKendra.setStateId(1L);
//
//        List<AddKendra> kendras = Arrays.asList(new AddKendra(), new AddKendra());
//        when(addKendraRepository.findAllByStateId(getKendra.getStateId(), Sort.by(Sort.Direction.ASC, "id"))).thenReturn(kendras);
//
//        Map<String, Object> response = addKendraServiceImpl.getAllKendraByStateDistrict(getKendra);
//
//        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.RECORD_FOUND_MESSAGE, response.get(DataConstant.MESSAGE));
//    }
//
//    @Test
//    void testGetAllKendraByStateDistrict_validPageIndexAndPageSize() {
//        GetKendra getKendra = new GetKendra();
//        getKendra.setPageIndex(1);
//        getKendra.setPageSize(10);
//        getKendra.setDistrictId(0L);
//        getKendra.setStateId(0L);
//        Pageable pageable = PageRequest.of(getKendra.getPageIndex(), getKendra.getPageSize(), Sort.by(Sort.Direction.ASC, "id"));
//        Page<AddKendra> page = mock(Page.class);
//        List<AddKendra> kendras = Arrays.asList(new AddKendra(), new AddKendra());
//        when(page.getContent()).thenReturn(kendras);
//        when(page.getNumber()).thenReturn(1);
//        when(page.getSize()).thenReturn(10);
//        when(page.getTotalElements()).thenReturn(2L);
//        when(page.getTotalPages()).thenReturn(1);
//        when(page.isFirst()).thenReturn(true);
//        when(page.isLast()).thenReturn(true);
//
//        when(addKendraRepository.findAll(pageable)).thenReturn(page);
//
//        Map<String, Object> response = addKendraServiceImpl.getAllKendraByStateDistrict(getKendra);
//
//        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.RECORD_FOUND_MESSAGE, response.get(DataConstant.MESSAGE));
//    }
//    @Test
//    void testGetAllKendraByStateDistrict_noRecordsFound() {
//        GetKendra getKendra = new GetKendra();
//        getKendra.setPageIndex(0);
//        getKendra.setPageSize(0);
//        getKendra.setStateId(0L);
//        getKendra.setDistrictId(0L);
//        when(addKendraRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(Collections.emptyList());
//
//        Map<String, Object> response = addKendraServiceImpl.getAllKendraByStateDistrict(getKendra);
//
//        assertEquals(DataConstant.NOT_FOUND, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.RECORD_NOT_FOUND_MESSAGE, response.get(DataConstant.MESSAGE));
//    }
//
//    @Test
//    void testGetAllKendraByStateDistrict_dbConnectionError() {
//        GetKendra getKendra = new GetKendra();
//        getKendra.setPageIndex(0);
//        getKendra.setPageSize(0);
//        getKendra.setStateId(0L);
//        getKendra.setDistrictId(0L);
//        when(addKendraRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenThrow(new DataAccessResourceFailureException("DB connection error"));
//
//        Map<String, Object> response = addKendraServiceImpl.getAllKendraByStateDistrict(getKendra);
//
//        assertEquals(DataConstant.DB_CONNECTION_ERROR, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.INVALID_CREDENTIAL, response.get(DataConstant.MESSAGE));
//    }
//
//    @Test
//    void testGetAllKendraByStateDistrict_serverError() {
//        GetKendra getKendra = new GetKendra();
//        getKendra.setPageIndex(0);
//        getKendra.setPageSize(0);
//        getKendra.setStateId(0L);
//        getKendra.setDistrictId(0L);
//        when(addKendraRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenThrow(new RuntimeException("Server error"));
//
//        Map<String, Object> response = addKendraServiceImpl.getAllKendraByStateDistrict(getKendra);
//
//        assertEquals(DataConstant.SERVER_ERROR, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.SERVER_MESSAGE, response.get(DataConstant.MESSAGE));
//    }
//}
