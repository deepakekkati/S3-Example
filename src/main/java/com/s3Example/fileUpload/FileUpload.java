package com.s3Example.fileUpload;

import java.io.File;
import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Qualifier("fileUpload")
public class FileUpload {
	
	@Value("${s3.bucket.name}")
    private String bucketName;
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	    
	//private static String bucketName     = "deepakfiles";
	
	public String uploadFileToS3(AmazonS3 s3client, File file, String fileName){
		String status = null;
		try {
			LOGGER.debug("Uploading a new object to S3 from a file\n");
            s3client.putObject(new PutObjectRequest(
            		                 bucketName, fileName, file));
            LOGGER.debug("File uploaded successfully");
            status = "File uploaded successfully";
         } catch (AmazonServiceException ase) {
        	 status = "AmazonServiceException";
        	 LOGGER.error("Caught an AmazonServiceException, which " +
            		"means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
        	 LOGGER.error("Error Message:    " + ase.getMessage());
        	 LOGGER.error("HTTP Status Code: " + ase.getStatusCode());
        	 LOGGER.error("AWS Error Code:   " + ase.getErrorCode());
        	 LOGGER.error("Error Type:       " + ase.getErrorType());
        	 LOGGER.error("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
        	status="AmazonClientException";
        	LOGGER.error("Caught an AmazonClientException, which " +
            		"means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
        	LOGGER.error("Error Message: " + ace.getMessage());
        } 
		return status;
    }
	
}