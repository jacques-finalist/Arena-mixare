package org.mixare.plugin;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mixare.plugin.util.OfflineAnswerStorage;
import org.mixare.plugin.util.WebSender;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SendAnswersOnlineActivity extends Activity{
	
	private static final String SHARED_PREFS_NAME = "answer_container";
	private static final String OFFLINE_ANSWERS = "offline_answers";
	private static final String TAG = "SendAnswerOnlineActivity";
	private List<OfflineAnswerStorage> offlineAnswerStorages;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendansweronline);
		try{
			buildOfflineAnswerStorage();
			buildText();
			buildButton();
		}catch(JSONException js){
			Log.e(TAG, "JsonException occured when reading the sharedpreferences", js);
		}
	}
	
	private void buildOfflineAnswerStorage() throws JSONException{
		offlineAnswerStorages = new ArrayList<OfflineAnswerStorage>();
		SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
		String result = sharedPreferences.getString(OFFLINE_ANSWERS, "");
		if(result.isEmpty()){
			return;
		}
		JSONObject jsonObject = new JSONObject(result);
		for(int i = 0; i < jsonObject.length(); i++){
			JSONArray jsonArray = jsonObject.getJSONArray(i+"");
			offlineAnswerStorages.add(new OfflineAnswerStorage(jsonArray.getString(0), jsonArray.getString(1)));
		}
	}
	
	private void buildText(){
		TextView textView = (TextView)findViewById(R.id.sendTitle);
		textView.setText(getResources().getString(R.string.send_answers) +" "+ offlineAnswerStorages.size() 
				+" "+ getResources().getString(R.string.send_answers_2));
	}
	
	private void buildButton(){
		Button button = (Button)findViewById(R.id.sendButton);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for(OfflineAnswerStorage offlineAnswerStorage : offlineAnswerStorages){
					new WebSender(offlineAnswerStorage);
				}
				SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
				SharedPreferences.Editor edit = sharedPreferences.edit();
				edit.clear();
				edit.commit();		
				finish();
			}
		});
	}
}
