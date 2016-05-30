package ui.panels.tree.menu;

import gui.menubar.GenericMenuBarAction;
import gui.menubar.GenericPopupMenu;
import db.access.DBAccess;
import db.element.Row;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 21, 2014, 11:04:24 PM 
 */
public class TVSeriesPopup extends GenericPopupMenu {

	private static final long serialVersionUID = 1L;

	public TVSeriesPopup( final Row series ) {
		createItem( "Move Series", new GenericMenuBarAction() {
			@Override
			public void execute( Object executor ) {				
				DBAccess.moveSeries( series );
			}
		} );
		createItem( "Delete Series", new GenericMenuBarAction() {
			@Override
			public void execute( Object executor ) {				
				DBAccess.deleteSeries( series );
			}
		} );
	}	
}