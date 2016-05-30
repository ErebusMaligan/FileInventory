package ui.panels.chassis;

import gui.dialog.OKCancelDialog;

import java.awt.BorderLayout;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.manager.WindowManager;
import db.DBN;
import db.access.DBAccess;
import db.element.Row;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 26, 2014, 5:25:47 PM 
 */
public class SharePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JButton set = new JButton();
	
	private Row disk;
	
	private DiskPanel dp;
	
	private Row share;
	
	public SharePanel( DiskPanel dp ) {
		this.dp = dp;
		this.setLayout( new BorderLayout() );
		set.setEnabled( false );
		set.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				ShareSelectionDialog d = new ShareSelectionDialog( WindowManager.getInstance().getPrimaryFrame() );
				d.setLocation( MouseInfo.getPointerInfo().getLocation() );
				d.setVisible( true );
				if ( d.getResult() == OKCancelDialog.OK ) {
						setShare( d.getShareRow(), false );
				}
			}
		} );
		set.setLayout( new BorderLayout() );
		set.setBorder( BorderFactory.createTitledBorder( "Share Info" ) );
		this.add( set, BorderLayout.CENTER );
	}
	
	public void setDisk( Row disk ) {
		this.disk = disk;
		if ( disk != null ) {
			set.setEnabled( true );
			setShare( DBAccess.getShareForDisk( disk ), true );
		} else {
			set.setEnabled( false );
			setShare( null, true );
		}
	}
	
	public void setShare( Row share, boolean initial ) {
		set.removeAll();
		JPanel p = new JPanel( new BorderLayout() );
		p.setOpaque( false );
		if ( share != null ) {
			p.add( new JLabel( share.getColumn( DBN.NAME ).toString(), JLabel.CENTER ), BorderLayout.CENTER );
		} else {
			p.add( new JLabel( "NO SHARE", JLabel.CENTER ), BorderLayout.CENTER );
		}
//		GU.spacer( set );
		set.add( p, BorderLayout.CENTER );
		if ( !initial ) {
			DBAccess.handleShareDiskMapping( disk, share );
		}
		this.share = share;
//		revalidate();
		dp.setDisk( disk, false, false );
	}
	
	public Row getShare() {
		return share;
	}
}