package com.kusitms.backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface IS3Service {

  String upload(MultipartFile multipartFile);

  void delete(String fileName);
}
