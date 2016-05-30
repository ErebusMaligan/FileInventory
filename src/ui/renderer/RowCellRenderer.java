package ui.renderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import db.element.Row;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Apr 30, 2014, 11:54:49 PM 
 */
public class RowCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1L;

	private RowConverter con;
	
	public RowCellRenderer( RowConverter con ) {
		this.con = con;
	}
	
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
		JLabel c = (JLabel)super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
		c.setText( value instanceof Row ? con.convert( (Row)value ) : "New Value" );
		return c;
	}
}