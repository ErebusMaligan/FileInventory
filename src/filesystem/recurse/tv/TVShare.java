package filesystem.recurse.tv;

import java.io.File;

import filesystem.FileProperties;
import filesystem.recurse.FileRecord;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Nov 30, 2013, 2:33:40 AM 
 */
public class TVShare extends FileRecord {

	private static final long serialVersionUID = 1L;
	
	public TVShare( File share, String unc ) {
		super( share, new Object[] { unc } );
//		put( FileProperties.SHARE_TYPE, source ? FileProperties.TV_SOURCE : FileProperties.TV_EPISODE );
		put( FileProperties.DRIVE_SIZE, FileProperties.getDisplaySize( share.getTotalSpace() ) );
	}

	@Override
	protected void createChildren() {
		System.out.println( file.getAbsolutePath() );
		for ( File f : file.listFiles() ) {
			if ( f.isDirectory() && !f.isHidden() && f.list().length > 0 ) {
				addChild( new TVSeries( f, optional ) );
			}
		}
	}
}