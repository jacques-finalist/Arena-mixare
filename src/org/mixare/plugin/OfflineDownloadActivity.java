package org.mixare.plugin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.TextView;

public class OfflineDownloadActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offlinedownloadactivity);
		buildGui();
	}
	
	private void buildGui(){
		setConnectionStatusGuiColor();
		
	}
	
	private void setConnectionStatusGuiColor(){
		TextView connectionStatus = (TextView)findViewById(R.id.connectionStatus);
		if(isInternetConnected()){
			connectionStatus.setText(R.string.internet_status_connected);
			connectionStatus.setTextColor(Color.parseColor("#0B0")); //darkgreen 
		}else{
			connectionStatus.setText(R.string.internet_status_disconnected);
			connectionStatus.setTextColor(Color.parseColor("#B00")); //darkred
		}
	}
	
	private boolean isInternetConnected(){
		 ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		 if(cm != null && cm.getActiveNetworkInfo() != null){
			 return cm.getActiveNetworkInfo().isConnectedOrConnecting();
		 }else{
			 return false;
		 }
	}
	
	
}
