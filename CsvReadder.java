package com.janaushadhi.adminservice.util;

import com.janaushadhi.adminservice.entity.*;
import com.janaushadhi.adminservice.entity.AddKendra;
import com.janaushadhi.adminservice.repository.AddProductRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV Reader Controller
 */
public class CsvReadder {


    @Autowired
    AddProductRepository addProductRepository;


    public static List<Newproduct> readCsv(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, DataConstant.UTF));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
        	//CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withQuote(null))) {
                
        	List<Newproduct> countryList = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                Newproduct newproduct = new Newproduct();
                newproduct.setDrugCode(Integer.valueOf(csvRecord.get(DataConstant.DRUG_CODE)));
                newproduct.setGenericName(csvRecord.get(DataConstant.GENERIC_NAME));
                newproduct.setGroupName(csvRecord.get(DataConstant.GROUP_NAME));
                newproduct.setMrp(Double.valueOf(csvRecord.get(DataConstant.MRP)));
                newproduct.setUnitSize(csvRecord.get(DataConstant.UNIT_SIZE));
               
              //  newproduct.setStatus(Short.valueOf(csvRecord.get(DataConstant.STATUS)));
                
                countryList.add(newproduct);
            }
            return countryList;
        } catch (IOException e) {
            throw new RuntimeException(DataConstant.FAIL_CSV_FILE + e.getMessage());
        }
    }


    public static List<LocateDistributer> readCsvs(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, DataConstant.UTF));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            List<LocateDistributer> distributerList = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                LocateDistributer distributer = new LocateDistributer();

                distributer.setNameOfFirm( csvRecord.get(DataConstant.NAMEOFFIRM));
                distributer.setEmail(csvRecord.get(DataConstant.Email));
                distributer.setContactNumber(csvRecord.get(DataConstant.CONTANCT));
                distributer.setCode(csvRecord.get(DataConstant.CODE));
                distributer.setDistributorAddress(csvRecord.get(DataConstant.ADDRESS));
//                distributer.setStatus(Short.valueOf(csvRecord.get(DataConstant.STATUS)));
                distributer.setStateId(Long.valueOf(csvRecord.get(DataConstant.STATE_ID)));
                distributer.setDistrictId(Long.valueOf(csvRecord.get(DataConstant.DISTRICT_ID)));
                distributer.setCityId(Long.valueOf(csvRecord.get("CityId")));
//                distributer.setCreatedDate(csvRecord.get(DataConstant.CREATED_DATE));
//                distributer.setUpdateddate(Date.valueOf(csvRecord.get("update_date")));

                distributerList.add(distributer);
            }
            return distributerList;
        } catch (IOException e) {
            throw new RuntimeException(DataConstant.FAIL_CSV_FILE + e.getMessage());
        }
    }

    public static List<AddKendra> readCsvKendra(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, DataConstant.UTF));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            List<AddKendra> kendraList = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
           // int i=0;
            for (CSVRecord csvRecord : csvRecords) {
                AddKendra kendra = new AddKendra();

                kendra.setContactPerson( csvRecord.get("ContactPerson"));
                kendra.setStoreCode(csvRecord.get("StoreCode"));
                kendra.setKendraAddress(csvRecord.get("KendraAddress"));
                if(csvRecord.get("PinCode") != null && !csvRecord.get("PinCode").trim().isEmpty()) {
                kendra.setPinCode(Long.valueOf(csvRecord.get("PinCode")));}
//                kendra.setStatus(Short.valueOf(csvRecord.get(DataConstant.STATUS)));
                kendra.setStateId(Long.valueOf(csvRecord.get(DataConstant.STATE_ID)));
                kendra.setDistrictId(Long.valueOf(csvRecord.get(DataConstant.DISTRICT_ID)));
                kendra.setLatitude(csvRecord.get("Latitude"));
                kendra.setLongitude(csvRecord.get("Longitude"));
//                kendra.setCreatedDate(csvRecord.get(DataConstant.CREATED_DATE));
//                kendra.setUpdateddate(Date.valueOf(csvRecord.get("update_date")));
                System.out.println("kendra == "+kendra.getStoreCode());
                kendraList.add(kendra);
            }
            return kendraList;
        } catch (IOException e) {
            throw new RuntimeException(DataConstant.FAIL_CSV_FILE + e.getMessage());
        }
    }

    public static List<DebarredList> readCsvForDebarred(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, DataConstant.UTF));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            List<DebarredList> debarredLists = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                DebarredList debarredList = new DebarredList();

                debarredList.setDrugName( csvRecord.get("drug_name"));
                debarredList.setDrugCode(csvRecord.get("drug_code"));
                debarredList.setManufacturedBy(csvRecord.get("manufactured_by"));
                debarredList.setReason(csvRecord.get("reason"));
//                debarredList.setStatus(Short.valueOf(csvRecord.get(DataConstant.STATUS))); //by default status =1 Active
                debarredList.setToDate(csvRecord.get("to_date"));
                debarredList.setFromDate(csvRecord.get("from_date"));
//                debarredList.setCreatedDate(csvRecord.get(DataConstant.CREATED_DATE));
//                debarredList.setUpdateddate(Date.valueOf(csvRecord.get("update_date")));

                debarredLists.add(debarredList);
            }
            return debarredLists;
        } catch (IOException e) {
            throw new RuntimeException(DataConstant.FAIL_CSV_FILE + e.getMessage());
        }
    }
    public static List<BlackList> readCsvForBlackList(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, DataConstant.UTF));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            List<BlackList> blackLists = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                BlackList blackList = new BlackList();

                blackList.setDrugName( csvRecord.get("drug_name"));
                blackList.setDrugCode(csvRecord.get("drug_code"));
                blackList.setManufacturedBy(csvRecord.get("manufactured_by"));
                blackList.setReason(csvRecord.get("reason"));
//               blackList.setStatus(Short.valueOf(csvRecord.get(DataConstant.STATUS)));
                blackList.setToDate(csvRecord.get("to_date"));
                blackList.setFromDate(csvRecord.get("from_date"));
//                blackList.setCreatedDate(csvRecord.get(DataConstant.CREATED_DATE));
//                blackList.setUpdateddate(Date.valueOf(csvRecord.get("update_date")));

                blackLists.add(blackList);
            }
            return blackLists;
        } catch (IOException e) {
            throw new RuntimeException(DataConstant.FAIL_CSV_FILE + e.getMessage());
        }
    }
    public static List<RevocationOrder> readCsvForRevocationOrder(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, DataConstant.UTF));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            List<RevocationOrder> debarredLists = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                RevocationOrder revocationOrder = new RevocationOrder();

                revocationOrder.setDrugName( csvRecord.get("drug_name"));
                revocationOrder.setDrugCode(csvRecord.get("drug_code"));
                revocationOrder.setManufacturedBy(csvRecord.get("manufactured_by"));
                revocationOrder.setReason(csvRecord.get("reason"));
//                revocationOrder.setStatus(Short.valueOf(csvRecord.get(DataConstant.STATUS)));
                revocationOrder.setBlackListDate(csvRecord.get("black_list_date"));
                revocationOrder.setUploadImage(csvRecord.get("upload_image"));

//                revocationOrder.setCreatedDate(csvRecord.get(DataConstant.CREATED_DATE));
//                revocationOrder.setUpdateddate(Date.valueOf(csvRecord.get("update_date")));

                debarredLists.add(revocationOrder);
            }
            return debarredLists;
        } catch (IOException e) {
            throw new RuntimeException(DataConstant.FAIL_CSV_FILE + e.getMessage());
        }
    }
}