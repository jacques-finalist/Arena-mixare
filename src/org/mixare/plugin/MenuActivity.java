package org.mixare.plugin;

import org.mixare.plugin.intent.IntentIntegrator;
import org.mixare.plugin.intent.IntentResult;
import org.mixare.plugin.service.MenuService;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MenuActivity extends Activity {

	private static final String RESULT_TYPE = "Datasource";
	private static final String SHARED_PREF_DATASOURCE = "barcodeDs";
	private static final String OFFLINE_ACTIVITY_CLOSED = "offline-activity-closed";
	private static final int OFFLINE_ACTIVITY_REQUESTCODE = 1503; //unique random number
	private static final String URL_STRING = "url";
	private static final String RESULT_TYPE_STRING = "resultType";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		buildWindowSettings();
		setContentView(R.layout.barcodemenu);
		addClickHandlerForScanButton();
		addClickHandlerForUsePreviousButton();
	}
	
	private void buildWindowSettings(){
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	private void addClickHandlerForScanButton() {
		Button scanButton = (Button) findViewById(R.id.scanButton);
		scanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scan();
			}
		});
	}
	
	private void addClickHandlerForUsePreviousButton(){
		Button usePrevious = (Button) findViewById(R.id.usePrevious);
		final String sharedPrefsValue = getSharedPreferences(
				SHARED_PREF_DATASOURCE, MODE_PRIVATE).getString("url", null);
		if (sharedPrefsValue != null) {
			usePrevious.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					closeAndContinueToNextActivity(sharedPrefsValue);					
				}
			});
		} else {
			usePrevious.setEnabled(false);
		}
	}
	
	/**
	 * Closes this activity, and returns to the application. This method also 
	 * adds an result to the closed activity. That result will be the url that the core
	 * will use as its datasource.
	 * @param u
	 */
	public void closeActivity(String u){
		String[] url = new String[1];
		url[0] = u;
		Intent intent = new Intent();
		intent.putExtra(RESULT_TYPE_STRING, RESULT_TYPE);
		intent.putExtra(URL_STRING, url);
		setResult(MenuService.ACTIVITY_REQUEST_CODE, intent);
		finish();
	}
	
	/**
	 * Opens the OfflineDownloadActivity to give the user the option to download the offline data
	 */
	public void openOfflineDownloadActivity(String url){
		Intent intent = new Intent(this, OfflineDownloadActivity.class);
		intent.putExtra(URL_STRING, url);
		startActivityForResult(intent, OFFLINE_ACTIVITY_REQUESTCODE);
		/*OfflineConverter offlineConverter = new OfflineConverter(url);
		try {
			closeActivity(offlineConverter.convert());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("barcode", "Unable to convert the online url to an offline file");;
		}*/
	}

	/**
	 * This method closes this activity, and goes to the next activity.
	 * If the online mode is activated, then this activity will return to the mixare code.
	 * If the offline mode is activated, then this activity will go to the OfflineDownloadActivity.
	 */
	public void closeAndContinueToNextActivity(final String url){
		ToggleButton offlineToggle = (ToggleButton)findViewById(R.id.offlineToggle);
		if(offlineToggle.isChecked()){ //online modus
			closeActivity(url);
		}else{
			openOfflineDownloadActivity(url);
		}
	}

	private void scan() {
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, data);
		if (scanResult != null) { //barcode result received
			processBarcodeScannerResults(scanResult, data);
		}
		if (data != null && data.getStringExtra(OFFLINE_ACTIVITY_CLOSED) != null){
			//Received result from OfflineDownloadActivity:
			closeAndContinueToNextActivity(data.getStringExtra(OFFLINE_ACTIVITY_CLOSED));			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * Processes the url that the barcode scanner returned.
	 */
	private void processBarcodeScannerResults(IntentResult scanResult, Intent data){
		if ((scanResult.getContents() == null || scanResult.getContents().equals("null")) && data != null) { 
			// if no url is found: scan again
			Toast.makeText(this, "No url found, scan again!", Toast.LENGTH_LONG).show();
			scan();
		} else { // url found, return it as result.
			SharedPreferences sharedPreferences = getSharedPreferences(
					SHARED_PREF_DATASOURCE, MODE_PRIVATE);
			sharedPreferences.edit()
					.putString(URL_STRING, scanResult.getContents()).commit();
			closeAndContinueToNextActivity(scanResult.getContents());
		}
	}
		
}
