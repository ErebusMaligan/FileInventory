package ui.panels.chassis;

import gui.dialog.OKCancelDialog;
import gui.entry.ComboBoxEntry;
import gui.props.variable.StringVariable;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import db.access.DBAccess;
import db.element.Row;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 26, 2014, 3:22:55 AM 
 */
public class DiskSelectionDialog extends OKCancelDialog {

	private static final long serialVersionUID = 1L;

	private StringVariable s = new StringVariable();
	
	private Row selected;
	
	public DiskSelectionDialog( Frame owner ) {
		super( owner, "Select Disk", true );
		setSize( 400, 160 );
		setLayout( new BorderLayout() );
		
		JPanel center = new JPanel();
		ComboBoxEntry c = new ComboBoxEntry( "Disk:", s );
		c.addContent( "NONE" );
		List<String> serials = DBAccess.getAvailableDiskSerials();
		Collections.sort( serials );
		for ( String s : serials ) {
			c.addContent( s );
		}
		center.add( c );
		this.add( center, BorderLayout.CENTER );
		this.add( getButtonPanel(), BorderLayout.SOUTH );
	}
	
	@Override
	public void ok() {
		selected = DBAccess.getDiskBySerial( s.toString() );
		super.ok();
	}
	
	public Row getDiskRow() {
		return selected;
	}
}
