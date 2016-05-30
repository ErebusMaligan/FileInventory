package db.access;

import gui.dialog.BusyDialog;
import gui.dialog.OKCancelDialog;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import ui.dialog.MoveSeriesDialog;
import ui.manager.WindowManager;
import cache.DataSourceProxy;
import cache.handler.CacheHandler;
import db.DBN;
import db.element.Row;
import db.element.column.ColumnData;
import db.element.column.IntData;
import db.element.column.StringData;
import db.instance.specific.sqlite.column.SQLiteStringData;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Apr 18, 2014, 11:26:22 PM 
 */
public class DBAccess {
	
	public static CacheHandler getHandler( String table ) {
		return DataSourceProxy.getInstance().getCache().getHandler( table );
	}
	
	public static Row getNetworkSourceFromIP( String IP ) {
		List<ColumnData> cd = new ArrayList<>();
		cd.add( new StringData( DBN.IP, IP ) );
		return getHandler( DBN.NETWORK_SOURCE ).getRow( cd );
	}
	
	public static List<Row> getDiskChassisRecords( String chassis ) {
		Vector<Row> rows = getHandler( DBN.DISK_CHASSIS ).getAllRows();
		List<Row> ret = new ArrayList<>();
		rows.forEach( r -> {
			if ( r.getColumn( DBN.NAME ).toString().equals( chassis ) ) {
				ret.add( r.clone() );
			}
		} );
		return ret;
	}
	
	public static void log( String msg, DBN.LL level ) {
		List<ColumnData> cd = new ArrayList<>();
		cd.add( new StringData( DBN.MSG, msg ) );
		cd.add( new StringData( DBN.CATEGORY, level.toString() ) );
		cd.add( new SQLiteStringData( DBN.TIME, new Timestamp( System.currentTimeMillis() ) ) );
		getHandler( DBN.LOG ).merge( new Row( cd ) );
	}
	
	public static List<Row> getCurScans() {
		return getHandler( DBN.SCANS ).getAllRows();
	}
	
	public static void enterScan( String path, String type ) {
		getHandler( DBN.SCANS ).merge( new Row( new ColumnData[] { new StringData( DBN.LOCAL_MAPPING, path ), new StringData( DBN.SCAN_TYPE, type ) } ) );
	}
	
	public static void removeScan( Row r ) {
		getHandler( DBN.SCANS ).delete( r );
	}
	
	public static Row getShareForDisk( Row disk ) {
		Row ret = null;
		List<ColumnData> cd = new ArrayList<>();
		cd.add( disk.getColumn( DBN.SERIAL ).clone() );
		Row r = getHandler( DBN.SHARE_DISK ).getRow( cd );
		if ( r != null ) {
			cd.clear();
			cd.add( r.getColumn( DBN.UNC ).clone() );
			ret = getHandler( DBN.NETWORK_SHARE ).getRow( cd );
		}
		return ret;
	}
	
	public static void handleShareDiskMapping( Row disk, Row share ) {
		if ( share == null ) {
			List<ColumnData> cd = new ArrayList<>();
			cd.add( disk.getColumn( DBN.SERIAL ).clone() );
			Row r = getHandler( DBN.SHARE_DISK ).getRow( cd );
			if ( r != null ) {
				getHandler( DBN.SHARE_DISK ).delete( r );
			}
		} else {
			List<ColumnData> cd = new ArrayList<>();
			cd.add( share.getColumn( DBN.UNC ).clone() );
			cd.add( disk.getColumn( DBN.SERIAL ).clone() );
			getHandler( DBN.SHARE_DISK ).merge( new Row( cd ) );
		}
	}
	
	public static void handleDiskChassisMapping( Row disk, Row chassis, int bay ) {
		if ( disk == null ) {
			Row r = getDiskChassisRowForBay( chassis, bay );
			if ( r != null ) {
				getHandler( DBN.DISK_CHASSIS ).delete( r );
			}
		} else {
			List<ColumnData> cd = new ArrayList<>();
			cd.add( chassis.getColumn( DBN.NAME ).clone() );
			cd.add( disk.getColumn( DBN.SERIAL ).clone() );
			cd.add( new IntData( DBN.BAY, bay ) );
			getHandler( DBN.DISK_CHASSIS ).merge( new Row( cd ) );
		}
	}
	
	public static Row getDiskBySerial( String serial ) {
		List<ColumnData> cd = new ArrayList<>();
		cd.add( new StringData( DBN.SERIAL, serial ) );
		return getHandler( DBN.DISK ).getRow( cd );
	}
	
