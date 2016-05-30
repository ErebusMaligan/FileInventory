package ui.manager;

import ui.frame.PrimaryFrame;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Apr 19, 2014, 2:35:09 AM 
 */
public class WindowManager {
	
	private static WindowManager instance;
	
	private PrimaryFrame frame;
	
	private WindowManager() {}
	
	public static WindowManager getInstance() {
		if ( instance == null ) {
			instance = new WindowManager();
		}
		return instance;
	}
	
	public PrimaryFrame getPrimaryFrame() {
		return frame;
	}
	
	public void setPrimaryFrame( PrimaryFrame frame ) {
		this.frame = frame;
	}
}