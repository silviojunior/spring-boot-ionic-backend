package com.codewaiter.cursomc.services;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

	private Logger log = LoggerFactory.getLogger(S3Service.class);

	@Autowired
	private AmazonS3 s3Client;

	@Value("${s3.bucket}")
	private String s3Bucket;

	public void uploadFile(String localFilePath) {

		try {
			File file = new File(localFilePath);
			
			log.info("Iniciando Upload...");
				s3Client.putObject(new PutObjectRequest(s3Bucket, "teste.png", file));
			log.info("Upload finalizado!");
		} catch (AmazonServiceException e) {
			log.info("AmazonServiceException: " + e.getMessage());
			log.info("Status code: " + e.getErrorCode());
		}catch(AmazonClientException e) {
			log.info("AmazonClientException: " + e.getMessage());
		}
	}
}
