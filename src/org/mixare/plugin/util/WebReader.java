package org.mixare.plugin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

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
	private boolean pageFound = false;
	
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
	
	private String readWebPageToString() throws IOException {
		URL u = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				u.openStream()));

		String webresult = "";
		String inputLine;
		while ((inputLine = in.readLine()) != null){
			webresult += inputLine;
		}
		in.close();
		return webresult;
	}
	
	public String getResult() {
		return result;
	}
	
	class readWebPage extends AsyncTask<WebReader, String, WebReader> {
		
		@Override
		protected WebReader doInBackground(WebReader... params) {
			try {
				params[0].result = params[0].readWebPageToString();
				params[0].pageFound = true;
				return params[0];
			} catch (IOException e) {
				Log.e("barcode", "Unable to read the webpage");
				return params[0];
			}
		}
				
	}
	
}