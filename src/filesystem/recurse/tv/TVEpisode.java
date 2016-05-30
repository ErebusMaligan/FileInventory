package filesystem.recurse.tv;

import java.io.File;

import statics.FileUtils;
import db.DBN;
import filesystem.recurse.FileRecord;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Nov 30, 2013, 2:51:16 AM 
 */
public class TVEpisode extends FileRecord {

	private static final long serialVersionUID = 1L;
	
	public TVEpisode( File file, Object[] optional ) {
		super( file, optional );
		put( DBN.PARENT_UNC, (String)optional[ 0 ] );
		put( DBN.FILE_TYPE, FileUtils.getSuffix( file ) );
		put( DBN.NAME, file.getName().substring( 0, file.getName().lastIndexOf( '.' ) ) );
		put( DBN.SIZE, Float.toString( this.getLength() ) );
	}

	@Override
	public float getLength() {
		return file.length();
	}
	
	//no children
	@Override
	protected void createChildren() {	
	}
}