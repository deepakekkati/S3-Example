package com.s3Example.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.s3Example.deleteFile.FileDelete;
import com.s3Example.downloadFile.FileDownload;
import com.s3Example.fileConvert.ConvertMultiPartFileToFile;
import com.s3Example.fileUpload.FileUpload;
import lombok.Setter;

@RestController
@ComponentScan("com.s3Example")
@RequestMapping("/api/v1")
public class Controller {
	
	@Autowired
    @Setter
    private FileUpload fileUpload;
	
	@Autowired
    @Setter
    private FileDownload fileDownload;
	
	@Autowired
    @Setter
    private FileDelete fileDelete;
	
	@Autowired
    @Setter
    private ConvertMultiPartFileToFile convertMultiPartFileToFile;
	
	AWSCredentials credentials = new BasicAWSCredentials("AccessKey","SecretKey");
	AmazonS3 s3client = new AmazonS3Client(credentials);
	
	public String index() {
	    return "s3 example by Deepak";
	  }
	
	
	@RequestMapping(value="/uploadFile", method=RequestMethod.POST)
    public @ResponseBody String uploadFile( 
            @RequestParam("file") MultipartFile file) throws IllegalStateException, IOException{
		String fileName = nameSplit(file.getOriginalFilename());
		if(!file.isEmpty()){
			File tempFile = convertMultiPartFileToFile.convertMultipartToFile(file);
			return fileUpload.uploadFileToS3(s3client, tempFile, fileName);
		} else {
			return "please upload a file";
		}
		
	}
	public String nameSplit(String fileName){
		if(fileName.indexOf('.')>0){
			String name = fileName.substring(0, fileName.indexOf('.') );
			return name;
		}else {
			return fileName;
		}
		
	}
	
	@RequestMapping(value = "/downloadFile/{fileName}", method = RequestMethod.GET)
    public String downloadFile(HttpServletResponse response, @PathVariable String fileName) throws IOException {
    	return fileDownload.downloadFileFromS3(s3client,response,fileName);
    }
	
	@RequestMapping(value = "/deleteFile/{fileName}", method = RequestMethod.GET)
    public String deleteFile(@PathVariable String fileName ) throws IOException {
    	return fileDelete.deleteFileFromS3(s3client, fileName);
    }
	
}

