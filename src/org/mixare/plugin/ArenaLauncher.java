package org.mixare.plugin;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ArenaLauncher extends Activity {

	private final static String TAG = "ArenaLauncher";
	// is set when mixare is installed
	private boolean isMixareInstalled = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.w(TAG, "ArenaLauncher started");

		// We try to locate mixare on the phone
		isMixareInstalled = false;
		try {
			PackageInfo pi = getPackageManager()
					.getPackageInfo("org.mixare", 0);

			if (pi.versionCode >= 1) {
				isMixareInstalled = true;
			}
		} catch (PackageManager.NameNotFoundException ex) {
			Log.e(TAG, "package: org.mixare not found in the market.");
		}

	}

	@Override
	public void onResume() {
		Log.w(TAG, "ArenaLauncher resumed");
		super.onResume();

		// Now we should have the position, let's proceed only if mixare is
		// installed
		if (isMixareInstalled) {
			// start mixare
			Intent i = new Intent("android.intent.category.LAUNCHER");
			i.setClassName("org.mixare", "org.mixare.PluginLoaderActivity");
			startActivity(i);

		} else {
			// Mixare is not installed, let's go to the market!
			Toast.makeText(this,
					"Mixare moet geinstalleerd zijn om de applicatie te kunnen gebruiken.",
					Toast.LENGTH_LONG).show();
			Intent i = new Intent();
			i.setAction(Intent.ACTION_VIEW);
			i.setData(Uri.parse("market://details?id=org.mixare"));
			startActivity(i);
		}
		finish();
	}

}
