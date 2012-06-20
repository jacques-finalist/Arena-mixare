/*
 * Copyright (C) 2012- Finalist IT Group
 * 
 * This file is part of mixare.
 * 
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package org.mixare.plugin.notification;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;
import android.R;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationMgrImpl implements NotificationManager{

	private Context ctx;
	private boolean enabled = true;
	private static NotificationMgrImpl instance;
	
	private int notifyId = 1100; //unique number
	
	public static NotificationMgrImpl getInstance() {
		return instance;
	}
	
	public NotificationMgrImpl(Context ctx) {
		this.ctx = ctx;
		instance = this;
	}

	@Override
	public void setEnabled(boolean enable) {
		this.enabled = enable;
	}

	@Override
	public void addNotification(String tickerText) {
	    if(enabled){
			android.app.NotificationManager nm = 
					(android.app.NotificationManager)ctx.getSystemService(NOTIFICATION_SERVICE);
			
			PendingIntent pendingIntent
		     = PendingIntent.getActivity(ctx, 0, new Intent(), FLAG_ACTIVITY_NO_ANIMATION);
			
			Notification notif = new Notification(R.drawable.stat_notify_more, tickerText,
		            System.currentTimeMillis());
			notif.setLatestEventInfo(ctx, tickerText, tickerText, pendingIntent);
		    nm.notify(notifyId, notif);
	    }
	}
	
	@Override
	public void addNotification(String tickerText, Class<?> activity) {
	    if(enabled){
			android.app.NotificationManager nm = 
					(android.app.NotificationManager)ctx.getSystemService(NOTIFICATION_SERVICE);
			
			PendingIntent pendingIntent
		     = PendingIntent.getActivity(ctx, 0, new Intent(ctx, activity), FLAG_ACTIVITY_NEW_TASK);
			
			Notification notif = new Notification(R.drawable.stat_notify_more, tickerText,
		            System.currentTimeMillis());
			notif.setLatestEventInfo(ctx, tickerText, tickerText, pendingIntent);
		    nm.notify(notifyId, notif);
	    }
	}
	
	public void clear(){
		android.app.NotificationManager nm = 
				(android.app.NotificationManager)ctx.getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(notifyId);
	}
	
}
