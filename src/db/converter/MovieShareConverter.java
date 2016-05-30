package db.converter;

import java.util.ArrayList;
import java.util.List;

import cache.DataSourceProxy;
import cache.handler.CacheHandler;
import db.DBN;
import db.access.DBAccess;
import db.element.Row;
import db.element.column.ColumnData;
import db.element.column.IntData;
import db.element.column.NumberData;
import db.element.column.StringData;
import filesystem.recurse.GenericRecord;
import filesystem.recurse.movie.MovieShare;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 5, 2014, 12:23:47 AM 
 */
public class MovieShareConverter {
	
	public List<Row> convertToDB( MovieShare ms ) {
		List<Row> ret = new ArrayList<>();
		for ( GenericRecord g : ms.getChildren() ) {
			List<ColumnData> cd = new ArrayList<>();
			cd.add( new StringData( DBN.PARENT_UNC, g.get( DBN.UNC ) ) );
			cd.add( new StringData( DBN.NAME, g.get( DBN.NAME ) ) );
			cd.add( new IntData( DBN.DISC_COUNT, g.getChildren().size() ) );
			cd.add( new StringData( DBN.FILE_TYPE, g.get( DBN.FILE_TYPE ) ) );
			cd.add( new NumberData( DBN.SIZE, Float.parseFloat( g.get( DBN.SIZE ) ) ) );
			ret.add( new Row( DBN.MOVIE, cd ) );
		}
		return ret;
	}
	
	public void writeRecords( List<Row> rows ) {
		CacheHandler h = DataSourceProxy.getInstance().getCache().getHandler( DBN.MOVIE );
		for ( Row r : rows ) {
			h.merge( r );
		}
		Row r = DBAccess.getShareForUNC( rows.get( 0 ).getColumn( DBN.PARENT_UNC ).toString() );
		r.getColumn( DBN.CONTENT_TYPE ).fromString( DBN.MOVIE );
		DBAccess.getHandler( DBN.NETWORK_SHARE ).merge( r );
	}
}