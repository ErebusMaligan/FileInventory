package ui.panels.chassis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import db.access.DBAccess;
import db.element.Row;
import gui.dialog.OKCancelDialog;
import gui.entry.ComboBoxEntry;
import gui.props.variable.StringVariable;
import statics.GU;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 26, 2014, 3:22:55 AM 
 */
public class ShareSelectionDialog extends OKCancelDialog {

	private static final long serialVersionUID = 1L;

	private StringVariable s = new StringVariable();
	
	private Row selected;
	
	public ShareSelectionDialog( Frame owner ) {
		super( owner, "Select Share", true );
		setSize( 400, 160 );
		setLayout( new BorderLayout() );
		
		JPanel center = new JPanel();
		ComboBoxEntry c = new ComboBoxEntry( "Share:", s, new Dimension[] { GU.FIELD, GU.LONG } );
		c.addContent( "NONE" );
		List<String> shares = DBAccess.getAvaialableShares();
		Collections.sort( shares );
		for ( String s : shares ) {
			c.addContent( s );
		}
		center.add( c );
		this.add( center, BorderLayout.CENTER );
		this.add( getButtonPanel(), BorderLayout.SOUTH );
	}
	
	@Override
	public void ok() {
		selected = DBAccess.getShareForUNC( s.toString() );
		super.ok();
	}
	
	public Row getShareRow() {
		return selected;
	}
}
