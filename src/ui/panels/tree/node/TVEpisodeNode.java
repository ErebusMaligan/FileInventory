package ui.panels.tree.node;

import db.DBN;
import db.element.Row;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 12, 2014, 2:30:22 PM 
 */
public class TVEpisodeNode extends RowNode {

	private static final long serialVersionUID = 1L;
	
	private String type;

	public TVEpisodeNode( Row row ) {
		super( row );
		size = (float)( row.getColumn( DBN.SIZE ).getValue() );
		type = row.getColumn( DBN.FILE_TYPE ).toString();
		name = row.getColumn( DBN.NAME ).toString() + "." + type;
		labelText =  name + "    " + getDispalySize();
	}

	@Override
	public String getTable() {
		return DBN.TV_EPISODE;
	}
	
	public String getType() {
		return type;
	}
	
}