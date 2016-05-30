package ui.panels.entry;

import gui.entry.Entry;
import gui.props.UIEntryProps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import statics.GU;
import statics.StringUtils;
import ui.panels.ReplacableComponent;
import ui.renderer.RowCellRenderer;
import ui.renderer.RowComparator;
import ui.renderer.RowConverter;
import cache.DataSourceProxy;
import cache.handler.CacheHandler;
import cache.handler.CacheListener;
import db.converter.UIEntryPropsConverter;
import db.element.Row;
import db.element.utils.RowUtils;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Apr 30, 2014, 3:54:22 PM 
 */
public abstract class EntryPanel extends JPanel implements RowConverter, CacheListener, ReplacableComponent {
	
	private static final long serialVersionUID = 1L;

	protected CacheHandler handler;
	
	protected UIEntryProps props;
	
	protected JComboBox<Row> rowSelection;
	
	protected JPanel center;
	
	protected List<Entry> entries;
	
	protected List<String> exceptions;
	
	protected Map<String, String> defaults;
	
	protected String table;
	
	protected JButton delete;
	
	protected JPanel buttonPanel;
	
	protected UIEntryPropsConverter pConvert;

	public EntryPanel( String table ) {
		this.table = table;
		handler = DataSourceProxy.getInstance().getCache().getHandler( table );
		handler.addListener( this );
		center = new JPanel();
		entries = new ArrayList<Entry>();
		exceptions = new ArrayList<String>();
		defaults = new HashMap<>();
		props = new UIEntryProps();
		pConvert = new UIEntryPropsConverter();
		defineProps();
		for ( String s : props.getNames() ) {
			defaults.put( s, props.getString( s ) );
		}
		defineExceptions();
		constructPanel();
	}
	
	protected void defineExceptions() {	
	}
	
	protected abstract void defineProps();
	
	protected void constructPanel() {
		this.setLayout( new BorderLayout() );
		this.add( getNorthPanel(), BorderLayout.NORTH );
		buildCenterPanel();
		this.add( center, BorderLayout.CENTER );
		buttonPanel = getButtonPanel();
		this.add( buttonPanel, BorderLayout.SOUTH );
	}
	
	protected List<String> getNames() {
		return props.getNames();
	}
	
	protected void buildCenterPanel() {
		center.setLayout( new BoxLayout( center, BoxLayout.Y_AXIS ) );
		center.add( Box.createVerticalGlue() );
		for ( String s : getNames() ) {
			if ( !exceptions.contains( s ) ) {
				Entry e = new Entry( StringUtils.toProperString( s ) + ":  ", props.getVariable( s ) );	
				addEntry( e );
			} else {
				handleException( s );
			}
		}
		center.add( Box.createVerticalGlue() );
	}
	
	protected void addEntry( Entry e ) {
		entries.add( e );
		center.add( e );
		GU.spacer( center, new Dimension( 20, 20 ) );
		e.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );
	}
	
	protected void handleException( String s ) {
		//implement specifics in subclass if applicable
	}

	protected JPanel getNorthPanel() {
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
		Vector<Row> rows = handler.getAllRows();
		rows.sort( new RowComparator( this ) );
		for ( Row r : rows ) {
			rowSelection.addItem( r );
		}
		GU.hp( p, GU.SPACER, GU.LONG, new JLabel( "View Row: ", JLabel.RIGHT ), rowSelection );
		rowSelection.setSelectedIndex( -1 );
		rowSelection.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				if ( rowSelection.getSelectedIndex() == -1 ) {
					for ( String s : defaults.keySet() ) {
						props.getVariable( s ).fromString( defaults.get( s ) );
					}
					delete.setEnabled( false );
				} else {
					delete.setEnabled( true );
					pConvert.setProps( rowSelection.getItemAt( rowSelection.getSelectedIndex() ), props );
				}
				reloadEntries();
			}
		} );
		p.setBorder( BorderFactory.createLineBorder( Color.BLACK, 2 ) );
		return p;
	}
	
	protected JPanel getButtonPanel() {
		JPanel p = new JPanel();
		JButton merge = new JButton( "Create/Update" );
		merge.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent e ) { merge(); } } );
		delete = new JButton( "Delete" );
		delete.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent e ) { delete(); } } );
		delete.setEnabled( false );
		GU.hp( p, merge, delete );
		p.setBorder( BorderFactory.createLineBorder( Color.BLACK, 2 ) );
		return p;
	}
	
	protected void loadSelection() {
		
	}
	
	protected void merge() {
		handler.merge( pConvert.convertProps( props ) );
	}
	
	protected void delete() {
		if ( JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog( this, "This will permanently remove this record from the database", "Are you sure?", JOptionPane.OK_CANCEL_OPTION ) ) {
			handler.delete( rowSelection.getItemAt( rowSelection.getSelectedIndex() ) );
		}
	}
	
	protected void reloadEntries() {
		for ( Entry e : entries ) {
			e.reload();
		}
	}

	@Override
	public void created( Row row ) {
		Row z = rowSelection.getItemAt( rowSelection.getSelectedIndex() );
		rowSelection.addItem( row );
		if ( z != null ) {
			for ( int i = 1; i < rowSelection.getItemCount(); i++ ) {
				if ( RowUtils.pkEqual( z, rowSelection.getItemAt( i ), DataSourceProxy.getInstance().getCache().getTableDefinition( z.getName() ) ) ) {
					rowSelection.setSelectedIndex( i );
					break;
				}
			}
		}
		reloadEntries();
	}

	@Override
	public void deleted( Row row ) {
		Row r = null;
		for ( int i = 1; i < rowSelection.getItemCount(); i++ ) {
			if ( RowUtils.pkEqual( row, rowSelection.getItemAt( i ), DataSourceProxy.getInstance().getCache().getTableDefinition( row.getName() ) ) ) {
				r = rowSelection.getItemAt( i );
				break;
			}
		}
		rowSelection.removeItem( r );
		reloadEntries();
	}

	@Override
	public void updated( Row row ) {
		Row r = null;
		for ( int i = 1; i < rowSelection.getItemCount(); i++ ) {
			if ( RowUtils.pkEqual( row, rowSelection.getItemAt( i ), DataSourceProxy.getInstance().getCache().getTableDefinition( row.getName() ) ) ) {
				r = rowSelection.getItemAt( i );
				break;
			}
		}
		rowSelection.removeItem( r );
		created( row );
	}	
	
	@Override
	public void shutdown() {
		handler.removeListener( this );
	}
	
	public Row getSelectedRow() {
		return rowSelection.getItemAt( rowSelection.getSelectedIndex() );
	}
}