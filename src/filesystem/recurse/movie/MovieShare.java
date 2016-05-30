package filesystem.recurse.movie;

import java.io.File;

import db.DBN;
import filesystem.recurse.FileRecord;
import filesystem.recurse.GenericRecord;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 4, 2014, 9:52:30 PM 
 */
public class MovieShare extends FileRecord {

	private static final long serialVersionUID = 1L;

	/**
	 * @param file
	 */
	public MovieShare( File file, String UNC ) {
		super( file );
		for ( GenericRecord c : children ) {
			c.put( DBN.UNC, UNC );
		}
	}

	@Override
	protected void createChildren() {
		for ( File f : file.listFiles() ) {
			if ( f.isDirectory() ) {
				if ( !( f.getName().contains( "System Volume" ) || f.getName().contains( "$" ) ) ) {
					children.add( new Movie( f ) );
				}
			}
		}
	}
}