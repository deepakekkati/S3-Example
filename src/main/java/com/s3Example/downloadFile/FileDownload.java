package com.s3Example.downloadFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;

@Service
@Qualifier("fileDownload")
public class FileDownload {

	@Value("${s3.bucket.name}")
    private String bucketName;
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	public String downloadFileFromS3(AmazonS3 s3client, HttpServletResponse response, String fileName) throws IOException{
		String status = null;
		
		try( S3Object object = s3client.getObject(new GetObjectRequest(bucketName, fileName));
                InputStream objectData = object.getObjectContent();
                    ServletOutputStream responseOutputStream = response.getOutputStream()) {
            response.setContentLength((int) object.getObjectMetadata().getContentLength());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName+ "\";");
            FileCopyUtils.copy(objectData, responseOutputStream);
            LOGGER.debug("File downloading");
            return status;
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