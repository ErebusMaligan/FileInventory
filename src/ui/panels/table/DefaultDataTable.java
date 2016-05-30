package ui.panels.table;

import gui.table.AbstractTableImplementation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import spreadsheet.converter.TableModelConverter;
import spreadsheet.export.XLSExporter;
import statics.GU;
import ui.icons.IconLoader;
import ui.manager.WindowManager;
import ui.panels.ReplacableComponent;
import cache.DataSourceProxy;
import cache.handler.CacheHandler;
import cache.handler.CacheListener;
import db.element.Row;
import db.element.column.ColumnData;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 6, 2014, 10:59:51 PM 
 */
public class DefaultDataTable extends AbstractTableImplementation implements ReplacableComponent, CacheListener {

	protected JPanel main = new JPanel();
	
	protected CacheHandler handler;
	
	protected String[] columnNames;
	
	public DefaultDataTable( String tableName ) {
		this( tableName, null );
	}
	
	public DefaultDataTable( String tableName, String[] columnNames ) {
		handler = DataSourceProxy.getInstance().getCache().getHandler( tableName );
		if ( columnNames == null ) {
			Set<String> def = DataSourceProxy.getInstance().getCache().getTableDefinition( tableName ).getDefinition().getValues().keySet();
			this.columnNames = new String[ def.size() ];
			def.toArray( this.columnNames );
		} else {
			this.columnNames = columnNames;
		}
		main = new JPanel();
		main.setLayout( new BorderLayout() );
		table.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
		JPanel north = new JPanel();
		JPanel p = new JPanel();
		p.setLayout( new BoxLayout( p, BoxLayout.X_AXIS ) );
		JButton b = new JButton( IconLoader.getInstance().getIcon( IconLoader.PRINT ) );
		GU.setSizes( b, new Dimension( 36, 36 ) );
		b.setToolTipText( "Print to Spreadsheet" );
		b.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent e ) { export(); } } );
		p.add( b );
		north.setLayout( new BorderLayout() );
		north.add( p, BorderLayout.NORTH );
		north.add( table.getTableHeader(), BorderLayout.SOUTH );
		main.add( north, BorderLayout.NORTH );
		main.add( new JScrollPane( table ), BorderLayout.CENTER );
		init( true );
	}
	
	@Override
	protected void addAsListener() {
		handler.addListener( this );
	}

	@Override
	protected Object[] convertRow( int i ) {
		Row r = (Row)data.get( i );
		Map<String, ColumnData> cd = r.getValues();
		Object[] row = new Object[ columnNames.length ];
		for ( int q = 0; q < columnNames.length; q++ ) {
			row[ q ] = cd.get( columnNames[ q ] ).toString();
		}
		return row;
	}

	@Override
	protected Object[] getColumnNames() {
		return columnNames;
	}

	@Override
	protected void pullSpecificData() {
		if ( data == null ) {
			data = new Vector<Object>();
		}
		data.clear();
		data.addAll( handler.getAllRows() );
	}

	@Override
	public void shutdown() {
		handler.removeListener( this );
	}
	
	public JPanel getPrimaryPanel() {
		return main;
	}

	@Override
	public void created( Row arg0 ) {
		pullData();
	}

	@Override
	public void deleted( Row arg0 ) {
		pullData();
	}

	@Override
	public void updated( Row arg0 ) {
		pullData();
	}
	
	public void export() {
		XLSExporter.exportSpreadSheet( WindowManager.getInstance().getPrimaryFrame(), TableModelConverter.getSpreadSheetData( new DefaultTableModel( this.convertData(), columnNames ), handler.getName() ), null );
	}
}