	public static Row getDiskRowForBay( Row chassis, int bay ) {
		Row ret = null;
		Row dc = getDiskChassisRowForBay( chassis, bay );
		if ( dc != null ) {
			ret = getDiskFromDCRow( dc );
		}
		return ret;
	}
	
	public static Row getDiskChassisRowForBay( Row chassis, int bay ) {
		List<ColumnData> criteria = new ArrayList<>();
		criteria.add( chassis.getColumn( DBN.NAME ).clone() );
		criteria.add( new IntData( DBN.BAY, bay ) );
		return getHandler( DBN.DISK_CHASSIS ).getRow( criteria );
	}
	
	public static Row getDiskFromDCRow( Row dc ) {
		List<ColumnData> criteria = new ArrayList<>();
		criteria.add( dc.getColumn( DBN.SERIAL ).clone() );
		return getHandler( DBN.DISK ).getRow( criteria );
	}
	
	public static void setDiskToBay( Row chassis, Row disk, int bay ) {
		List<ColumnData> cd = new ArrayList<>();
		cd.add( chassis.getColumn( DBN.NAME ).clone() );
		cd.add( disk.getColumn( DBN.SERIAL ).clone() );
		cd.add( new IntData( DBN.BAY, bay ) );
		getHandler( DBN.DISK_CHASSIS ).merge( new Row( cd ) );
	}
	
	public static void deleteSeries( final Row series ) {
		new BusyDialog( WindowManager.getInstance().getPrimaryFrame() ) {
			private static final long serialVersionUID = 1L;
			@Override
			public void executeTask() {
				CacheHandler ep = getHandler( DBN.TV_EPISODE );
				CacheHandler sea = getHandler( DBN.TV_SEASON );
				CacheHandler ser = getHandler( DBN.TV_SERIES );
				List<Row> seasons = DBAccess.getSeasonsForSeries( series );
				seasons.forEach( r -> { 
					DBAccess.getEpisodesForSeason( r ).forEach( e -> ep.delete( e ) ); 
					sea.delete( r ); 
				} );
				ser.delete( series );
			}
		};
	}
	
	public static void moveSeries( final Row series ) {
		MoveSeriesDialog d = new MoveSeriesDialog( WindowManager.getInstance().getPrimaryFrame() );
		d.setVisible( true );
		if ( d.getResult() == OKCancelDialog.OK ) {
			final String n = d.getShare();
			new BusyDialog( WindowManager.getInstance().getPrimaryFrame() ) {
				private static final long serialVersionUID = 1L;
				@Override
				public void executeTask() {		
					String o = series.getColumn( DBN.PARENT_UNC ).toString();
					CacheHandler ep = getHandler( DBN.TV_EPISODE );
					CacheHandler sea = getHandler( DBN.TV_SEASON );
					CacheHandler ser = getHandler( DBN.TV_SERIES );
					List<Row> seasons = DBAccess.getSeasonsForSeries( series );
					List<Row> nEp = new ArrayList<Row>();
					List<Row> nSea = new ArrayList<Row>();
					seasons.forEach( r -> {
						DBAccess.getEpisodesForSeason( r ).forEach( e -> {
							nEp.add( e.clone() );
							ep.delete( e );
						} );
						nSea.add( r.clone() );
						sea.delete( r );
					} );
					Row nSer = series.clone();
					ser.delete( series );
					nSer.getColumn( DBN.PARENT_UNC ).fromString( n );
					nSer.getColumn( DBN.UNC ).fromString( nSer.getColumn( DBN.UNC ).toString().replace( o, n ) );
					nSea.forEach( r -> {
						String oS = r.getColumn( DBN.UNC ).toString();
						r.getColumn( DBN.PARENT_UNC ).fromString( nSer.getColumn( DBN.UNC ).toString() );
						r.getColumn( DBN.UNC ).fromString( r.getColumn( DBN.UNC ).toString().replace( o, n ) );
						nEp.forEach( e -> {
							if ( e.getColumn( DBN.PARENT_UNC ).toString().equals( oS ) ) {
								e.getColumn( DBN.PARENT_UNC ).fromString( r.getColumn( DBN.UNC ).toString() );
							}
						} );
					} );
					ser.merge( nSer );
					nSea.forEach( r -> sea.merge( r ) );
					nEp.forEach( e -> ep.merge( e ) );
				}
			};
		}
	}
	
	public static Row getShareForDisk( String serial ) {
		List<ColumnData> cd = new ArrayList<>();
		cd.add( new StringData( DBN.SERIAL, serial ) );
		return getHandler( DBN.NETWORK_SHARE ).getRow( cd );
	}
	
