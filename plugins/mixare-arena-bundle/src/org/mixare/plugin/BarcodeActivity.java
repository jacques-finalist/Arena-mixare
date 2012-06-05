package org.mixare.plugin;

import java.io.IOException;

import org.mixare.plugin.intent.IntentIntegrator;
import org.mixare.plugin.intent.IntentResult;
import org.mixare.plugin.offline.OfflineConverter;
import org.mixare.plugin.service.BarcodeService;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class BarcodeActivity extends Activity {

	public final String resultType = "Datasource";

	private final String sharedPrefDataSource = "barcodeDs";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.barcodemenu);
		addClickHandlers();
	}

	private void addClickHandlers() {
		Button scanButton = (Button) findViewById(R.id.scanButton);
		scanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scan();
			}
		});

		Button usePrevious = (Button) findViewById(R.id.usePrevious);
		final String sharedPrefsValue = getSharedPreferences(
				sharedPrefDataSource, MODE_PRIVATE).getString("url", null);
		if (sharedPrefsValue != null) {
			usePrevious.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//tries to change this to an offline string, else returns
					//online string
					changeToOfflineString(sharedPrefsValue);					
				}
			});
		} else {
			usePrevious.setClickable(false);
		}
	}
	
	public void closeScanner(String u){
		String[] url = new String[1];
		url[0] = u;
		Intent intent = new Intent();
		intent.putExtra("resultType", resultType);
		intent.putExtra("url", url);
		setResult(BarcodeService.ACTIVITY_REQUEST_CODE, intent);
		finish();
	}

	/**
	 * If the offline modus has been activated, then convert this datasource to
	 * an offline datasource before sending it to the core.
	 * 
	 * @return an call to close this activity and returns the offline string
	 * if the offline modus has been activated, else, just return the original string
	 */
	public void changeToOfflineString(final String url){
		ToggleButton offlineToggle = (ToggleButton)findViewById(R.id.offlineToggle);
		if(offlineToggle.isChecked()){ //online modus
			closeScanner(url);
		}else{
			OfflineConverter offlineConverter = new OfflineConverter(url);
			try {
				closeScanner(offlineConverter.convert());
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("barcode", "Unable to convert the online url to an offline file");;
			}
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
		if (scanResult != null) {

			if ((scanResult.getContents() == null || scanResult.getContents().equals("null")) && data != null) { 
				// if no url is found: scan again
				Toast.makeText(this, "No url found, scan again!",
						Toast.LENGTH_LONG).show();
				scan();
				return;
			} else { // url found, return it as result.

				SharedPreferences sharedPreferences = getSharedPreferences(
						sharedPrefDataSource, MODE_PRIVATE);
				sharedPreferences.edit()
						.putString("url", scanResult.getContents()).commit();
				changeToOfflineString(scanResult.getContents());
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
		
}
