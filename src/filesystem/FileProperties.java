package filesystem;

import java.text.DecimalFormat;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Nov 30, 2013, 3:03:13 AM 
 */
public class FileProperties {
	
	//file size constants
	public static final float KB = 1024f;
	
	public static final float MB = KB * KB;
	
	public static final float GB = KB * MB;
	
	public static final float TB = GB * KB;

	//File properties
	public static final String NAME = "NAME";
	
	public static final String SIZE = "SIZE";
	
	public static final String SHARE_TYPE = "SHARE_TYPE";
	
	public static final String DRIVE_SIZE = "DRIVE_SIZE";
	
	public static final String SEASONS = "SEASONS";
	
	public static final String EPISODES = "EPISODES";
	
	//share types
	public static final String MOVIE = "Movie";
	
	public static final String TV_SOURCE = "TV Source";
	
	public static final String TV_EPISODE = "TV Episode";
	
	public static final String[] MOVIE_TYPES = new String[] { "iso", "mkv", "avi", "mp4", "divx", "m4v" };
	
	public static final String[] EPISODE_TYPES = new String[] { "flv", "mkv", "mp4", "avi", "vob" };
	
	private static DecimalFormat format = new DecimalFormat( "0.000" );
	
	public static boolean isType( String name, String[] types ) {
		boolean ret = false;
		for ( String s : types ) {
			if ( name.endsWith( s ) ) {
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	
	public static String getDisplaySize( float length ) {
		String u = "MB";
		float val = length / MB;
		if ( val <= 0 ) {
			u = "KB";
			val = length / KB;
		} else if ( val >= 1000 && val < 1000000 ) {
			u = "GB";
			val = length / GB;
		} else if ( val >= 1000000 ) {
			u = "TB";
			val = length / TB;
		}
		return format.format( val ) + " " + u;
	}
}