package io.realmarket.propeler.service.impl;

import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.SdkClientException;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder;
import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.ObjectMetadata;
import com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectId;
import io.realmarket.propeler.api.dto.FileDto;
import io.realmarket.propeler.service.CloudObjectStorageService;
import io.realmarket.propeler.service.exception.COSException;
import io.realmarket.propeler.service.exception.util.ExceptionMessages;
import io.realmarket.propeler.service.util.FileUtils;
import io.realmarket.propeler.service.util.RandomStringBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service
@Slf4j
public class CloudObjectStorageServiceImpl implements CloudObjectStorageService {

  private static final Integer SIZE_OF_PUBLIC_NAME = 32;

  @Value(value = "${cos.api.key.id}")
  private String cosApiKeyId;

  @Value(value = "${cos.service.crn}")
  private String cosServiceCrn;

  @Value(value = "${cos.endpoint}")
  private String cosEndpoint;

  @Value(value = "${cos.auth.endpoint}")
  private String cosAuthEndpoint;

  @Value(value = "${cos.bucket.location}")
  private String cosBucketLocation;

  @Value(value = "${cos.bucket.name}")
  private String cosBucketName;

  @Value(value = "${cos.public_prefix}")
  private String cosPublicPrefix;

  /**
   * @param apiKey api-key used for authentication on Cloud Object Storage (COS)
   * @param serviceInstanceId Service Cloud Resource Name (CRN)
   * @param endpointUrl COS endpoint url
   * @param location COS bucket location
   * @return AmazonS3 cloud client
   */
  private AmazonS3 createCloudClient(
      String apiKey, String serviceInstanceId, String endpointUrl, String location) {

    ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
    clientConfig.setUseTcpKeepAlive(true);

    return AmazonS3ClientBuilder.standard()
        .withCredentials(
            new AWSStaticCredentialsProvider(
                new BasicIBMOAuthCredentials(apiKey, serviceInstanceId)))
        .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration(endpointUrl, location))
        .withPathStyleAccessEnabled(true)
        .withClientConfiguration(clientConfig)
        .build();
  }

  @Override
  public byte[] download(String fileName) {
    log.info("Download file:{}", fileName);
    try {
      AmazonS3 cloudClient =
          createCloudClient(cosApiKeyId, cosServiceCrn, cosEndpoint, cosBucketLocation);

      return org.apache.commons.io.IOUtils.toByteArray(
          cloudClient
              .getObject(new GetObjectRequest(new S3ObjectId(cosBucketName, fileName)))
              .getObjectContent());

    } catch (SdkClientException sdke) {
      if (sdke.getMessage().contains("Status Code: 404")) {
        log.debug("File with name '{}' does not exist.", fileName);
        throw new EntityNotFoundException("File with the provided name does not exist.");
      } else {
        log.error("SDK download Error: {}", sdke.getMessage());
        throw new COSException("Could not retrieve file.");
      }
    } catch (IOException e) {
      log.error("IOException: {}", e.getMessage());
      throw new COSException("Could not retrieve file.");
    }
  }

  @Override
  public boolean doesFileExist(String fileName) {
    AmazonS3 cloudClient =
        createCloudClient(cosApiKeyId, cosServiceCrn, cosEndpoint, cosBucketLocation);
    return cloudClient.doesObjectExist(cosBucketName, fileName);
  }

  @Override
  public FileDto downloadFileDto(String fileName) {
    if (StringUtils.isEmpty(fileName)) {
      throw new EntityNotFoundException(ExceptionMessages.IMAGE_DOES_NOT_EXIST);
    }
    return new FileDto(
        FilenameUtils.getExtension(fileName),
        Base64.getEncoder().encodeToString(download(fileName)));
  }

  @Override
  public byte[] downloadPublic(String fileName) {
    if (StringUtils.isEmpty(fileName)) {
      throw new EntityNotFoundException(ExceptionMessages.IMAGE_DOES_NOT_EXIST);
    }
    return download(String.join("", cosPublicPrefix, fileName));
  }

  @Override
  public String uploadPublic(MultipartFile file) {
    String name =
        String.join(
            "",
            RandomStringBuilder.generateBase32String(SIZE_OF_PUBLIC_NAME),
            ".",
            FileUtils.getExtensionOrThrowException(file));
    upload(String.join("", cosPublicPrefix, name), file);
    return name;
  }

  public void upload(String name, MultipartFile file) {
    InputStream inputStream;
    try {
      inputStream = file.getInputStream();
    } catch (IOException exception) {
      throw new COSException("Could not extract file.");
    }
    upload(name, inputStream, Math.toIntExact(file.getSize()));
  }

  @Override
  @Transactional
  public void uploadAndReplace(String oldName, String name, MultipartFile file) {
    if (oldName != null && !oldName.equals(name)) {
      try {
        delete(oldName);
      } catch (EntityNotFoundException enfe) {
        log.warn("Image not exists. Skipping delete.");
      }
    }
    upload(name, file);
  }

  @Override
  @Async
  public void upload(String name, InputStream inputStream, int fileSize) {
    log.info("Uploading file[{}]:{}", fileSize, name);
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(fileSize);

    AmazonS3 cloudClient =
        createCloudClient(cosApiKeyId, cosServiceCrn, cosEndpoint, cosBucketLocation);

    PutObjectRequest req = new PutObjectRequest(cosBucketName, name, inputStream, metadata);

    try {
      cloudClient.putObject(req);
      log.info("Stored object on cloud: {}", name);

    } catch (SdkClientException sdke) {
      log.error("SDK upload Error: {}", sdke.getMessage());
      throw new COSException("Could not save file.");
    }
  }

  @Override
  public void delete(String fileName) {
    if (StringUtils.isEmpty(fileName)) {
      return;
    }
    log.info("Delete file:{}", fileName);
    AmazonS3 cloudClient =
        createCloudClient(cosApiKeyId, cosServiceCrn, cosEndpoint, cosBucketLocation);
    try {
      if (cloudClient.doesObjectExist(cosBucketName, fileName)) {
        cloudClient.deleteObject(cosBucketName, fileName);
      } else {
        log.debug("File with name '{}' does not exist.", fileName);
        throw new EntityNotFoundException("File with the provided name does not exist.");
      }
    } catch (SdkClientException sdke) {
      log.error("SDK Error: {}", sdke.getMessage());
      throw new COSException("Could not delete file.");
    }
  }
}
