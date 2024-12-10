package com.janaushadhi.adminservice.controller;
import com.janaushadhi.adminservice.service.FileUploadService;
import com.janaushadhi.adminservice.util.DataConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/file")
//@CrossOrigin(origins = "*", allowedHeaders = "*")


public class FileUploadController {

	@Autowired
	FileUploadService fileUploadService;


//	@PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public ResponseEntity<List<String>> uploadFiles(@RequestParam(value = "files", required = false) List<MultipartFile> files) {
//		List<String> uploadedFileUrls = fileUploadService.uploadFiles(files);
//		return ResponseEntity.ok(uploadedFileUrls);
//	}

	@PostMapping(value = "/fileupload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Map<Object, Object> addFile(@RequestParam(value = "docfile", required = false) List<MultipartFile> docfile) {
		Map<Object, Object> map = new HashMap<>();
		if (docfile != null) {
			return fileUploadService.addFiles(docfile);
		}
		map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
		map.put(DataConstant.MESSAGE, DataConstant.BAD_REQUEST_MESSAGE); // Corrected this line
		map.put(DataConstant.DATA, null);
		return map;
	}
}
