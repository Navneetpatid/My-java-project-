package com.janaushadhi.adminservice.serviceimpl;
import com.janaushadhi.adminservice.entity.Newproduct;
import com.janaushadhi.adminservice.repository.AddProductRepository;
import com.janaushadhi.adminservice.requestpayload.GetDataRequest;
import com.janaushadhi.adminservice.requestpayload.NewProductRequestPayload;
import com.janaushadhi.adminservice.requestpayload.ProductDeletePayload;
import com.janaushadhi.adminservice.requestpayload.ProductSearchRequest;
import com.janaushadhi.adminservice.responsepayload.GetAllDeleteProduct;
import com.janaushadhi.adminservice.responsepayload.NewProductRPageResponse;
import com.janaushadhi.adminservice.responsepayload.NewProductResponse;
import com.janaushadhi.adminservice.service.NewProductService;
import com.janaushadhi.adminservice.util.CsvReadder;
import com.janaushadhi.adminservice.util.DataConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class ProductServiceImpl implements NewProductService {

    @Autowired
    private AddProductRepository addProductRepository;

    @Value("${spring.url}")
    private String url;

    @Value("${spring.dir}")
    private String UploadDir;


    @Override
    public Map<Object, Object> addUpdateProduct(NewProductRequestPayload newProductRequestPayload) {
        Map<Object, Object> response = new HashMap<>();
        try {
            // Check if any mandatory field is null or empty
            if (newProductRequestPayload.getGenericName()==null || newProductRequestPayload.getGenericName().trim().isEmpty() || 
            		newProductRequestPayload.getGroupName()==null ||	newProductRequestPayload.getGroupName().trim().isEmpty() || 
            		newProductRequestPayload.getDrugCode()==null || newProductRequestPayload.getDrugCode()!=0 ||
            		newProductRequestPayload.getUnitSize()==null || newProductRequestPayload.getUnitSize().trim().isEmpty() || 
            		newProductRequestPayload.getMrp()==null ) {
                response.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                response.put(DataConstant.MESSAGE, DataConstant.MANDATORY_FIELDS_MISSING);
                log.error("Mandatory fields are missing in the product request payload");
                return response;
            }

            if (newProductRequestPayload.getProductId() == 0) {
                Newproduct newProductEntity = new Newproduct();
                BeanUtils.copyProperties(newProductRequestPayload, newProductEntity);
                newProductEntity.setStatus((short) 1);
                Newproduct productEntity = addProductRepository.save(newProductEntity);
                    response.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    response.put(DataConstant.MESSAGE, DataConstant.PRODUCT_ADD_SUCCESSFULLY);
                    response.put(DataConstant.DATA, productEntity);
                    // Log success message
                    log.info("New product added successfully: {}", productEntity);
                    return response;
             
                 
            } else {
                Newproduct productData = addProductRepository.findByproductId(newProductRequestPayload.getProductId());
                if (productData != null) {
                    BeanUtils.copyProperties(newProductRequestPayload, productData);
                    productData.setStatus((short)1);
                    addProductRepository.save(productData);

                    response.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    response.put(DataConstant.MESSAGE, DataConstant.PRODUCT_UPDATE_SUCCESSFULLY);
                    // Log success message
                    log.info("Product updated successfully: {}", productData);
                    return response;
                } else {
                    response.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    response.put(DataConstant.MESSAGE, DataConstant.PRODUCT_FAILED_TO_UPDATE);
                    // Log failure message
                    log.error("Failed to update product: No product found with ID {}", newProductRequestPayload.getProductId());
                    return response;
                }
            }
        } catch (Exception e) {
            log.error("Error occurred while processing product data", e);
            response.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            response.put(DataConstant.MESSAGE, DataConstant.SERVER_ISSUE);
            return response;
        }
    }

    @Override
    public Map<String, Object> getAllProduct(GetDataRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            if(request.getPageIndex()==null && request.getPageSize()==null) {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                map.put(DataConstant.RESPONSE_BODY,null );
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return map;
            }
            List<NewProductResponse> newProductResponsePayloadList = new ArrayList<>();
            NewProductRPageResponse newProductPageResponse = new NewProductRPageResponse();
            List<Newproduct> newproductListList = new ArrayList<>();
            Pageable pageable = null;
            Page<Newproduct> page = null;

            if (request.getPageIndex() == 0 && request.getPageSize() == 0) {
                if (request.getPageIndex() == 0 && request.getPageSize() == 0 && request.getSearchText().trim().isEmpty() &&  request.getColumnName().trim().isEmpty() &&  request.getOrderBy().trim().isEmpty()) {
                    newproductListList = addProductRepository.findAllNewProductroductAndStatusNot();
                } else if (request.getPageIndex() == 0 && request.getPageSize() == 0 && request.getSearchText() != null&&! request.getSearchText().trim().isEmpty() &&  request.getColumnName().trim().isEmpty() && request.getOrderBy().trim().isEmpty()) {
                    newproductListList = addProductRepository.findProductBySearch(request.getSearchText());
                } else if (request.getPageIndex() == 0 && request.getPageSize() == 0 &&  request.getSearchText().trim().isEmpty() && request.getColumnName() != null&& !request.getColumnName().trim().isEmpty() && request.getOrderBy().equals(DataConstant.ASC)) {
                    newproductListList = addProductRepository.searchAndOrderByASC(request.getColumnName());
                } else if (request.getPageIndex() == 0 && request.getPageSize() == 0 &&  request.getSearchText().trim().isEmpty() && request.getColumnName() != null&& ! request.getColumnName().trim().isEmpty() && request.getOrderBy().equals(DataConstant.DESC)) {
                    newproductListList = addProductRepository.searchAndOrderByDESC(request.getColumnName());
                } else if (request.getPageIndex() == 0 && request.getPageSize() == 0 && request.getSearchText() != null&&  !request.getSearchText().trim().isEmpty() && request.getColumnName() != null&& !request.getColumnName().trim().isEmpty() && request.getOrderBy().equals(DataConstant.ASC)) {
                    newproductListList = addProductRepository.findASC(request.getSearchText(), request.getColumnName());
                } else if (request.getPageIndex() == 0 && request.getPageSize() == 0 && request.getSearchText() != null && ! request.getSearchText().trim().isEmpty()&& request.getColumnName() != null&&  !request.getColumnName().trim().isEmpty() && request.getOrderBy().equals(DataConstant.DESC)) {
                    newproductListList = addProductRepository.findDESC(request.getSearchText(), request.getColumnName());
                }
                if (newproductListList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", newproductListList);
                    for (Newproduct product : newproductListList) {
                        NewProductResponse newProductResponsePayload = new NewProductResponse();
                        BeanUtils.copyProperties(product, newProductResponsePayload);
                        newProductResponsePayloadList.add(newProductResponsePayload);
                    }
                    newProductPageResponse.setNewProductResponsesList(newProductResponsePayloadList);
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, newProductPageResponse);
                    log.info("Record found! status - {}", newProductPageResponse);
                    return map;
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, newProductPageResponse);
                    log.info("Record not found! status - {}");
                    return map;
                }
            } else if (request.getPageIndex() != null && request.getPageSize() != null) {
                if ( request.getPageSize() >= 1) {
                    pageable = PageRequest.of(request.getPageIndex(), request.getPageSize(), Sort.by(Sort.Direction.DESC, "product_id"));
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (request.getPageIndex() != null && request.getPageSize() != 0 && request.getSearchText().trim().isEmpty() && request.getColumnName().trim().isEmpty() &&  request.getOrderBy().trim().isEmpty()) {
                        //   pageable = PageRequest.of(getAllAdmin.getPageIndex(), getAllAdmin.getPageSize());
                        page = addProductRepository.findAllAndStatu(pageable);

                    } else if (request.getPageIndex() != null && request.getPageSize() != 0 && request.getSearchText() != null && !request.getSearchText().trim().isEmpty()&& request.getColumnName().trim().isEmpty() &&  request.getOrderBy().trim().isEmpty()) {
                        page = addProductRepository.findAllNewProductsAndStatusNot22( request.getSearchText(),pageable);
                    } else if (request.getPageIndex() != null && request.getPageSize() != null &&  request.getSearchText().trim().isEmpty() && request.getColumnName() != null&& ! request.getColumnName().trim().isEmpty() && request.getOrderBy().equals(DataConstant.ASC)) {
                        page = addProductRepository.searchAndOrderByASC(request.getColumnName(), pageable);
                    } else if (request.getPageIndex() != null && request.getPageSize() != null &&  request.getSearchText().trim().isEmpty() && request.getColumnName() != null && ! request.getColumnName().trim().isEmpty()&& request.getOrderBy().equals(DataConstant.DESC)) {
                        page = addProductRepository.searchAndOrderByDESC(pageable, request.getColumnName());
                    } else if (request.getPageIndex() != null && request.getPageSize() != null && request.getSearchText() != null&& ! request.getSearchText().trim().isEmpty() && request.getColumnName() != null&& ! request.getColumnName().trim().isEmpty() && request.getOrderBy().equals(DataConstant.ASC)) {
                        page = addProductRepository.findASC(request.getSearchText(), request.getColumnName(), pageable);
                    } else if (request.getPageIndex() != null && request.getPageSize() != null && request.getSearchText() != null &&  !request.getSearchText().trim().isEmpty()&& request.getColumnName() != null && ! request.getColumnName().trim().isEmpty()&& request.getOrderBy().equals(DataConstant.DESC)) {
                        page = addProductRepository.findDESC(request.getSearchText(), request.getColumnName(), pageable);
                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", newproductListList);
                        newproductListList = page.getContent();
                        int index=0;
                        for (Newproduct productLoopDetail : newproductListList) {
                            NewProductResponse newProductResponsePayload = new NewProductResponse();
                            BeanUtils.copyProperties(productLoopDetail, newProductResponsePayload);
                            //for frontEnd team pagination
                            if(request.getPageIndex() == 0) {
                            	newProductResponsePayload.setSerialNo(index+1);
                        		index++;
                        	}else {
                        		newProductResponsePayload.setSerialNo((request.getPageSize()*request.getPageIndex())+(index+1));
                        		index++;
                        	}
                            newProductResponsePayloadList.add(newProductResponsePayload);
                            
                        }
                        newProductPageResponse.setNewProductResponsesList(newProductResponsePayloadList);
                        newProductPageResponse.setPageIndex(page.getNumber());
                        newProductPageResponse.setPageSize(page.getSize());
                        newProductPageResponse.setTotalElement(page.getTotalElements());
                        newProductPageResponse.setTotalPages(page.getTotalPages());
                        newProductPageResponse.setIsFirstPage(page.isFirst());
                        newProductPageResponse.setIsLastPage(page.isLast());

                        map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, newProductPageResponse);
                        log.info("Record found! status - {}", newProductPageResponse);
                    } else {
                        map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, newProductPageResponse);
                        log.info("Record not found! status - {}");
                    }
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info(" Invaild Page Size - {}", DataConstant.PAGE_SIZE_MESSAGE);
                    return map;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
        } catch (Exception e) {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
        }
        return map;
    }
    @Override
    public Map<Object, Object> updateProductStatus(Long id, short status) {

        Map<Object, Object> updateProductMap = new HashMap<>();
        Newproduct newproduct = null;
        try {
            newproduct = this.addProductRepository.findByProductIdAndStatusNot(id, (short) 2).orElse(null);

            if (newproduct != null) {
                newproduct.setStatus(status);
                this.addProductRepository.save(newproduct);
                if (status == 0) {
                    updateProductMap.put(DataConstant.OBJECT_RESPONSE, newproduct);
                    updateProductMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    updateProductMap.put(DataConstant.MESSAGE, DataConstant.DEACTIVATED_SUCCESSFULLY);
                    log.info("product status deactivated successfully",DataConstant.DEACTIVATED_SUCCESSFULLY);
                } else if (status == 1) {
                    updateProductMap.put(DataConstant.OBJECT_RESPONSE, newproduct);
                    updateProductMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    updateProductMap.put(DataConstant.MESSAGE, DataConstant.ACTIVATED_SUCCESSFULLY);
                    log.info("product status activated successfully",DataConstant.ACTIVATED_SUCCESSFULLY);

                } else {
                    updateProductMap.put(DataConstant.OBJECT_RESPONSE, null);
                    updateProductMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    updateProductMap.put(DataConstant.MESSAGE, DataConstant.INVALID_REQUEST);
                    log.info("PageNo PageSize can't be null",DataConstant.INVALID_REQUEST);

                }
            } else {
                updateProductMap.put(DataConstant.OBJECT_RESPONSE, null);
                updateProductMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                updateProductMap.put(DataConstant.MESSAGE, DataConstant.PRODUCT_NOT_FOUND);
                log.info("No products found.",DataConstant.PRODUCT_NOT_FOUND);

            }
        } catch (Exception e) {
            updateProductMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            updateProductMap.put(DataConstant.MESSAGE, DataConstant.SERVER_ISSUE);
            log.error("An error occurred while fetching products: " + e.getMessage());
        }
        return updateProductMap;
    }

    @Override
    public Map<Object, Object> deleteProduct(Long id) {

        Map<Object, Object> deleteProductMap = new HashMap<>();
        Newproduct newproduct = null;
        try {
            newproduct = this.addProductRepository.findByProductIdAndStatusNot(id, (short) 2).orElse(null);

            if (newproduct != null) {

                newproduct.setStatus((short) 2);
                this.addProductRepository.save(newproduct);
                deleteProductMap.put(DataConstant.OBJECT_RESPONSE, newproduct);
                deleteProductMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                deleteProductMap.put(DataConstant.MESSAGE, DataConstant.PRODUCT_DELETE_SUCCESSFULLY);
                log.info(" products  deleted successfully .",DataConstant.PRODUCT_DELETE_SUCCESSFULLY);
            } else {
                deleteProductMap.put(DataConstant.OBJECT_RESPONSE, null);
                deleteProductMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                deleteProductMap.put(DataConstant.MESSAGE, DataConstant.PRODUCT_NOT_FOUND);
                log.info("No products found.");
            }
        } catch (Exception e) {
            deleteProductMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            deleteProductMap.put(DataConstant.MESSAGE, DataConstant.SERVER_ISSUE);
            log.error("An error occurred while fetching products: " + e.getMessage());
        }
        return deleteProductMap;
    }

    @Override
    public Map<Object, Object> searchProducts(ProductSearchRequest request) {
        Map<Object, Object> searchProductMap = new HashMap<>();
        List<NewProductResponse> newproductList = new ArrayList<>();

        List<Newproduct> getNewProduct22 = null;

        Pageable pageable = null;
        Page<Newproduct> page = null;
        Integer pageSize = request.getPageSize();


        NewProductRPageResponse newProductRPageResponse = new NewProductRPageResponse();

        try {

            if ((request.getPageIndex() == 0 || request.getPageIndex() != 0) && request.getPageSize() != 0 && !request.getQuery().isEmpty()) {
                pageable = PageRequest.of(request.getPageIndex(), pageSize);
                page = this.addProductRepository.findAllNewProductsAndStatusNot22(request.getQuery(), pageable);

                if (!page.isEmpty()) {

                    for (Newproduct newProductsLoop : page) {
                        NewProductResponse newProductResponse = new NewProductResponse();
                        BeanUtils.copyProperties(newProductsLoop, newProductResponse);
                        newproductList.add(newProductResponse);
                    }
                    newProductRPageResponse.setNewProductResponsesList(newproductList);

                    newProductRPageResponse.setPageIndex(page.getNumber());
                    newProductRPageResponse.setPageSize(page.getSize());
                    newProductRPageResponse.setTotalElement(page.getTotalElements());
                    newProductRPageResponse.setTotalPages(page.getTotalPages());
                    searchProductMap.put(DataConstant.OBJECT_RESPONSE, newProductRPageResponse);
                    searchProductMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    searchProductMap.put(DataConstant.MESSAGE, DataConstant.PRODUCT_FOUND_SUCCESS);
                } else {
                    searchProductMap.put(DataConstant.OBJECT_RESPONSE, null);
                    searchProductMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    searchProductMap.put(DataConstant.MESSAGE, DataConstant.PRODUCT_NOT_FOUND);
                    log.info("No products found.");
                }
            } else if ((request.getPageIndex() == 0 || request.getPageIndex() != 0) && request.getPageSize() != 0 && request.getQuery().isEmpty()) {
                pageable = PageRequest.of(request.getPageIndex(), pageSize);
                page = this.addProductRepository.findAllByPage(pageable);

                if (!page.isEmpty()) {

                    for (Newproduct newProductLoopData : page) {
                        NewProductResponse newProductResponse = new NewProductResponse();
                        BeanUtils.copyProperties(newProductLoopData, newProductResponse);
                        newproductList.add(newProductResponse);
                    }
                    newProductRPageResponse.setNewProductResponsesList(newproductList);
                    searchProductMap.put(DataConstant.OBJECT_RESPONSE, newProductRPageResponse);
                    searchProductMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    searchProductMap.put(DataConstant.MESSAGE, DataConstant.PRODUCT_FOUND_SUCCESS);
                    log.info("products found successfully.",DataConstant.PRODUCT_FOUND_SUCCESS);

                } else {
                    searchProductMap.put(DataConstant.OBJECT_RESPONSE, null);
                    searchProductMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    searchProductMap.put(DataConstant.MESSAGE, DataConstant.PRODUCT_NOT_FOUND);
                    log.info("No products found.");
                }
            } else if (request.getPageIndex() == 0 && request.getPageSize() == 0 && !request.getQuery().isEmpty()) {

                getNewProduct22 = this.addProductRepository.findProductBySearch(request.getQuery());

                if (!getNewProduct22.isEmpty()) {

                    for (Newproduct newProductLoop : getNewProduct22) {
                        NewProductResponse newProductResponse = new NewProductResponse();
                        BeanUtils.copyProperties(newProductLoop, newProductResponse);
                        newproductList.add(newProductResponse);
                    }
                    newProductRPageResponse.setNewProductResponsesList(newproductList);
                    searchProductMap.put(DataConstant.OBJECT_RESPONSE, newProductRPageResponse);
                    searchProductMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    searchProductMap.put(DataConstant.MESSAGE, DataConstant.PRODUCT_FOUND_SUCCESS);
                    log.info(" products found successfully.",DataConstant.PRODUCT_FOUND_SUCCESS);

                } else {
                    searchProductMap.put(DataConstant.OBJECT_RESPONSE, null);
                    searchProductMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    searchProductMap.put(DataConstant.MESSAGE, DataConstant.PRODUCT_NOT_FOUND);
                    log.info("No products found.");
                }
            }
        } catch (Exception e) {
            searchProductMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            searchProductMap.put(DataConstant.MESSAGE, DataConstant.SERVER_ISSUE);
            log.error("An error occurred while fetching products: " + e.getMessage());
        }
        return searchProductMap;
    }


    @Override
    public Map<Object, Object> uploadCsvFileToDataBase(MultipartFile docfile) throws IOException {
        Map<Object, Object> csvProductMap = new HashMap<>();

        List<Newproduct> newproductList = CsvReadder.readCsv(docfile.getInputStream());
        List<Newproduct> products =new ArrayList<>();
        try {
            if (!newproductList.isEmpty()) {

                List<NewProductResponse> newProductResponsesList = new ArrayList<>();
                for (Newproduct newproduct1Loop : newproductList) {
                    Newproduct newproduct = new Newproduct();
                    newproduct.setGenericName(newproduct1Loop.getGenericName());
                    newproduct.setGroupName(newproduct1Loop.getGroupName());
                    newproduct.setMrp(newproduct1Loop.getMrp());
                    newproduct.setDrugCode(newproduct1Loop.getDrugCode());
                    newproduct.setUnitSize(newproduct1Loop.getUnitSize());
                    newproduct.setStatus(DataConstant.ONE);
                    products.add(newproduct);
                   // Newproduct savedNewProduct = addProductRepository.save(newproduct);
                    NewProductResponse newProductResponse = new NewProductResponse();
                    BeanUtils.copyProperties(newproduct, newProductResponse);
                    newProductResponsesList.add(newProductResponse);
                }
                addProductRepository.saveAll(products);
                csvProductMap.put(DataConstant.RESPONSE_BODY, newProductResponsesList);
                csvProductMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                csvProductMap.put(DataConstant.MESSAGE, DataConstant.DATA_STORE_SUCCESSFULLY);
                log.info(" products added successfully .",DataConstant.DATA_STORE_SUCCESSFULLY);
            } else {
                csvProductMap.put(DataConstant.RESPONSE_BODY, null);
                csvProductMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                csvProductMap.put(DataConstant.MESSAGE, DataConstant.FILE_NOT_FOUND);
                log.info(" products not  found .",DataConstant.FILE_NOT_FOUND);

            }
        } catch (Exception e) {
            log.error("Exception occurs while uploading city {}", e.getMessage());
            csvProductMap.put(DataConstant.RESPONSE_BODY, null);
            csvProductMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
            csvProductMap.put(DataConstant.MESSAGE, "Csv file content improper data, Please enter proper image url and corresponding data !!");
        }
        return csvProductMap;
    }
    
    @Override
    public Map<Object, Object> deleteProductBulk(ProductDeletePayload productIds) {
    	Map<Object, Object> productBulkMap = new HashMap<>();
        List<Newproduct> newproductList = null;
        List<Newproduct> list = new ArrayList<>();
        try {
        	if(productIds.getProductIds().isEmpty()) {
                productBulkMap.put(DataConstant.OBJECT_RESPONSE, null);
                productBulkMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                productBulkMap.put(DataConstant.MESSAGE, DataConstant.PLEASE_ADD_ID_TO_DELETE);
                log.info(DataConstant.PLEASE_ADD_ID_TO_DELETE);
                return productBulkMap;
        	}
            newproductList = this.addProductRepository.findAllById(productIds.getProductIds());
            if(newproductList!=null && !newproductList.isEmpty()) {
            for(Newproduct product:newproductList) {
            	product.setStatus((short)2);
            	list.add(product);
            }
            list=this.addProductRepository.saveAll(list);
                productBulkMap.put(DataConstant.OBJECT_RESPONSE, list);
                productBulkMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                productBulkMap.put(DataConstant.MESSAGE, DataConstant.PRODUCT_DELETE_SUCCESSFULLY);
            log.info(DataConstant.PRODUCT_DELETE_SUCCESSFULLY);
            }
            else{
                productBulkMap.put(DataConstant.OBJECT_RESPONSE, null);
                productBulkMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                productBulkMap.put(DataConstant.MESSAGE, DataConstant.PRODUCT_NOT_FOUND);
            log.info("No products found.");
        }
        } catch (Exception e) {
            productBulkMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            productBulkMap.put(DataConstant.MESSAGE, DataConstant.SERVER_ISSUE);
            log.error("An error occurred while fetching products: " + e.getMessage());
        }
        return productBulkMap;
    	
    }


    public Map<String, Object> getAllDeleteProduct(GetAllDeleteProduct request) {
        Map<String, Object> getAllDeleteMap = new HashMap<>();
        try {
            List<NewProductResponse> newProductResponsePayloadList = new ArrayList<>();
            NewProductRPageResponse newProductPageResponse = new NewProductRPageResponse();
            List<Newproduct> newproductListList = new ArrayList<>();
            Pageable pageable = null;
            Page<Newproduct> page = null;
            Short status=2;
            if (request.getPageIndex() == 0 && request.getPageSize() == 0) {
                if (request.getPageIndex() == 0 && request.getPageSize() == 0 ) {
                    newproductListList = addProductRepository.findAllByStatus(status);
                }
                if (newproductListList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", newproductListList);
                    for (Newproduct productDetails : newproductListList) {
                        NewProductResponse newProductResponsePayload = new NewProductResponse();
                        BeanUtils.copyProperties(productDetails, newProductResponsePayload);
                        newProductResponsePayloadList.add(newProductResponsePayload);
                    }
                    newProductPageResponse.setNewProductResponsesList(newProductResponsePayloadList);
                    getAllDeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    getAllDeleteMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    getAllDeleteMap.put(DataConstant.RESPONSE_BODY, newProductPageResponse);
                    log.info("Record found! status - {}", newProductPageResponse);
                    return getAllDeleteMap;
                } else {
                    getAllDeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    getAllDeleteMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    getAllDeleteMap.put(DataConstant.RESPONSE_BODY, newProductPageResponse);
                    log.info("Record not found! status - {}");
                    return getAllDeleteMap;
                }
            } else if (request.getPageIndex() != null && request.getPageSize() != null) {
                if (request.getPageSize() >= 1) {
                    pageable = PageRequest.of(request.getPageIndex(), request.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (request.getPageIndex() != null && request.getPageSize() != 0) {
                        //   pageable = PageRequest.of(getAllAdmin.getPageIndex(), getAllAdmin.getPageSize());
                        page = addProductRepository.findAllByStatus( status,pageable);

                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", newproductListList);
                        newproductListList = page.getContent();
                        int index=0;
                        for (Newproduct productData : newproductListList) {
                            NewProductResponse newProductResponsePayload = new NewProductResponse();
                            BeanUtils.copyProperties(productData, newProductResponsePayload);
                            //for frontEnd team pagination
                            if(request.getPageIndex() == 0) {
                                newProductResponsePayload.setSerialNo(index+1);
                                index++;
                            }else {
                                newProductResponsePayload.setSerialNo((request.getPageSize()*request.getPageIndex())+(index+1));
                                index++;
                            }
                            newProductResponsePayloadList.add(newProductResponsePayload);

                        }
                        newProductPageResponse.setNewProductResponsesList(newProductResponsePayloadList);
                        newProductPageResponse.setPageIndex(page.getNumber());
                        newProductPageResponse.setPageSize(page.getSize());
                        newProductPageResponse.setTotalElement(page.getTotalElements());
                        newProductPageResponse.setTotalPages(page.getTotalPages());
                        newProductPageResponse.setIsFirstPage(page.isFirst());
                        newProductPageResponse.setIsLastPage(page.isLast());

                        getAllDeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        getAllDeleteMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        getAllDeleteMap.put(DataConstant.RESPONSE_BODY, newProductPageResponse);
                        log.info("Record found! status - {}", newProductPageResponse);
                    } else {
                        getAllDeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        getAllDeleteMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        getAllDeleteMap.put(DataConstant.RESPONSE_BODY, newProductPageResponse);
                        log.info("Record not found! status - {}");
                    }
                } else {
                    getAllDeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    getAllDeleteMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Invaild Page Size");
                    return getAllDeleteMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            getAllDeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            getAllDeleteMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
        } catch (Exception e) {
            getAllDeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            getAllDeleteMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
        }
        return getAllDeleteMap;
    }

}