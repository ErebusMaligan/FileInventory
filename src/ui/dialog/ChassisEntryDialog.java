package ui.dialog;

import gui.dialog.OKCancelDialog;

import java.awt.BorderLayout;
import java.awt.Frame;

import ui.panels.entry.ChassisEntryPanel;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Jun 1, 2014, 4:38:52 PM 
 */
public class ChassisEntryDialog extends OKCancelDialog {

	private static final long serialVersionUID = 1L;

	public ChassisEntryDialog( Frame owner ) {
		super( owner, "Chassis Entry Dialog", true );
		this.setLayout( new BorderLayout() );
		this.add( new ChassisEntryPanel(), BorderLayout.CENTER );
		this.setDefaultCloseOperation( DISPOSE_ON_CLOSE );
		this.setSize( 600, 500 );
	}
}