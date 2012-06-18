package org.mixare.plugin;

import org.mixare.plugin.R;
import org.mixare.plugin.service.ArenaSplashService;
import org.mixare.plugin.service.MenuService;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class ArenaSplashActivity extends Activity {

	private static final String RESULT_TYPE = "Splashscreen";
	private static final String CLOSE_ACTIVITY_CALL = "closed";
	private static final int SPLASHTIME = 2000; // 2 seconds

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splashscreen);
		// Runnable exiting the splash screen and launching the menu
		Handler exitHandler = null;
		;
		Runnable exitRunnable = null;
		exitRunnable = new Runnable() {
			public void run() {
				exitSplash();
			}
		};
		// Run the exitRunnable in in _splashTime ms
		exitHandler = new Handler();
		exitHandler.postDelayed(exitRunnable, SPLASHTIME);
	}

	private void exitSplash() {
		Intent intent = new Intent();
		intent.putExtra("resultType", RESULT_TYPE);
		setResult(ArenaSplashService.ACTIVITY_REQUEST_CODE, intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.putExtra(CLOSE_ACTIVITY_CALL, "true");
			setResult(MenuService.ACTIVITY_REQUEST_CODE, intent);
			finish();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
}
