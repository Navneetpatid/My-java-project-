package com.janaushadhi.adminservice.util;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
@Slf4j
public class FileUploader {

	public static List<String> uploadProfileImage(List<MultipartFile> multipartFiles, String path) {
		List<String> fileList = new ArrayList<>();
		for (MultipartFile multipartFile : multipartFiles) {
			if (!multipartFile.isEmpty()) {
				try {
					Long timestamp = System.currentTimeMillis();
					String filename = timestamp + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
					String filePath = path + filename;

					try (BufferedOutputStream buffStream = new BufferedOutputStream(
							new FileOutputStream(filePath, true))) {
						byte[] bytes = multipartFile.getBytes();
						buffStream.write(bytes);
						fileList.add(filename);
					}
				catch (Exception e) {
						log.error("Error writing file {}: {}", filename, e.getMessage(), e);
					}
				} catch (Exception e) {
					log.error("Error processing file {}: {}", multipartFile.getOriginalFilename(), e.getMessage(), e);
				}
			}
		}
		return fileList;
	}

}
