package org.mixare.plugin.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	public static final String FOLDER = "mixare-offline-ds";
	private static final String TAG = "OfflineConverter";

	public OfflineConverter(String url) {
		fileName = url;
	}

	public OfflineConverter() {
	}

	public String convert() throws IOException {
		if (isWritableSDCardMounted()) {
			clearDir();
			String result = write(fileName);
			if(result != null){
				return result;
			}else{
				return "";
			}
		} else {
			Log.e(TAG, "SD CARD not found");
			throw new IOException("SD Card not found");
		}
	}

	private boolean isWritableSDCardMounted() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}
	
	/**
	 * Changes all http:// urls to a file://
	 * Thus converting online data to offline data
	 * It converts the datasource url (to a file location) And all
	 * http urls inside that url to an file url
	 * @return
	 * @throws IOException
	 */
	public String write(String fname) throws IOException{
		String path = Environment.getExternalStorageDirectory() + "/" + FOLDER + "/" + URLEncoder.encode(fname);
		FileOutputStream fos = new FileOutputStream(new File(path));
		WebReader webReader = new WebReader(fname);		
		if(webReader.getByteResult() != null){
			String result = convertUrlsInFoundString(webReader.getResult()); //actually recursive
			if(!result.equals(webReader.getResult())){ //if anything changed from the string, write that string to the file
				fos.write(result.getBytes());
			}else{									  //usually binary files, that did not change
				fos.write(webReader.getByteResult()); 
			}
		}else{
			Log.e(TAG, "Unable to download file: "+ fname);
		}
		fos.close();
		return "file://"+path;
	}
	
	private String convertUrlsInFoundString(String found){
		Pattern pattern = Pattern.compile("(\\\")(.*?)(\\\")");
		Matcher matcher = pattern.matcher(found);
		while(matcher.find()){
			String match = matcher.group().replace("\"","");
			if(match.startsWith("http://")){
				try {
					String newUrl = write(match);
					found = found.replace(match, newUrl);
				} catch (IOException e) {
					Log.e(TAG, 
							"io exception occured, when converting: "+ match);
				}
			}
		}
		return found;
	}
	
	public boolean fileExist(){
		if(fileName.startsWith("file://")){
			File file = new File(fileName.replace("file://", ""));
			return file.exists();
		}else{
			String path = Environment.getExternalStorageDirectory() + "/" + FOLDER + "/" + URLEncoder.encode(fileName);
			File file = new File(path);
			return file.exists();
		}
	}
	
	public String getExistingFilePath(){
		if(fileExist()){
			String path = Environment.getExternalStorageDirectory() + "/" + FOLDER + "/" + URLEncoder.encode(fileName);
			return "file://"+ path;
		}
		return null;
	}
	
	private void clearDir(){
		String path = Environment.getExternalStorageDirectory() + "/" + FOLDER + "/";
		File directory = new File(path);
		if(directory.list() != null){
			for(String fname : directory.list()){
				File file = new File(directory, fname);
				file.delete();
			}
		}else{
			directory.mkdirs();
		}
	}
}
