package com.codewaiter.cursomc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {
	
	@Value("${aws.access_key_id}")
	private String awsAccessKeyId;
			
	@Value("${aws.secret_access_key}")
	private String awsSecretAccessKey;

	@Value("${s3.region}")
	private String s3Region;
	
	@Bean
	public AmazonS3 s3Client() {
		BasicAWSCredentials awsCred = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(s3Region))
							.withCredentials(new AWSStaticCredentialsProvider(awsCred)).build();
		return s3Client;
	}
}
