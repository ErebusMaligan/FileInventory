package ui.panels.chassis.export;

import gui.dialog.OKCancelDialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * @author Daniel J. Rivers 2015
 *
 *         Created: Mar 9, 2015, 4:13:01 AM
 */
public class ChassisExportDialog extends OKCancelDialog {

	private static final long serialVersionUID = 1L;

	private JRadioButton all = new JRadioButton( "All", true );
	
	public ChassisExportDialog( Frame owner ) {
		super( owner, "Export Options", true );
		this.setSize( 200, 200 );
		this.setLayout( new BorderLayout() );
		JPanel opt = new JPanel();
		opt.setBorder( BorderFactory.createTitledBorder( "Selection Options" ) );
		opt.setLayout( new GridLayout( 2, 1 ) );
		ButtonGroup bg = new ButtonGroup();
		JRadioButton sel = new JRadioButton( "Selected Only", false );	
		opt.add( all );
		opt.add( sel );
		bg.add( sel );
		bg.add( all );
		this.add( opt, BorderLayout.CENTER );
		this.add( getButtonPanel(), BorderLayout.SOUTH );
	}
	
	public boolean isAll() {
		return all.isSelected();
	}
}