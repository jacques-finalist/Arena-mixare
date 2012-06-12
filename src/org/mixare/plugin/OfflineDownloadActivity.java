package org.mixare.plugin;

import java.io.IOException;

import org.mixare.plugin.util.OfflineConverter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OfflineDownloadActivity extends Activity{

	private static final String COLOR_DARK_GREEN = "#00BB00";
	private static final String COLOR_DARK_RED = "BB0000";
	private static final String OFFLINE_ACTIVITY_CLOSED = "offline-activity-closed";
	private static final int OFFLINE_ACTIVITY_REQUESTCODE = 1503; //unique random number
	private static final String URL_STRING = "url";
	private static final String TAG = "OfflineDownloadActivity";

	private String onlineUrl;
	private String offlineUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onlineUrl = getIntent().getStringExtra(URL_STRING);
		setContentView(R.layout.offlinedownloadactivity);
		buildGui();
	}
	
	private void buildGui(){
		setConnectionStatusGuiColor();	
		setDownloadFileGui();
		setContinueButtonGui();
	}
	
	private void setConnectionStatusGuiColor(){
		TextView connectionStatus = (TextView)findViewById(R.id.connectionStatus);
		if(isInternetConnected()){
			connectionStatus.setText(R.string.internet_status_connected);
			connectionStatus.setTextColor(Color.parseColor(COLOR_DARK_GREEN));
		}else{
			connectionStatus.setText(R.string.internet_status_disconnected);
			connectionStatus.setTextColor(Color.parseColor(COLOR_DARK_RED));
		}
	}
	
	private boolean isInternetConnected(){
		 ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		 if(cm != null && cm.getActiveNetworkInfo() != null){
			 return cm.getActiveNetworkInfo().isConnectedOrConnecting();
		 }else{
			 Log.w(TAG, "No Internet conncetion");
			 return false;
		 }
	}	
	
	private void setDownloadFileGui(){
		TextView textView = (TextView)findViewById(R.id.alreadyDownloaded);
		final OfflineConverter offlineConverter = new OfflineConverter(onlineUrl);
		if(!offlineConverter.fileExist()){
			textView.setVisibility(View.INVISIBLE);
		}else{
			textView.setVisibility(View.VISIBLE);
			offlineUrl = offlineConverter.getExistingFilePath();
		}
		
		final Button downloadButton = (Button)findViewById(R.id.downloadDataButton);
		downloadButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					downloadButton.setEnabled(false);
					offlineUrl = offlineConverter.convert();
					buildGui();
				} catch (IOException e) {
					Log.e(TAG, "Unable to convert the online url to an offline file");
				}
			}
		});
	}
	
	private void setContinueButtonGui(){
		Button continueButton = (Button)findViewById(R.id.continueButton);
		continueButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(OFFLINE_ACTIVITY_CLOSED, offlineUrl);
				setResult(OFFLINE_ACTIVITY_REQUESTCODE, intent);
				finish();
			}
		});
	}
	
}
