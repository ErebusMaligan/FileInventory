package db.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cache.handler.CacheHandler;
import db.DBN;
import db.access.DBAccess;
import db.element.Row;
import db.element.column.ColumnData;
import db.element.column.NumberData;
import db.element.column.StringData;
import filesystem.recurse.GenericRecord;
import filesystem.recurse.tv.TVShare;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 11, 2014, 11:14:17 PM 
 */
public class TVShareConverter {
	
	public Map<String, List<Row>> convertToDB( TVShare ms ) {
		Map<String, List<Row>> ret = new HashMap<>();
		List<Row> series = new ArrayList<>();
		List<Row> season = new ArrayList<>();
		List<Row> episode = new ArrayList<>();
		for ( GenericRecord g : ms.getChildren() ) {		
			List<ColumnData> cd = new ArrayList<>();
			cd.add( new StringData( DBN.UNC, g.get( DBN.UNC ) ) );
			cd.add( new StringData( DBN.PARENT_UNC, g.get( DBN.PARENT_UNC ) ) );
			series.add( new Row( DBN.TV_SERIES, cd ) );
			for ( GenericRecord g2 : g.getChildren() ) {
				List<ColumnData> cd2 = new ArrayList<>();
				cd2.add( new StringData( DBN.UNC, g2.get( DBN.UNC ) ) );
				cd2.add( new StringData( DBN.PARENT_UNC, g2.get( DBN.PARENT_UNC ) ) );
				season.add( new Row( DBN.TV_SEASON, cd2 ) );
				for ( GenericRecord g3 : g2.getChildren() ) {
					List<ColumnData> cd3 = new ArrayList<>();
					cd3.add( new StringData( DBN.NAME, g3.get( DBN.NAME ) ) );
					cd3.add( new StringData( DBN.PARENT_UNC, g3.get( DBN.PARENT_UNC ) ) );
					cd3.add( new NumberData( DBN.SIZE, Float.parseFloat( g3.get( DBN.SIZE ) ) ) );
					cd3.add( new StringData( DBN.FILE_TYPE, g3.get( DBN.FILE_TYPE ) ) );
					episode.add( new Row( DBN.TV_EPISODE, cd3 ) );
				}
			}
		}
		ret.put( DBN.TV_SERIES, series );
		ret.put( DBN.TV_SEASON, season );
		ret.put( DBN.TV_EPISODE, episode );
		return ret;
	}

	
	public void writeRecords( Map<String, List<Row>> rows ) {
		for ( String s : new String[] { DBN.TV_SERIES, DBN.TV_SEASON, DBN.TV_EPISODE } ) {
			CacheHandler h = DBAccess.getHandler( s );
			rows.get( s ).forEach( r -> h.merge( r ) );
			if ( s.equals( DBN.TV_SERIES ) ) {
				Row r = DBAccess.getShareForUNC( rows.get( s ).get( 0 ).getColumn( DBN.PARENT_UNC ).toString() );
				r.setColumn( DBN.CONTENT_TYPE, new StringData( DBN.CONTENT_TYPE, DBN.TV_SERIES ) );
				DBAccess.getHandler( DBN.NETWORK_SHARE ).merge( r );
			}
		}
	}
}