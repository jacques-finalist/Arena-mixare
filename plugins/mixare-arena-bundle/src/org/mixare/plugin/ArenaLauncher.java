package org.mixare.plugin;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

public class ArenaLauncher extends Activity {

	// is set when mixare is installed
	boolean isMixareInstalled = false;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// We try to locate mixare on the phone
		isMixareInstalled = false;
		try {
			PackageInfo pi = getPackageManager()
					.getPackageInfo("org.mixare", 0);

			if (pi.versionCode >= 1) {
				isMixareInstalled = true;
			}
		} catch (PackageManager.NameNotFoundException ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {

		super.onResume();

		// Now we should have the position, let's proceed only if mixare is
		// installed
		if (isMixareInstalled) {
			// start mixare
			Intent i = new Intent();
			i.setAction(Intent.ACTION_VIEW);
			i.setDataAndType(Uri.parse("http://www.mixare.org/geotest.php"), "application/mixare-json");
			startActivity(i);

		} else {
			// Mixare is not installed, let's go to the market!
			try {
				Intent i = new Intent();
				i.setAction(Intent.ACTION_VIEW);
				i.setData(Uri.parse("market://search?q=pname:org.mixare"));
				startActivity(i);

				finish();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

}
