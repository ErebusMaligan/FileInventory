package db.table;

import java.util.HashMap;
import java.util.Map;

import db.DBN;
import db.element.Table;
import db.element.column.ColumnData;
import db.element.column.IntData;
import db.element.column.NumberData;
import db.element.column.StringData;
import db.instance.specific.sqlite.column.SQLiteStringData;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Mar 1, 2014, 4:08:11 AM 
 */
public class TableFactory {
	
	private static final Map<String, Table> tables = new HashMap<>();
	
	static {
		Table ret = null;
		ret = new Table( DBN.NETWORK_SOURCE, new ColumnData[] { new StringData( DBN.UNC ), new StringData( DBN.IP ), new IntData( DBN.SHARES ) } );
		ret.setPKs( new String[] { DBN.UNC, DBN.IP } );
		tables.put( DBN.NETWORK_SOURCE, ret );
		
		ret = new Table( DBN.NETWORK_SHARE, new ColumnData[] { new StringData ( DBN.UNC ), new StringData( DBN.PARENT_UNC ), new StringData( DBN.LOCAL_MAPPING ), new StringData( DBN.NAME ), new StringData( DBN.CONTENT_TYPE ),  } );
		ret.setPKs( new String[] { DBN.UNC } );
		tables.put( DBN.NETWORK_SHARE, ret );
		
		ret = new Table( DBN.CONTENT_TYPES, new ColumnData[] { new StringData ( DBN.NAME ) } );
		ret.setPKs( new String[] { DBN.NAME } );
		tables.put( DBN.CONTENT_TYPES, ret );
		
		ret = new Table( DBN.CHASSIS, new ColumnData[] { new StringData( DBN.NAME ), new StringData( DBN.IP ), new IntData( DBN.BAYS ), new StringData( DBN.IMAGE ) } );
		ret.setPKs( new String[] { DBN.NAME, DBN.IP } );
		tables.put( DBN.CHASSIS, ret );
		
		ret = new Table( DBN.DISK_CHASSIS, new ColumnData[] { new StringData( DBN.NAME ), new StringData( DBN.SERIAL ), new IntData( DBN.BAY ) } );
		ret.setPKs( new String[] { DBN.NAME, DBN.SERIAL } );
		tables.put( DBN.DISK_CHASSIS, ret );
		
		ret = new Table( DBN.DISK, new ColumnData[] { new StringData( DBN.SERIAL ), new StringData( DBN.MANUFACTURER ), new StringData( DBN.MODEL ), new NumberData( DBN.SIZE ), new StringData( DBN.TYPE ), new StringData( DBN.NOTES ) } );
		ret.setPKs( new String[] { DBN.SERIAL } );
		tables.put( DBN.DISK, ret );
		
		ret = new Table( DBN.SHARE_DISK, new ColumnData[] { new StringData( DBN.UNC ), new StringData( DBN.SERIAL ) } );
		ret.setPKs( new String[] { DBN.UNC, DBN.SERIAL } );
		tables.put( DBN.SHARE_DISK, ret );
		
		ret = new Table( DBN.MOVIE, new ColumnData[] { new StringData( DBN.PARENT_UNC ), new StringData( DBN.NAME ), new IntData( DBN.DISC_COUNT ), new StringData( DBN.FILE_TYPE ), new NumberData( DBN.SIZE ) } );
		ret.setPKs( new String[] { DBN.PARENT_UNC, DBN.NAME } );
		tables.put( DBN.MOVIE, ret );
		
		ret = new Table( DBN.TV_SERIES, new ColumnData[] { new StringData( DBN.UNC ), new StringData( DBN.PARENT_UNC ), new IntData( DBN.EXPECTED ) } ); 
		ret.setPKs( new String[] { DBN.UNC, DBN.PARENT_UNC } );
		tables.put( DBN.TV_SERIES, ret );
		
		ret = new Table( DBN.TV_SEASON, new ColumnData[] { new StringData( DBN.UNC ), new StringData( DBN.PARENT_UNC ), new IntData( DBN.EXPECTED ) } );
		ret.setPKs( new String[] { DBN.UNC, DBN.PARENT_UNC } );
		tables.put( DBN.TV_SEASON, ret );
		
		ret = new Table( DBN.TV_EPISODE, new ColumnData[] { new StringData( DBN.NAME ), new StringData( DBN.PARENT_UNC ), new StringData( DBN.FILE_TYPE ), new NumberData( DBN.SIZE ) } );
		ret.setPKs( new String[] { DBN.NAME, DBN.PARENT_UNC, DBN.FILE_TYPE } );
		tables.put( DBN.TV_EPISODE, ret );
		
		ret = new Table( DBN.LOG, new ColumnData[] { new StringData( DBN.MSG ), new StringData( DBN.CATEGORY ), new SQLiteStringData( DBN.TIME ) } );
		ret.setPKs( new String[] { DBN.MSG, DBN.TIME } );
		tables.put( DBN.LOG, ret );
		
		ret = new Table( DBN.SCANS, new ColumnData[] { new StringData( DBN.LOCAL_MAPPING ), new StringData( DBN.SCAN_TYPE ) } );
		ret.setPKs( new String[] { DBN.LOCAL_MAPPING, DBN.SCAN_TYPE } );
		tables.put( DBN.SCANS, ret );
		
	}
	
	
	public static Table getTable( String name ) {
		return tables.get( name );
	}
}