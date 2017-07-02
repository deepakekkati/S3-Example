package com.s3Example.fileConvert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Qualifier("convertMultiPartFileToFile")
public class ConvertMultiPartFileToFile {

	public File convertMultipartToFile(MultipartFile file) throws IllegalStateException, IOException 
	{
		 File convFile = new File(file.getOriginalFilename());
		    convFile.createNewFile(); 
		    FileOutputStream fos = new FileOutputStream(convFile); 
		    fos.write(file.getBytes());
		    fos.close(); 
		    return convFile;
	}
	
}
