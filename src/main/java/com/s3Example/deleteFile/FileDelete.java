package com.s3Example.deleteFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

@Service
@Qualifier("fileDelete")
public class FileDelete {

	@Value("${s3.bucket.name}")
    private String bucketName;
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	public String deleteFileFromS3(AmazonS3 s3client, String fileName){
		String status = null;
		try {
			if(s3client.doesObjectExist(bucketName, fileName)){
				 s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
				 status ="file deleted";
			}else {
				return "File doesn't exist";
			}
			
			
	        }catch (AmazonServiceException ase) {
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
