/**
 * 
 */
package org.mixare.plugin;

import org.mixare.lib.marker.PluginMarker;
import org.mixare.lib.marker.draw.DrawCommand;
import org.mixare.lib.marker.draw.DrawImage;
import org.mixare.lib.marker.draw.DrawTextBox;
import org.mixare.lib.marker.draw.ParcelableProperty;

import android.graphics.Bitmap;

/**
 * @author A.Egal
 * An custom marker that shows a bitmap image in the augmented reality browser
 */
public class ImageMarker extends PluginMarker{

	public static final int MAX_OBJECTS = 20;
	private Bitmap image; 

	public ImageMarker(int id, String title, double latitude, double longitude,
			double altitude, String url, int type, int color) {
		super(id, title, latitude, longitude, altitude, url, type, color);
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
	public void setExtras(String name, ParcelableProperty parcelableProperty) {
		if(name.equals("bitmap")){
			image = (Bitmap)parcelableProperty.getObject();
		}
	}
	
}