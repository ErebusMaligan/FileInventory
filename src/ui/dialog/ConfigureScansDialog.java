package ui.dialog;

import gui.dialog.OKCancelDialog;
import gui.entry.ComboBoxEntry;
import gui.props.variable.StringVariable;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;

import statics.GU;
import ui.renderer.RowCellRenderer;
import cache.handler.CacheListener;
import db.DBN;
import db.access.DBAccess;
import db.element.Row;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Jul 30, 2014, 6:47:57 PM 
 */
public class ConfigureScansDialog extends OKCancelDialog implements CacheListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private StringVariable l = new StringVariable();
	
	private StringVariable t = new StringVariable();
	
	private ComboBoxEntry unc;
	
	private ComboBoxEntry types;
	
	private DefaultListModel<Row> model = new DefaultListModel<Row>();
	
	private JList<Row> list = new JList<Row>( model );
	
	public ConfigureScansDialog( Frame owner ) {
		super( owner, "Configure Scans",  true );
		this.setLayout( new BorderLayout() );
		this.add( getScanPanel(), BorderLayout.NORTH );
		this.add( getCurScansPanel(), BorderLayout.CENTER );
//		this.add( getButtonPanel(), BorderLayout.SOUTH );
		this.setDefaultCloseOperation( DISPOSE_ON_CLOSE );
		this.addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosed( WindowEvent e ) {
				DBAccess.getHandler( DBN.SCANS ).removeListener( ConfigureScansDialog.this );
			}
		} );
		this.setSize( 600, 500 );
		reloadList();
		DBAccess.getHandler( DBN.SCANS ).addListener( this );
	}
	
	private JPanel getScanPanel() {
		JPanel p = new JPanel();
		unc = new ComboBoxEntry( "Share Path", l );
		DBAccess.getShareUNCPaths().forEach( s -> unc.addContent( s ) );
		types = new ComboBoxEntry( "Scan Type", t );
		for ( String s : new String[] { DBN.TV_SERIES, DBN.MOVIE } ) {
			types.addContent( s );
		}
		p.setLayout( new BoxLayout( p, BoxLayout.Y_AXIS ) );
		p.add( unc );
		GU.spacer( p );
		p.add( types );
		GU.spacer( p );
		JButton add = new JButton( "Add Scan" );
		add.addActionListener( ( e ) ->	DBAccess.enterScan( l.toString(), t.toString() ) );
		p.add( add );
		return p;
	}
	
	private JPanel getCurScansPanel() {
		JPanel p = new JPanel();
		list.setCellRenderer( new RowCellRenderer( r -> { return r.getColumn( DBN.LOCAL_MAPPING ).toString() + " -> " + r.getColumn( DBN.SCAN_TYPE ).toString(); } ) );
		p.setLayout( new BorderLayout() );
		list.setBorder( BorderFactory.createTitledBorder( "Current Scans" ) );
		p.add( list, BorderLayout.CENTER );
		JPanel b = new JPanel();
		b.setLayout( new BoxLayout( b, BoxLayout.X_AXIS ) );
		JButton rem = new JButton( "Remove" );
		rem.addActionListener( new ActionListener() { 
			public void actionPerformed( ActionEvent e ) {
				if ( list.getSelectedIndex() != -1 ) {
					DBAccess.removeScan( list.getSelectedValue() );
				}
			}
		} );
		b.add( rem );
		p.add( b, BorderLayout.SOUTH );
		return p;
	}
	
	private void reloadList() {
		model.clear();
		for ( Row r : DBAccess.getCurScans() ) {
			model.addElement( r );
		}
	}

	@Override
	public void created( Row arg0 ) {
		reloadList();
	}

	@Override
	public void deleted( Row arg0 ) {
		reloadList();
	}

	@Override
	public void updated( Row arg0 ) {
		reloadList();
	}
}