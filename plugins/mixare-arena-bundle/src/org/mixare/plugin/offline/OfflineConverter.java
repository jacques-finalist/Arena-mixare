package org.mixare.plugin.offline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

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
			return write();
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
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(readWebPage(fileName).getBytes());
			fos.close();
		}
		Log.w("offlineFileConverter", "File already exists");
		return "file://"+path;
	}

	private String readWebPage(String url) throws IOException {
		URL oracle = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				oracle.openStream()));

		String result = "";
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			result += inputLine;
		in.close();
		return result;
	}

}
