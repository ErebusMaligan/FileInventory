package ui.panels.chassis;

import gui.dialog.OKCancelDialog;
import gui.layout.WrapLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import spreadsheet.export.XLSExporter;
import statics.GU;
import ui.dialog.ChassisEntryDialog;
import ui.dialog.DiskEntryDialog;
import ui.icons.IconLoader;
import ui.manager.WindowManager;
import ui.panels.chassis.export.ChassisData;
import ui.panels.chassis.export.ChassisExportDialog;
import ui.panels.chassis.export.ChassisSSExporter;
import ui.panels.entry.EntryPanel;
import ui.renderer.RowCellRenderer;
import db.DBN;
import db.element.Row;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 25, 2014, 10:48:53 PM 
 */
public class ChassisEditorPanel extends EntryPanel {

	private static final long serialVersionUID = 1L;

	public ChassisEditorPanel() {
		super( DBN.CHASSIS );
		for ( ActionListener l : rowSelection.getActionListeners() ) {  //remove the default actionlistener
			rowSelection.removeActionListener( l );
		}
		rowSelection.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				buildCenterPanel();
				revalidate();
			}
		} );
	}
	
	@Override
	protected JPanel getNorthPanel() {
		JPanel north = new JPanel( new BorderLayout() );
		JPanel p = new JPanel();
		p.setLayout( new BoxLayout( p, BoxLayout.Y_AXIS ) );
		JLabel title = new JLabel( table );
		JPanel t = new JPanel();
		t.add( title );
		title.setFont( GU.deriveFont( title, 18, Font.BOLD ) );
		p.add( t );
		GU.spacer( p );
		rowSelection = new JComboBox<Row>();
		rowSelection.setRenderer( new RowCellRenderer( this ) );
		rowSelection.addItem( null );
		for ( Row r : handler.getAllRows() ) {
			rowSelection.addItem( r );
		}
		GU.hp( p, GU.SPACER, GU.LONG, new JLabel( "View Chassis: ", JLabel.RIGHT ), rowSelection );
		rowSelection.setSelectedIndex( -1 );
		rowSelection.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				if ( rowSelection.getSelectedIndex() == -1 ) {
					for ( String s : defaults.keySet() ) {
						props.getVariable( s ).fromString( defaults.get( s ) );
					}
				} else {
					pConvert.setProps( rowSelection.getItemAt( rowSelection.getSelectedIndex() ), props );
				}
				reloadEntries();
			}
		} );
		north.add( p, BorderLayout.NORTH );
		
		JPanel s = new JPanel();
		s.setLayout( new BoxLayout( s, BoxLayout.X_AXIS ) );
		JButton b = new JButton( IconLoader.getInstance().getIcon( IconLoader.DOWN_HD ) );
		GU.setSizes( b, new Dimension( 36, 36 ) );
		b.setToolTipText( "Define Disks" );
		b.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				new DiskEntryDialog( WindowManager.getInstance().getPrimaryFrame() ).setVisible( true );
			}
		} );
		s.add( b );
		GU.spacer( s );
		
		JButton b2 = new JButton( "Define Chassis" );
		b2.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				new ChassisEntryDialog( WindowManager.getInstance().getPrimaryFrame() ).setVisible( true );
			}
		} );
		s.add( b2 );
		GU.spacer( s );
		
		JButton b3 = new JButton( IconLoader.getInstance().getIcon( IconLoader.PRINT ) );
		GU.setSizes( b3, new Dimension( 36, 36 ) );
		b3.setToolTipText( "Print to Spreadsheet" );
		b3.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent e ) { export(); } } );
		s.add( b3 );
		
		north.add( s, BorderLayout.SOUTH );
		
		north.setBorder( BorderFactory.createLineBorder( Color.BLACK, 2 ) );
		return north;
	}
	
	@Override
	protected void constructPanel() {
		this.setLayout( new BorderLayout() );
		this.add( getNorthPanel(), BorderLayout.NORTH );
		buildCenterPanel();
		center.setLayout( new WrapLayout() );
		JScrollPane scroll = new JScrollPane( center );
		scroll.getVerticalScrollBar().setUnitIncrement( 16 );
		this.add( scroll, BorderLayout.CENTER );
	}

	@Override
	protected void buildCenterPanel() {
		center.removeAll();
		if ( rowSelection.getSelectedIndex() != -1 ) {
			int bays = (Integer)getSelectedRow().getColumn( DBN.BAYS ).getValue();
			for ( int i = 0; i < bays; i++ ) {
				JPanel p = new DiskPanel( getSelectedRow(), i + 1 );
				GU.setSizes( p, new Dimension( 200, 230 ) );
				center.add( p );
			}
		}
	}
	
	@Override
	public String convert( Row r ) {
		return r.getColumn( DBN.NAME ).toString() + " (" + r.getColumn( DBN.IP ) + ")";
	}

	@Override
	protected void defineProps() {
		//doing nothing here
	}
	
	private void export() {
		ChassisExportDialog d = new ChassisExportDialog( WindowManager.getInstance().getPrimaryFrame() );
		d.setLocation( MouseInfo.getPointerInfo().getLocation() );
		d.setVisible( true );
		if ( d.getResult() == OKCancelDialog.OK ) {
			List<ChassisData> l = new ArrayList<>();
			if ( d.isAll() ) {
				for ( Row r : handler.getAllRows() ) {
					l.add( new ChassisData( r ) ); 
				}
			} else {
				Row current = rowSelection.getItemAt( rowSelection.getSelectedIndex() );
				if ( current != null ) {
					l.add( new ChassisData( current ) );
				}
			}
			XLSExporter.exportSpreadSheet( WindowManager.getInstance().getPrimaryFrame(), ChassisSSExporter.createSheet( l ), null );
		}
	}
}