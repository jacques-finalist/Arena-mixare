package org.mixare.plugin;

import org.mixare.lib.MixUtils;
import org.mixare.lib.gui.ScreenLine;
import org.mixare.lib.marker.draw.ClickHandler;
import org.mixare.lib.marker.draw.PrimitiveProperty;
import org.mixare.plugin.service.ArenaProcessorService;

import android.app.Service;
import android.content.Intent;

/**
 * An extension of imagemarker, that can work offline.
 * @author A. Egal
 *
 */
public class OfflineCapableImageMarker extends ImageMarker{

	private float mouseClickX;
	private float mouseClickY;
	
	public OfflineCapableImageMarker(int id, String title, double latitude,
			double longitude, double altitude, String url, int type, int color) {
		super(id, title, latitude, longitude, altitude, url, type, color);
	}
	
	/**
	 * Called by the mixare core; notices the marker that the user clicked on the screen. And askes
	 * the marker to calculate if it is pressed. Fires a custom ItemViewActivity when it is clicked.
	 */
	@Override
	public ClickHandler fClick() {
		if(isClickValid()){
			Service service = ArenaProcessorService.getInstance();
			Intent dialogIntent = new Intent(service.getBaseContext(), ItemViewActivity.class);
			dialogIntent.putExtra("url", getURL());
			dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			service.getApplication().startActivity(dialogIntent);	
		}
		return null;
	}
	
	/**
	 * Checks if the click on the screen hit this marker (user clicked on marker)
	 */
	private boolean isClickValid(){
		ScreenLine pushedPoint = new ScreenLine();
		float currentAngle = MixUtils.getAngle(cMarker.x, cMarker.y,
				signMarker.x, signMarker.y);
		
		/*if the marker is not active (i.e. not shown in AR view) we 
		 * don't have to check it for clicks */
		if (!isActive() && !isVisible){
			return false;
		}

		return isMarkerClicked(pushedPoint, currentAngle);		
	}
	
	private boolean isMarkerClicked(ScreenLine pushedPoint, float currentAngle){
		pushedPoint.x = mouseClickX - signMarker.x;
		pushedPoint.y = mouseClickY - signMarker.y;
		pushedPoint.rotate((float) Math.toRadians(-(currentAngle + 90)));
		pushedPoint.x += txtLab.getX();
		pushedPoint.y += txtLab.getY();

		float objX = txtLab.getX() - txtLab.getWidth() / 2;
		float objY = txtLab.getY() - txtLab.getHeight() / 2;
		float objW = txtLab.getWidth();
		float objH = txtLab.getHeight();

		if (pushedPoint.x > objX && 
			pushedPoint.x < objX + objW && 
			pushedPoint.y > objY && 
			pushedPoint.y < objY + objH) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * The offlinecapable imagemarker listens for mouseclick locations, so that
	 * it can create its own view, instead of a webview.
	 */
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
