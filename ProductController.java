package com.janaushadhi.adminservice.controller;

import com.janaushadhi.adminservice.requestpayload.*;
import com.janaushadhi.adminservice.responsepayload.GetAllDeleteProduct;
import com.janaushadhi.adminservice.service.NewProductService;
import com.janaushadhi.adminservice.util.DataConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/product")
public class ProductController {

    @Autowired
    private NewProductService newProductService;


    @PostMapping("/addProduct")
    public Map<Object, Object> addUpdateProduct(@RequestBody NewProductRequestPayload requestPayload) {

        return newProductService.addUpdateProduct(requestPayload);
    }


    // --------------------------------------------DELETE-WORKING------------------------------------------------

    /**
     * @param id
     * @return
     * @SameerKhan
     */
    @DeleteMapping(value = "/softDeleteProduct")
    public Map<Object, Object> deleteProduct(@RequestParam Long id) {
        return newProductService.deleteProduct(id);

    }
    
    @DeleteMapping(value = "/softDeleteProducts")
    public Map<Object, Object> deleteProductBulk(@RequestBody ProductDeletePayload productIds) {
        return newProductService.deleteProductBulk(productIds);

    }

    // --------------------------------------------ACTIVATE-AND-DEACTIVATE---------------------------------------

    /**
     * @param request
     * @return
     * @SameerKhan
     */
    @PostMapping("/updateProductStatus")
    public Map<Object, Object> updateProductStatus(@RequestBody StatusRequest request) {
        return newProductService.updateProductStatus(request.getId(), request.getStatus());

    }
    // --------------------------------------------PRODUCT-SEARCH------------------------------------------------

    /**
     * @param request
     * @return
     * @SameerKhan
     */
    @PostMapping("/searchProducts")
    public Map<Object, Object> searchProducts(@RequestBody ProductSearchRequest request) {
        return newProductService.searchProducts(request);

    }


    @PostMapping(value = "/getAllProduct")
    public Map<String, Object> getAllProduct(@RequestBody GetDataRequest request) {
        return newProductService.getAllProduct(request);

    }

    /**
     * @SameerKhan
     * @param csvRequest
     * @return
     * @throws IOException
     */
    // --------------------------------------------CSV-Upload------------------------------------------------




    @PostMapping(value="/uploadCsvFileToDataBase",consumes =MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<Object, Object> uploadCsvFileToDataBase(@RequestParam("docfile") MultipartFile docfile)throws IOException {
        HashMap<Object, Object> map = new HashMap<>();
        if (docfile != null) {
            return newProductService.uploadCsvFileToDataBase(docfile);
        }
        map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
        map.put(DataConstant.RESPONSE_BODY, null);
        map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
        return map;
    }

    @PostMapping(value = "/getAllDeleteProduct")
    public Map<String, Object> getAllDeleteProduct(@RequestBody GetAllDeleteProduct request) {
        return newProductService.getAllDeleteProduct(request);

    }

}