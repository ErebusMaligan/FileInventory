package ui.dialog;

import gui.dialog.OKCancelDialog;
import gui.entry.ComboBoxEntry;
import gui.props.variable.StringVariable;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import db.access.DBAccess;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 24, 2014, 12:06:34 AM 
 */
public class MoveSeriesDialog extends OKCancelDialog {

	private static final long serialVersionUID = 1L;
	
	private StringVariable selected = new StringVariable();
	
	public MoveSeriesDialog( Frame owner ) {
		super( owner, "Move Series To:", true );
		setSize( 400, 160 );
		JPanel main = new JPanel();
		main.setLayout( new BoxLayout( main, BoxLayout.Y_AXIS ) );
		ComboBoxEntry share = new ComboBoxEntry( "Destination Share:", selected );
		for ( String s : DBAccess.getShareUNCPaths() ) {
			share.addContent( s );
		}
		main.add( share );
		this.add( main, BorderLayout.CENTER );
		this.add( getButtonPanel(), BorderLayout.SOUTH );
	}
	
	public String getShare() {
		return selected.toString();
	}
}