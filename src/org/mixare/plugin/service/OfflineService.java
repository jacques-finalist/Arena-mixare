package org.mixare.plugin.service;

import java.io.IOException;

import org.mixare.lib.service.IDataConverter;
import org.mixare.plugin.offline.OfflineConverter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * Service converts the web url to an offline file
 * @author abdullahi
 *
 */
public class OfflineService extends Service{
	
	public final String PLUGIN_NAME = "offlineservice";
	
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	private final IDataConverter.Stub binder = new IDataConverter.Stub() {

		@Override
		public String convert(String url) throws RemoteException {
			try{
				return new OfflineConverter(url).convert();
			}catch(IOException io){
				return null;
			}			
		}

		@Override
		public String getPluginName() throws RemoteException {
			return PLUGIN_NAME;
		}
				
	};
	
}
