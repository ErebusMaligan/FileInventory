package ui.renderer;

import db.element.Row;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 1, 2014, 12:35:52 AM 
 */
public interface RowConverter {
	public String convert( Row r );
}
