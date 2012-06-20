package org.mixare.plugin.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mixare.plugin.R;
import org.mixare.plugin.SendAnswersOnlineActivity;
import org.mixare.plugin.notification.NotificationMgrImpl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class OfflineAnswerStorage {

	private final static String OFFLINE_ANSWERS = "offline_answers";
	
	final private String submitUrl;
	final private String answer;
	
	public OfflineAnswerStorage(final String submitUrl, final String answer) {
		this.submitUrl = submitUrl.replace("(", "").replace(")", "");
		this.answer = answer;
	}

	public String getSubmitUrl() {
		return submitUrl;
	}

	public String getAnswer() {
		return answer;
	}
	
	public void store(SharedPreferences sharedPrefs, Context ctx) throws JSONException{
		String result = sharedPrefs.getString(OFFLINE_ANSWERS, "");
		JSONObject jsonObject;
		if(result.isEmpty()){
			jsonObject = new JSONObject();
		}else{
			jsonObject = new JSONObject(result);
		}
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(submitUrl);
		jsonArray.put(answer);
		
		jsonObject.put(jsonObject.length()+"", jsonArray);
		
		Editor editor = sharedPrefs.edit();
		editor.putString(OFFLINE_ANSWERS, jsonObject.toString());
		editor.commit();
		
		NotificationMgrImpl.getInstance().addNotification(ctx.getResources().getString(R.string.click_here_send_answers), SendAnswersOnlineActivity.class);		
	}
}
