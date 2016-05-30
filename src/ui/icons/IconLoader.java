package ui.icons;

import icon.DefaultIconLoader;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 12, 2014, 10:15:35 PM 
 */
public class IconLoader extends DefaultIconLoader {
	
	public static final String RIGHT_ARROW = "RightArrow";
	
	public static final String DOWN_ARROW = "DownArrow";
	
	public static final String RIGHT_HD = "RightHardDrive";
	
	public static final String DOWN_HD = "DownHardDrive";
	
	public static final String REFRESH = "Refresh";
	
	public static final String PRINT = "Print";
	
	private static IconLoader instance;
	
	private IconLoader() {
		names = new String[] { RIGHT_ARROW, DOWN_ARROW, RIGHT_HD, DOWN_HD, REFRESH, PRINT };
		init();
	}
		
	public static IconLoader getInstance() {
			return instance != null ? instance : new IconLoader();
	}
}