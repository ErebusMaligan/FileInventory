package scan;

import java.io.File;

import ui.manager.WindowManager;
import db.DBN;
import db.access.DBAccess;
import db.converter.MovieShareConverter;
import db.converter.TVShareConverter;
import db.element.Row;
import filesystem.recurse.movie.MovieShare;
import filesystem.recurse.tv.TVShare;
import gui.dialog.BusyDialog;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Jul 30, 2014, 10:29:19 PM 
 */
public class ScanExecutor {
	
	public static void scan() {
		new BusyDialog( WindowManager.getInstance().getPrimaryFrame() ) {
			private static final long serialVersionUID = 1L;
			@Override
			public void executeTask() {
				for ( Row r : DBAccess.getCurScans() ) {
					try {
						String letter = r.getColumn( DBN.LOCAL_MAPPING ).toString();
						String type = r.getColumn( DBN.SCAN_TYPE ).toString();
						if ( type.equals( DBN.MOVIE ) ) {
							scanMovie( letter );
						} else if ( type.equals( DBN.TV_SERIES ) ) {
							scanTV( letter );
						}
					} catch ( Exception e ) {
						e.printStackTrace();
					}
				}
			}
		};
	}
	
	public static void scanTV( String l ) {
		TVShareConverter tsc = new TVShareConverter();
		tsc.writeRecords( tsc.convertToDB( new TVShare( new File( l ), l ) ) );
	}
	
	public static void scanMovie( String l ) {
		MovieShareConverter msc = new MovieShareConverter();
		msc.writeRecords( msc.convertToDB( new MovieShare( new File( l ), l ) ) );
	}
}