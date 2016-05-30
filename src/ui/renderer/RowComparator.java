package ui.renderer;

import java.util.Comparator;

import db.element.Row;

/**
 * @author Daniel J. Rivers
 *         2015
 *
 * Created: Dec 2, 2015, 2:38:44 AM 
 */
public class RowComparator implements Comparator<Row> {

	private RowConverter con;
	
	public RowComparator( RowConverter con ) {
		this.con = con;
	}
	
	@Override
	public int compare( Row o1, Row o2 ) {
		return con.convert( o1 ).compareTo( con.convert( o2 ) );
	}
}