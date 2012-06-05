/**
 * 
 */
package org.mixare.plugin;

import org.mixare.lib.MixUtils;
import org.mixare.lib.gui.ScreenLine;
import org.mixare.lib.marker.PluginMarker;
import org.mixare.lib.marker.draw.ClickHandler;
import org.mixare.lib.marker.draw.DrawCommand;
import org.mixare.lib.marker.draw.DrawImage;
import org.mixare.lib.marker.draw.DrawTextBox;
import org.mixare.lib.marker.draw.ParcelableProperty;
import org.mixare.lib.marker.draw.PrimitiveProperty;
import org.mixare.plugin.service.ArenaProcessorService;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * @author A.Egal
 * An custom marker that shows a bitmap image in the augmented reality browser
 */
public class ImageMarker extends PluginMarker{

	public static final int MAX_OBJECTS = 20;
	private Bitmap image; 
	public static final int OSM_URL_MAX_OBJECTS = 5;
	
	private float mouseClickX;
	private float mouseClickY;

	public ImageMarker(int id, String title, double latitude, double longitude,
			double altitude, String URL, int type, int color) {
		super(id, title, latitude, longitude, altitude, URL, type, color);
	}

	@Override
	public int getMaxObjects() {
		return MAX_OBJECTS;
	}
	
	@Override
	public DrawCommand[] remoteDraw(){
		DrawCommand[] dCommands = new DrawCommand[2];
		dCommands[0] = new DrawImage(isVisible, signMarker, image);
		dCommands[1] = new DrawTextBox(isVisible, distance, title, underline, textBlock, txtLab, signMarker);
		return dCommands;
	}
	
	@Override
	public ClickHandler fClick() {
		if(isClickValid()){
			Service service = ArenaProcessorService.instance;
			Intent dialogIntent = new Intent(service.getBaseContext(), ItemViewActivity.class);
			dialogIntent.putExtra("url", getURL());
			dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			service.getApplication().startActivity(dialogIntent);	
		}
		return null;
	}
	
	private boolean isClickValid(){
		ScreenLine pPt = new ScreenLine();
		float currentAngle = MixUtils.getAngle(cMarker.x, cMarker.y,
				signMarker.x, signMarker.y);
		//if the marker is not active (i.e. not shown in AR view) we don't have to check it for clicks
		if (!isActive() && !isVisible)
			return false;

		pPt.x = mouseClickX - signMarker.x;
		pPt.y = mouseClickY - signMarker.y;
		pPt.rotate((float) Math.toRadians(-(currentAngle + 90)));
		pPt.x += txtLab.getX();
		pPt.y += txtLab.getY();

		float objX = txtLab.getX() - txtLab.getWidth() / 2;
		float objY = txtLab.getY() - txtLab.getHeight() / 2;
		float objW = txtLab.getWidth();
		float objH = txtLab.getHeight();

		if (pPt.x > objX && pPt.x < objX + objW && pPt.y > objY
				&& pPt.y < objY + objH) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void setExtras(String name, ParcelableProperty parcelableProperty) {
		if(name.equals("bitmap")){
			image = (Bitmap)parcelableProperty.getObject();
		}
	}
	
	@Override
	public void setExtras(String name, PrimitiveProperty primitiveProperty) {
		if(name.equals("x")){
			mouseClickX = (Float)primitiveProperty.getObject();
		}
		if(name.equals("y")){
			mouseClickY = (Float)primitiveProperty.getObject();
		}
	}
	
}