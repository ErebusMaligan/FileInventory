package db.converter;

import gui.props.UIEntryProps;
import gui.props.variable.FloatVariable;
import gui.props.variable.IntVariable;
import gui.props.variable.PropsVariable;

import java.util.ArrayList;
import java.util.List;

import db.element.Row;
import db.element.column.ColumnData;
import db.element.column.IntData;
import db.element.column.NumberData;
import db.element.column.StringData;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Apr 30, 2014, 8:17:11 PM 
 */
public class UIEntryPropsConverter {
	
	public Row convertProps( UIEntryProps props ) {
		Row ret = null;
		List<ColumnData> data = new ArrayList<ColumnData>();
		for ( String s : props.getNames() ) {
			PropsVariable v = props.getVariable( s );
			if ( v instanceof IntVariable ) {
				data.add( new IntData( s, (Integer)v.getValue() ) );
			} else if ( v instanceof FloatVariable ) {
				data.add( new NumberData( s, (Float)v.getValue() ) );
			} else {
				data.add( new StringData( s, v.toString() ) );
			}
		}
		ret = new Row( data );
		return ret;
	}
	
	public void setProps( Row row, UIEntryProps props  ) {
		for ( String s : row.getValues().keySet() ) {
			if ( row.getColumn( s ) != null && row.getColumn( s ).getValue() != null && props.getVariable( s ) != null ) {
				props.getVariable( s ).fromString( row.getColumn( s ).toString() );
			}
		}
	}
}