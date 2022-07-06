package com.bethsaida.org.models;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.io.IOException;

public class FileUploadUtil {

	 public static void saveFile(String uploadDir, String fileName,
			 MultipartFile multipartFile) throws IOException, java.io.IOException {
	        Path uploadPath = Paths.get(uploadDir);
	         
	        if (!Files.exists(uploadPath)) {
	            Files.createDirectories(uploadPath);
	        }
	         
	     try (InputStream inputStream = multipartFile.getInputStream()) { 
	    	
	    	Path filePath = uploadPath.resolve(fileName); 
	    	System.out.println(filePath.toFile().getAbsolutePath());
	    	Files.copy(inputStream, filePath,StandardCopyOption.REPLACE_EXISTING); 
	    	} catch (IOException ioe) { 
	    		throw new IOException("Could not save image files: " + fileName, ioe); 
	        }
		      
	    }

	

	
	
}
