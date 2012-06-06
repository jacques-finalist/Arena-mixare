package org.mixare.plugin.offline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import org.mixare.plugin.util.WebReader;

import android.os.Environment;
import android.util.Log;

/**
 * Converts an url+content to an offline file
 * 
 * @author A. Egal
 * 
 */
public class OfflineConverter {

	private String fileName;
	private final String folder = "mixare-offline-ds";

	public OfflineConverter(String url) {
		fileName = url;
	}

	public OfflineConverter() {
	}

	public String convert() throws IOException {
		if (isWritableSDCardMounted()) {
			String result = write();
			if(result != null){
				return result;
			}else{
				return "";
			}
		} else {
			throw new IOException("SD Card not found");
		}
	}

	private boolean isWritableSDCardMounted() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}
	
	public String write() throws IOException{
		String path = Environment.getExternalStorageDirectory() + "/" + folder + "/" + URLEncoder.encode(fileName);
		File file = new File(path);
		if(!file.exists()){
			clearDir();
			FileOutputStream fos = new FileOutputStream(file);
			WebReader webReader = new WebReader(fileName);
			fos.write(webReader.getResult().getBytes());
			fos.close();
		}
		Log.w("offlineFileConverter", "File already exists");
		return "file://"+path;
	}
	
	private void clearDir(){
		String path = Environment.getExternalStorageDirectory() + "/" + folder + "/";
		File directory = new File(path);
		if(directory.list() != null){
			for(String fileName : directory.list()){
				File file = new File(directory, fileName);
				file.delete();
			}
		}else{
			directory.mkdirs();
		}
	}
}
