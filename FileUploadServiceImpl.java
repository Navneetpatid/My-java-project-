package com.janaushadhi.adminservice.serviceimpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.janaushadhi.adminservice.service.FileUploadService;
import com.janaushadhi.adminservice.util.DataConstant;
import com.janaushadhi.adminservice.util.FileUploader;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

	@Value("${spring.url}")
	public String url;

	@Value("${spring.dir}")
	public String uploadDir;

	public Map<Object, Object> addFiles(List<MultipartFile> docfile) {
		Map<Object, Object> map = new HashMap<>();
		List<String> fileListData=new ArrayList<>();
		try {
			if (docfile != null && !docfile.isEmpty()) {
				// Save new file
				List<String> filename = FileUploader.uploadProfileImage(docfile, url);
				if (!filename.isEmpty()) {
					for(String file:filename ) {
						String fullPath = uploadDir + file; // Concatenate directory path with file name
						fileListData.add(fullPath);
						log.info("File upload success =" + fullPath);
						//map.put(DataConstant.DATA, fullPath); // Include full path in response
					}
					map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
					map.put(DataConstant.MESSAGE, DataConstant.RECORD_ADDED_SUCCESSFULLY);
					map.put(DataConstant.DATA, fileListData);
					log.info("File upload success =" ,DataConstant.RECORD_ADDED_SUCCESSFULLY);
					return map;
				} else {
					// Log error when file upload fails
					log.error("File upload failed");
					map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
					map.put(DataConstant.MESSAGE, DataConstant.FILE_UPLOAD_FAILD);
					map.put(DataConstant.DATA, null);
					return map;
				}
			}
			map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
			map.put(DataConstant.MESSAGE, DataConstant.BAD_REQUEST_MESSAGE);
			map.put(DataConstant.DATA, null);
		} catch (DataAccessResourceFailureException e) {
			log.error("exception in fileupload",e.getMessage()); ;
			map.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
			map.put(DataConstant.MESSAGE, DataConstant.NO_SERVER_CONNECTION);
		} catch (Exception e) {
			log.error("Exception occurred  in fileUpload: {}", e.getMessage());
			map.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
			map.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
		}
		return map;
	}
}
