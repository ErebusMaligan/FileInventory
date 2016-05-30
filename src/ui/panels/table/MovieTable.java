package ui.panels.table;

import gui.table.AbstractTableImplementation;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ui.panels.ReplacableComponent;
import cache.DataSourceProxy;
import cache.handler.CacheHandler;
import cache.handler.CacheListener;
import db.DBN;
import db.element.Row;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 6, 2014, 10:59:51 PM 
 */
public class MovieTable extends AbstractTableImplementation implements ReplacableComponent, CacheListener {

	private JPanel main = new JPanel();
	
	private CacheHandler handler;
	
	public MovieTable() {
		handler = DataSourceProxy.getInstance().getCache().getHandler( DBN.MOVIE );
		main = new JPanel();
		main.setLayout( new BorderLayout() );
		table.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
		main.add( table.getTableHeader(), BorderLayout.NORTH );
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
		Object[] row = new Object[] {
				r.getColumn( DBN.NAME ).toString(),
				r.getColumn( DBN.PARENT_UNC ).toString(),
				r.getColumn( DBN.SIZE ).toString()
		};
		return row;
	}

	@Override
	protected Object[] getColumnNames() {
		return new String[] { DBN.NAME, DBN.PARENT_UNC, DBN.SIZE };
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
}