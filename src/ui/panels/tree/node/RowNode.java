package ui.panels.tree.node;

import javax.swing.tree.DefaultMutableTreeNode;

import cache.DataSourceProxy;
import db.element.Row;
import db.element.utils.RowUtils;
import filesystem.FileProperties;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 12, 2014, 2:02:29 PM 
 */
public abstract class RowNode extends DefaultMutableTreeNode {
	
	private static final long serialVersionUID = 1L;

	protected String labelText;
	
	protected float size;
	
	protected String name;
	
	protected String displaySize;

	public RowNode( Row row ) {
		super( row );
	}
	
	public Row getRow() {
		return (Row)getUserObject();
	}
	
	public abstract String getTable();
	
	public boolean containsData( Row r ) {
		return RowUtils.pkEqual( getRow(), r, DataSourceProxy.getInstance().getCache().getTableDefinition( getTable() ) );
	}
	
	public String getLabelText() {
		return labelText;
	}
	
	/**
	 * @return the size
	 */
	public float getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize( float size ) {
		this.size = size;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName( String name ) {
		this.name = name;
	}

	/**
	 * @param labelText the labelText to set
	 */
	public void setLabelText( String labelText ) {
		this.labelText = labelText;
	}
	
	public String getDispalySize() {
		if ( displaySize == null ) {
			displaySize = FileProperties.getDisplaySize( size );
		}
		return displaySize;
	}
}