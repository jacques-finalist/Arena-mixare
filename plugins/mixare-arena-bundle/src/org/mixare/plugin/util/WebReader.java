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
	
	public WebReader(String url){
		this.url = url;
		new readWebPage().execute(this);
		long now = System.currentTimeMillis();
		while(pageFound == false && System.currentTimeMillis() > now + 5000){
			
		}
	}
	
	private String readWebPageToString() throws IOException {
		URL u = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				u.openStream()));

		String result = "";
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			result += inputLine;
		in.close();
		return result;
	}
	
	public String getResult() {
		return result;
	}
	
	class readWebPage extends AsyncTask<WebReader, String, WebReader> {
		
		@Override
		protected WebReader doInBackground(WebReader... params) {
			try {
				String result = params[0].readWebPageToString();
				params[0].result = result;
				params[0].pageFound = true;
				return params[0];
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("barcode", "Unable to read the webpage");
				return params[0];
			}
		}
				
	}
	
}
