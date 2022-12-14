package com.kusitms.backend.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.kusitms.backend.exception.ApiException;
import com.kusitms.backend.exception.ApiExceptionEnum;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service implements IS3Service {

  private final AmazonS3Client amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public String upload(MultipartFile multipartFile) {
    String fileName = createFileName(multipartFile.getOriginalFilename());
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(multipartFile.getSize());
    objectMetadata.setContentType(multipartFile.getContentType());

    try (InputStream inputStream = multipartFile.getInputStream()) {
      amazonS3Client.putObject(
          new PutObjectRequest(bucket, fileName, inputStream, objectMetadata).withCannedAcl(
              CannedAccessControlList.PublicRead
          )
      );
    } catch (IOException e) {
      throw new ApiException(ApiExceptionEnum.BAD_REQUEST_EXCEPTION);
    }
    return amazonS3Client.getUrl(bucket, fileName).toString();
  }

  @Override
  public void delete(String fileName) {
    String imageUrl = fileName.split("/")[3];

    if (!amazonS3Client.doesObjectExist(bucket, imageUrl)) {
      throw new ApiException(ApiExceptionEnum.NOT_FOUND_EXCEPTION);
    }
    amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, imageUrl));
  }

  private String createFileName(String fileName) {
    return UUID.randomUUID().toString().concat(getFileExtension(fileName));
  }

  private String getFileExtension(String fileName) {
    try {
      return fileName.substring(fileName.lastIndexOf("."));
    } catch (StringIndexOutOfBoundsException e) {
      throw new ApiException(ApiExceptionEnum.BAD_REQUEST_EXCEPTION);
    }
  }
}
