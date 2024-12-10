package com.janaushadhi.adminservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FileUploadService {

	public Map<Object,Object> addFiles(List<MultipartFile> files);
}
