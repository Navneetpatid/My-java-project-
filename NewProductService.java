package com.janaushadhi.adminservice.service;
import com.janaushadhi.adminservice.responsepayload.GetAllDeleteProduct;
import org.springframework.web.multipart.MultipartFile;

import com.janaushadhi.adminservice.requestpayload.GetDataRequest;
import com.janaushadhi.adminservice.requestpayload.NewProductRequestPayload;
import com.janaushadhi.adminservice.requestpayload.ProductDeletePayload;
import com.janaushadhi.adminservice.requestpayload.ProductSearchRequest;

import java.io.IOException;
import java.util.Map;

public interface NewProductService {

    Map<Object, Object> addUpdateProduct(NewProductRequestPayload newProductRequestPayload);

    Map<String, Object> getAllProduct(GetDataRequest request);

    //---------------------------------------------Active&Inactive-----------------
    Map<Object, Object> updateProductStatus(Long id, short status);

    //---------------------------------------------Delete--------------------------
    Map<Object, Object> deleteProduct(Long id);

    //---------------------------------------------Search--------------------------
    Map<Object, Object> searchProducts(ProductSearchRequest request);

    //----------------------Upload-DAta-BAse-File-to-Csv------------------------
    Map<Object, Object> uploadCsvFileToDataBase(MultipartFile docfile) throws IOException;

	Map<Object, Object> deleteProductBulk(ProductDeletePayload productIds);
    Map<String, Object> getAllDeleteProduct(GetAllDeleteProduct request);
}
