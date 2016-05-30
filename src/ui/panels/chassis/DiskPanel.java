package ui.panels.chassis;

import gui.dialog.OKCancelDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import statics.GU;
import ui.manager.WindowManager;
import db.DBN;
import db.access.DBAccess;
import db.element.Row;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 26, 2014, 2:41:11 AM 
 */
public class DiskPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private int bay;
	
	private Row chassis;
	
	private SharePanel sp = new SharePanel( this );
	
	private JButton set = new JButton();
	
	public DiskPanel( Row chassis, int bay ) {
		this.chassis = chassis;
		this.bay = bay;
		this.setLayout( new BorderLayout() );
		set.setLayout( new BorderLayout() );
		setDisk( DBAccess.getDiskRowForBay( chassis, bay ), true );
		set.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				DiskSelectionDialog d = new DiskSelectionDialog( WindowManager.getInstance().getPrimaryFrame() );
				d.setLocation( MouseInfo.getPointerInfo().getLocation() );
				d.setVisible( true );
				if ( d.getResult() == OKCancelDialog.OK ) {
					setDisk( null, false ); //handles unmapping the original one if necessary
					setDisk( d.getDiskRow(), false );
					
				}
			}
		} );
		set.setBorder( BorderFactory.createTitledBorder( "Disk Info" ) );
		this.add( sp, BorderLayout.NORTH );
		this.add( set, BorderLayout.CENTER );
		int b = 5;
		this.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createEmptyBorder( b, b, b, b ), BorderFactory.createCompoundBorder( BorderFactory.createTitledBorder( "Bay " + bay ), BorderFactory.createBevelBorder( BevelBorder.RAISED ) ) ) );
	}
	
	private void setDisk( Row disk, boolean initial ) {
		setDisk( disk, initial, true );
	}
	
	public void setDisk( Row disk, boolean initial, boolean setSP ) {
		set.removeAll();
		if ( disk != null ) {
//			DiskData dd = new DiskData( disk, bay );
			
			//disk info
			JPanel p = new JPanel();
			p.setOpaque( false );
			p.setLayout( new GridLayout( 7, 1 ) );
			JLabel c = new JLabel( disk.getColumn( DBN.SIZE ).toString() + " TB", SwingConstants.CENTER );
			c.setFont( GU.deriveFont( c, 15, Font.BOLD ) );
			p.add( c );
			p.add( new JLabel( disk.getColumn( DBN.MANUFACTURER ).toString() + " " + disk.getColumn( DBN.TYPE ).toString(), SwingConstants.CENTER ) );
			p.add( new JLabel( disk.getColumn( DBN.SERIAL ).toString(), SwingConstants.CENTER ) );
			p.add( Box.createGlue() );
			p.add( Box.createGlue() );
			p.add( new JLabel( disk.getColumn( DBN.NOTES ).toString(), SwingConstants.CENTER ) );
			p.add( Box.createGlue() );
			set.add( p, BorderLayout.CENTER );
			
			//progress bar stuff
			Row share = sp.getShare();
			if ( share != null ) {
				File f = new File( share.getColumn( DBN.UNC ).toString() );
				long offset = 1073741824;
				long max = f.getTotalSpace() / offset;
				long free = f.getUsableSpace() / offset;
				double used = max - free;
				int percent = (int)(used / max * 100);
				JProgressBar b = new JProgressBar( 0, (int)max );
				b.setValue( (int)used );
				String info =  (long)used + " / " + max + " GiB  (" + percent + "%)";
				b.setString( info );
				b.setStringPainted( true );
				b.setToolTipText( info );
				GU.setSizes( b, new Dimension( p.getSize().width, GU.FIELD.height ) ); 
				b.setBorder( BorderFactory.createLineBorder( percent > 24 ? percent > 49 ? percent > 74 ? Color.RED : Color.ORANGE : Color.YELLOW : Color.GREEN, 2 ) );
				set.add( b, BorderLayout.SOUTH );
			}
		} else {
			set.add( new JLabel( "NO DISK", SwingConstants.CENTER ), BorderLayout.CENTER );
		}
		if ( !initial ) {
			DBAccess.handleDiskChassisMapping( disk, chassis, bay );
		}
		if ( setSP ) {
			sp.setDisk( disk );
		}
		revalidate();
	}
}