	public static Row getShareForUNC( String unc ) {
		List<ColumnData> cd = new ArrayList<>();
		cd.add( new StringData( DBN.UNC, unc ) );
		return getHandler( DBN.NETWORK_SHARE ).getRow( cd );
	}
	
	public static List<Row> getSeasonsForSeries( Row series ) {
		List<ColumnData> cd = new ArrayList<>();
		cd.add( new StringData( DBN.PARENT_UNC, series.getColumn( DBN.UNC ).toString() ) );
		return getHandler( DBN.TV_SEASON ).getRows( cd );
	}
	
	public static List<Row> getEpisodesForSeason( Row season ) {
		List<ColumnData> cd = new ArrayList<>();
		cd.add( new StringData( DBN.PARENT_UNC, season.getColumn( DBN.UNC ).toString() ) );
		return getHandler( DBN.TV_EPISODE ).getRows( cd );		
	}
	
	public static String getPathForFile( Row file ) {
		String ret = null;
		try {
			String s = file.getColumn( DBN.PARENT_UNC ).toString();
			int i = s.indexOf( "\\", 2 );
			i = s.indexOf( "\\", i + 1 );
			String path = s.substring( i );
			String unc = s.substring( 0, i );
			List<ColumnData> cd = new ArrayList<>();
			cd.add( new StringData( DBN.UNC, unc ) );
			String letter = getHandler( DBN.NETWORK_SHARE ).getRow( cd ).getColumn( DBN.LOCAL_MAPPING ).toString();
			String f = "\\" + file.getColumn( DBN.NAME ).toString() + "." + file.getColumn( DBN.FILE_TYPE );
			return letter + path + f;
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return ret;
	}
	
	private static List<String> getColumnAsString( String table, String column, boolean allowDupes ) {
		List<String> ret = new ArrayList<>();
		List<ColumnData> cd = getHandler( table ).getColumnData( column, allowDupes );
		cd.forEach( c -> ret.add( c.toString() ) );
		return ret;
	}
	
	public static List<String> getIPs() {
		return getColumnAsString( DBN.NETWORK_SOURCE, DBN.IP, true );
	}
	
	public static List<String> getChassisNames() {
		return getColumnAsString( DBN.CHASSIS, DBN.NAME, true );
	}
	
	public static List<String> getAvailableDiskSerials() {
		List<String> ret = new ArrayList<>();
		List<String> all = getDiskSerials();
		List<ColumnData> cd = new ArrayList<>();
		for ( String s : all ) {
			cd.clear();
			cd.add( new StringData( DBN.SERIAL, s ) );
			if ( getHandler( DBN.DISK_CHASSIS ).getRow( cd ) == null ) {
				ret.add( s );
			}
		}
		return ret;
	}
	
	public static List<String> getAvaialableShares() {
		List<String> ret = new ArrayList<>();
		List<String> all = getColumnAsString( DBN.NETWORK_SHARE, DBN.UNC, true );
		List<ColumnData> cd = new ArrayList<>();
		for ( String s : all ) {
			cd.clear();
			cd.add( new StringData( DBN.UNC, s ) );
//			if ( getHandler( DBN.SHARE_DISK ).getRow( cd ) == null ) {  this works fine, but then I can't account for raided drives that point to the same share
				ret.add( s );
//			}
		}
		return ret;
	}
	
	public static List<String> getDiskSerials() {
		return getColumnAsString( DBN.DISK, DBN.SERIAL, true );
	}

	public static List<String> getShareUNCPaths() {
		return getColumnAsString( DBN.NETWORK_SHARE, DBN.UNC, true );
	}
	
	public static String getShareForDrive( String drive ) {
		String ret = null;
		if ( drive != null ) {
			Vector<Row> rows = getHandler( DBN.NETWORK_SHARE ).getAllRows();
			for ( Row r : rows ) {
				String d = r.getColumn( DBN.LOCAL_MAPPING ).toString(); 
				if ( !d.equals( ColumnData.nullSQL ) ) {
					if ( d.equals( drive ) ) {
						ret = r.getColumn( DBN.UNC ).toString();
						break;
					}
				}
			}
		}
		return ret;
	}
	
	public static void updateLocalDrive( String share, String letter ) {
		if ( share != null ) {
			CacheHandler h = getHandler( DBN.NETWORK_SHARE );
			Vector<Row> rows = h.getAllRows();
			for ( Row r : rows ) {
				if ( r.getColumn( DBN.UNC ).toString().equals( share ) ) {
					String l = letter;
					if ( l == null ) {
						l = ColumnData.nullSQL;
					}
					r.getColumn( DBN.LOCAL_MAPPING ).fromString( l );
					h.merge( r );
					break;
				}
			}
		}
	}
}