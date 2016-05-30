package filesystem.recurse.movie;

import java.io.File;

import statics.FileUtils;
import db.DBN;
import filesystem.FileProperties;
import filesystem.recurse.FileRecord;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 4, 2014, 10:42:11 PM 
 */
public class Movie extends FileRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param file
	 */
	public Movie( File file ) {
		super( file );
		put( DBN.DISC_COUNT, Integer.toString( children.size() ) );
		put( DBN.SIZE, Float.toString( this.getLength() ) );
	}
	
	@Override
	protected void createChildren() {
		for ( File f : file.listFiles() ) {
			for ( String s : FileProperties.MOVIE_TYPES ) {
				if ( s.equalsIgnoreCase( FileUtils.getSuffix( f ) ) ) {
					children.add( new FileRecord( f ) {

						private static final long serialVersionUID = 1L;

						protected void createChildren() {
							//none
						}
						
						@Override
						public float getLength() {
							return file.length();
						}
					} );
					put( DBN.FILE_TYPE, s );
					break;
				}
			}
		}
	}
}