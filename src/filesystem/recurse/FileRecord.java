package filesystem.recurse;

import java.io.File;

import filesystem.FileProperties;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Nov 30, 2013, 3:59:02 AM 
 */
public abstract class FileRecord extends GenericRecord {

	private static final long serialVersionUID = 1L;

	protected File file;
	
	protected Object[] optional;
	
	public FileRecord( File file ) {
		this( file, null );
	}
	
	public FileRecord( File file, Object[] optional ) {
		super();
		if ( optional != null ) {
			this.optional = optional;
		}
		this.file = file;
		createChildren();
		put( FileProperties.NAME, file.getName() );
		put( FileProperties.SIZE, FileProperties.getDisplaySize( getLength() ) );
	}
	
	protected abstract void createChildren();
	
	protected float getLength() {
		float len = 0;
		for ( GenericRecord r : children ) {
			len += ( (FileRecord)r ).getLength();
		}
		return len;
	}
}