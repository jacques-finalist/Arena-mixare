package org.mixare.plugin.connection;

import org.mixare.lib.service.IDataConverter;
import org.mixare.plugin.PluginConnection;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class DataConverterServiceConnection extends PluginConnection implements
		ServiceConnection {
	
	private IDataConverter iDataConverter;
	
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// get instance of the aidl binder
		iDataConverter = IDataConverter.Stub
				.asInterface(service);
		storeFoundPlugin();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {

	}

	public IDataConverter getDataConverter(){
		return iDataConverter;
	}
	
}
