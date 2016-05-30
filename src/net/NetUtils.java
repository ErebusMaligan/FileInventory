package net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import cache.Cache;
import cache.DataSourceProxy;
import cache.handler.CacheHandler;
import db.DBN;
import db.element.Row;
import db.element.column.ColumnData;
import db.element.column.IntData;
import db.element.column.StringData;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Jan 1, 2014, 11:43:29 PM 
 */
public class NetUtils {
	
	public static InetAddress getInet( String sysName ) throws UnknownHostException {
		InetAddress ret = null;
		if ( sysName != null ) {
			ret = InetAddress.getByName( sysName.substring( 2 ) );
		}
		return ret;
	}
	
	public static String extractShareName( String share ) {
		return share.split( "Disk" )[ 0 ].trim();
	}
	
	public static String extractShareLocalDisk( String share ) {
		String[] arr = share.split( "Disk" ); 
		return arr.length > 1 ? arr[ 1 ].trim() : null;
	}
	
	public static String toUNCPath( String parent, String current ) {
		return parent + "\\" + current;
	}
	
	//does not check to see if UNC is null or even contains a path... meh
	public static String stripUNCEnd( String unc ) {
		return unc.substring( unc.lastIndexOf( "\\" ) + 1 );
	}
	
	public static void scanNetworkShares() {
		System.out.println( "Scanning Network Shares To DB..." );
		NetInterface n = NetInterface.getInstance();
		Cache cache = DataSourceProxy.getInstance().getCache();
		CacheHandler sourceH = cache.getHandler( DBN.NETWORK_SOURCE );
		CacheHandler shareH = cache.getHandler( DBN.NETWORK_SHARE );
		List<String> systems = n.getRemoteSystemListing();
		if ( systems != null ) {
			for ( String s : n.getRemoteSystemListing() ) {
				scanSystem( s, n, sourceH, shareH );
			}
		}
		System.out.println( "Completed Network Scan." );
	}
	
	private static void scanSystem( String s, NetInterface n, CacheHandler sourceH, CacheHandler shareH ) {
		s = s.contains( " " ) ? s.substring( 0, s.indexOf( " " ) ) : s; 
		System.out.println( "Scanning: " + s );
		ArrayList<ColumnData> netSource = new ArrayList<ColumnData>();
		netSource.add( new StringData( DBN.UNC, s ) );
		try {
			netSource.add( new StringData( DBN.IP, NetUtils.getInet( s ).getHostAddress() ) );
			System.out.println( "Retreiving Shares for: " + s );
			List<String> shares = n.getRemoteSystemShares( s );
			if ( shares != null ) {
				netSource.add( new IntData( DBN.SHARES, shares.size() ) );
				sourceH.merge( new Row( netSource ) );
				for ( String sh : shares ) {
					System.out.println( "Adding Info for Share: " + sh );
					String name = NetUtils.extractShareName( sh );
					String local = NetUtils.extractShareLocalDisk( sh );
					shareH.merge( new Row( new ColumnData[] { new StringData( DBN.PARENT_UNC, s ), new StringData( DBN.UNC, NetUtils.toUNCPath( s, name ) ), new StringData( DBN.NAME, name ),
							new StringData( DBN.LOCAL_MAPPING, local ) } ) );
				}
			}
		} catch ( UnknownHostException e ) {
			e.printStackTrace();
		}
	}
	
//	public static List<String> getAvaialableDriveLetters() {
//		List<String> ret = new ArrayList<String>();
//		NetInterface n = NetInterface.getInstance();
//		for ( int i = 65; i < 91; i++ ) { //65, 91
//			String test = "" + (char)i;
//			if ( n.testDriveLetter( test ) == null ) {
//				ret.add( test );
//			}
//		}
//		return  ret;
//	}
//	
//	public static List<String> getUsedDriveLetters() {
//		List<String> ret = new ArrayList<String>();
//		NetInterface n = NetInterface.getInstance();
//		for ( int i = 65; i < 91; i++ ) { //65, 91
//			String test = "" + (char)i;
//			if ( n.testDriveLetter( test ) != null ) {
//				ret.add( test );
//			}
//		}
//		return  ret;
//	}
}