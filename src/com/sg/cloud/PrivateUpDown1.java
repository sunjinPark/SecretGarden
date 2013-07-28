package com.sg.cloud;
import java.io.File;
import java.io.IOException;

import com.sg.model.Files;


interface PrivateUpDown1 {
	public boolean auth();
	public int upload() throws IOException;
	public Files download(Files request) throws IOException;
	public int deleteFile();
	public int upload(String fileName, String userId, File targetFile, String dirPath) throws IOException;
	public int download() throws IOException;

}
