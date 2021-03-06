package ui.panels.tree.node;

import net.NetUtils;
import db.DBN;
import db.element.Row;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 12, 2014, 2:30:22 PM 
 */
public class TVSeriesNode extends RowNode {

	private static final long serialVersionUID = 1L;

	public TVSeriesNode( Row row ) {
		super( row );
		name = NetUtils.stripUNCEnd( row.getColumn( DBN.UNC ).toString() );
		labelText =  name + "    " + getDispalySize();
	}

	@Override
	public String getTable() {
		return DBN.TV_SERIES;
	}

	public void setSize( float size ) {
		this.size = size;
		displaySize = null;
		labelText = name + "    " + getDispalySize();
	}
}