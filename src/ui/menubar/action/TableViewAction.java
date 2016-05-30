package ui.menubar.action;

import gui.menubar.GenericMenuBarAction;
import ui.manager.WindowManager;
import ui.panels.table.DefaultDataTable;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 11, 2014, 8:06:51 PM 
 */
public class TableViewAction implements GenericMenuBarAction {

	private String tableName;
	
	private String[] columnNames;
	
	private DefaultDataTable ddt;
	
	public TableViewAction( String tableName ) {
		this( tableName, null );
	}
	
	public TableViewAction( String tableName, String[] columnNames ) {
		this.tableName = tableName;
		this.columnNames = columnNames;
	}
	
	public TableViewAction( String tableName, String[] columnNames, DefaultDataTable ddt ) {
		this( tableName, columnNames );
		this.ddt = ddt;
	}
	
	@Override
	public void execute( Object executor ) {
		if ( ddt == null ) {
			ddt =  new DefaultDataTable( tableName, columnNames );
		}
		WindowManager.getInstance().getPrimaryFrame().setCenterComponent( ddt.getPrimaryPanel() );
	}
}