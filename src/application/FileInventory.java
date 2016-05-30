package application;

import java.sql.SQLException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ui.frame.PrimaryFrame;
import ui.manager.WindowManager;
import db.DataStore;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Nov 28, 2013, 6:11:22 AM 
 */
public class FileInventory {

	public static void main( String[] args ) throws ClassNotFoundException, SQLException {

		if ( args.length > 0 ) {
			if ( args[ 0 ].equals( "-debug" ) ) {
				ApplicationConstants.DEBUG_MODE = true;
			}
		}
		
		DataStore.getInstance().initDB( "Inventory.db" );

		

		//		JFrame f = new MountTest();
		//		f.setVisible( true );
		//		for ( String s : DBAccess.getInstance().getShareUNCPaths() ) {
		//			System.err.println( s );
		//		}
		//		DataSourceProxy.getInstance().getCache().printCache();
		
		startUI();
	}

	public static void startUI() {
		try {
//			UIManager.setLookAndFeel("net.sourceforge.napkinlaf.NapkinLookAndFeel");
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		} catch ( ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e ) {
			e.printStackTrace();
		}
		WindowManager.getInstance().setPrimaryFrame( new PrimaryFrame() );
	}
}