package org.mixare.plugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mixare.plugin.util.WebReader;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class ItemViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(LayoutParams.FLAG_NOT_TOUCH_MODAL,	LayoutParams.FLAG_NOT_TOUCH_MODAL);
		getWindow().setFlags(LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
		buildView(savedInstanceState.getString("url"));
	}
	
	private void buildView(String url){
		WebReader webReader = new WebReader(url);
		JSONObject json = convertStringToJson(webReader.getResult());
		try {
			buildGuiDialog(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private JSONObject convertStringToJson(String content){
		try {
			return new JSONObject(content);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void buildGuiDialog(JSONObject json) throws JSONException{
		if(json.getString("type").equals("Question")){
			setContentView(R.layout.questionitem);
			TextView question = (TextView)findViewById(R.id.question);
			question.setText(json.getString("description"));
			JSONArray answers = json.getJSONArray("answers");
			if(answers.length() > 0){
				
			}else{
				
			}
		}
	}
	
	/**
	 * Close when pressing outside of activity.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
			finish();
			return true;
		}
		return super.onTouchEvent(event);
	}

}
