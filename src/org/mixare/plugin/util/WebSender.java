package org.mixare.plugin.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.mixare.plugin.notification.NotificationMgrImpl;

import android.os.AsyncTask;
import android.util.Log;

public class WebSender {

	private final static String TAG = "WebSender";
	private final static String ANSWER = "answer";
	
	private final String submitUrl;
	private final HashMap<String, String> postParams;
	
	public WebSender(OfflineAnswerStorage offlineAnswerStorage){
		submitUrl = offlineAnswerStorage.getSubmitUrl();
		postParams = new HashMap<String, String>();
		postParams.put(ANSWER, offlineAnswerStorage.getAnswer());
		new sendWebPage().execute();
	}
	class sendWebPage extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost(submitUrl);
		    
		    try {
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		        Iterator<Entry<String, String>> it = postParams.entrySet().iterator();
		        while(it.hasNext()){
		        	Entry<String, String> entry = it.next();
	 	        	nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		        }
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        HttpResponse httpResponse = httpclient.execute(httppost);
		        httpResponse.getEntity().getContent();
		    } catch (ClientProtocolException e) {
		    	Log.e(TAG, "ClientProtocolException occurred when sending answer to: "+ submitUrl, e);
		    } catch (IOException e) {
		    	Log.e(TAG, "IO exception occurred when sending answer to: "+ submitUrl, e);
		    }
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
			NotificationMgrImpl.getInstance().clear();
		}
		
	}
}
