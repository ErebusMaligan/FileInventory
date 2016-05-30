package ui.dialog;

import gui.dialog.BusyDialog;
import gui.dialog.OKCancelDialog;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import statics.GU;
import ui.manager.WindowManager;
import db.access.DBAccess;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Apr 19, 2014, 2:14:05 AM 
 */
public abstract class ScanAsTypeDialog extends OKCancelDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JComboBox<String> unc = new JComboBox<String>();
	
	/**
	 * @param owner
	 * @param title
	 * @param modal
	 */
	public ScanAsTypeDialog( Frame owner ) {
		super( owner, "Scan Content as Movies", true );
		setSize( 400, 140 );
		setLayout( new BorderLayout() );
		DBAccess.getShareUNCPaths().forEach( s -> unc.addItem( s ) );
		ok.setText( "Scan" );
		JPanel main = new JPanel();
		main.setLayout( new BoxLayout( main, BoxLayout.Y_AXIS ) );
		JLabel lbl = new JLabel( "Path:" );
		lbl.setHorizontalAlignment( SwingConstants.RIGHT );
		GU.hp( main, GU.SPACER, GU.FIELD, lbl, unc );
		this.add( main, BorderLayout.CENTER );
		this.add( getButtonPanel(), BorderLayout.SOUTH );
	}
	
	public void ok() {
		super.ok();
		new BusyDialog( WindowManager.getInstance().getPrimaryFrame() ) {
			private static final long serialVersionUID = 1L;
			@Override
			public void executeTask() {
				scan( unc.getItemAt( unc.getSelectedIndex() ) );
			}
		};
	}
	
	protected abstract void scan( String u );
}