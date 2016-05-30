package ui.panels.table;

import java.util.Map;

import db.DBN;
import db.element.Row;
import db.element.column.ColumnData;
import filesystem.FileProperties;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Jul 20, 2014, 11:18:38 PM 
 */
public class MovieDataTable extends DefaultDataTable {

	public MovieDataTable( String tableName ) {
		super( tableName );
	}
	
	public MovieDataTable( String tableName, String[] columnNames ) {
		super( tableName, columnNames );
	}
	
	@Override
	protected Object[] convertRow( int i ) {
		Row r = (Row)data.get( i );
		Map<String, ColumnData> cd = r.getValues();
		Object[] row = new Object[ columnNames.length ];
		for ( int q = 0; q < columnNames.length; q++ ) {
			if ( columnNames[ q ].equals( DBN.SIZE ) ) {
				row[ q ] = FileProperties.getDisplaySize( (float)cd.get( columnNames[ q ] ).getValue() );
			} else {
				row[ q ] = cd.get( columnNames[ q ] ).toString();
			}
		}
		return row;
	}

}
