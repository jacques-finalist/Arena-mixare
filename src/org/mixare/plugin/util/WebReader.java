package org.mixare.plugin.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Responsible for reading webpages, and returning the string of that page
 * @author A. Egal
 *
 */
public class WebReader {

	private String url;
	private String result;
	private byte[] byteResult;
	private boolean pageFound = false;
	private String mimeType;
	
	private static final int TEN_SECONDS = 10 * 1000;
	private static final int ONE_SECOND = 1 * 1000;
	
	
	public WebReader(String url){
		this.url = url;
		new readWebPage().execute(this);
		long now = System.currentTimeMillis();
		while(!pageFound && System.currentTimeMillis() < now + TEN_SECONDS){
			try {
				Thread.sleep(ONE_SECOND);
			} catch (InterruptedException e) {}
		}
	}
	
	public void readWebPageToString() throws IOException {
		URL u = new URL(url);
		InputStream in = new BufferedInputStream(u.openStream());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		byte[] buf = new byte[1024];
		int n = 0;
		while (-1!=(n= in.read(buf))){
		   out.write(buf, 0, n);
		}
		
		out.close();
		in.close();
		byteResult = out.toByteArray();
		result = new String(byteResult);
	}
	
	public String getResult() {
		return result;
	}
	
	public byte[] getByteResult() {
		return byteResult;
	}
	
	public String getMimeType() {
		return mimeType;
	}
	
	class readWebPage extends AsyncTask<WebReader, String, WebReader> {
		
		@Override
		protected WebReader doInBackground(WebReader... params) {
			try {
				params[0].readWebPageToString();
				params[0].pageFound = true;
				return params[0];
			} catch (IOException e) {
				Log.e("barcode", "Unable to read the webpage"+ e.getMessage());
				return params[0];
			}
		}
				
	}
	
}