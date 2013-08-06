package com.sg.cloud;

import java.io.File;
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.sg.model.Files;


interface PublicUpDown {
	
	public boolean auth();
	public boolean auth(String userId);
	public int upload() throws IOException;
	public Files download(Files request);
	public int deleteFile();
	public int upload(Files fileDescript, File targetFile) throws IOException;

}
