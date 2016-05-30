package ui.dialog;

import gui.dialog.OKCancelDialog;

import java.awt.BorderLayout;
import java.awt.Frame;

import ui.panels.entry.DiskEntryPanel;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Jun 1, 2014, 4:38:52 PM 
 */
public class DiskEntryDialog extends OKCancelDialog {

	private static final long serialVersionUID = 1L;

	public DiskEntryDialog( Frame owner ) {
		super( owner, "Disk Entry Dialog", true );
		this.setLayout( new BorderLayout() );
		this.add( new DiskEntryPanel(), BorderLayout.CENTER );
		this.setDefaultCloseOperation( DISPOSE_ON_CLOSE );
		this.setSize( 600, 500 );
	}
}