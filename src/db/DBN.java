package db;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Feb 28, 2014, 11:24:14 PM 
 */
public class DBN {
	
	public static final String NETWORK_SOURCE = "NETWORK_SOURCE";
	
	public static final String UNC = "UNC";

	public static final String IP = "IP";

	public static final String SHARES = "SHARES";
	
	
	public static final String NETWORK_SHARE = "NETWORK_SHARE";
	
	public static final String PARENT_UNC = "PARENT_UNC";
	
	public static final String LOCAL_MAPPING = "LOCAL_MAPPING";	

	public static final String NAME = "NAME";
	
	public static final String CONTENT_TYPE = "CONTENT_TYPE";

	
	public static final String CONTENT_TYPES = "CONTENT_TYPES";
	
	
	public static final String CHASSIS = "CHASSIS";
	
	public static final String BAYS = "BAYS";
	
	
	public static final String DISK = "DISK";
	
	public static final String SERIAL = "SERIAL";
	
	public static final String MANUFACTURER = "MANUFACTURER";
	
	public static final String MODEL = "MODEL";
	
	public static final String SIZE = "SIZE";
	
	
	public static final String DISK_CHASSIS = "DISK_CHASSIS";
	
	
	public static final String SHARE_DISK = "SHARE_DISK";
	
	
	public static final String MOVIE = "MOVIE";
	
	public static final String UUID = "UUID";
	
	public static final String DISC_COUNT = "DISC_COUNT";
	
	public static final String FILE_TYPE = "FILE_TYPE";
	
	
	public static final String TV_SERIES = "TV_SERIES";
	
	public static final String EXPECTED = "EXPECTED";
	
	
	public static final String TV_SEASON = "TV_SEASON";
	
	public static final String PARENT_ID = "PARENT_ID";
	
	public static final String NUMBER = "NUMBER";
	
	
	public static final String TV_EPISODE = "TV_EPISODE";
	
	public static final String CONTENT_MOVIE = "MOVIE";
	
	public static final String CONTENT_TV = "TV";
	

	public static final String LOG = "LOG";
	
	public static final String SCANS = "SCANS";
	
	public static final String SCAN_TYPE = "SCAN_TYPE";

	public static final String[] ALL_TABLES = { LOG, NETWORK_SOURCE, NETWORK_SHARE, CONTENT_TYPES, CHASSIS, DISK_CHASSIS, DISK, SHARE_DISK, MOVIE, TV_SERIES, TV_SEASON, TV_EPISODE, SCANS };

	public static final String BAY = "BAY";

	public static final String IMAGE = "IMAGE";
	
	public static final String MSG = "MSG";
	
	public static final String TIME = "TIME";

	public static final String CATEGORY = "CATEGORY";

	public static final String NOTES = "NOTES";

	public static final String TYPE = "TYPE";
	
	
	public static enum LL { INFO, ERROR, CRITICAL };
	

}