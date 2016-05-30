package filesystem.recurse.tv;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db.DBN;
import filesystem.FileProperties;
import filesystem.recurse.FileRecord;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Nov 30, 2013, 4:45:49 AM 
 */
public class TVSeason extends FileRecord {
	
	private static final long serialVersionUID = 1L;
	
	public TVSeason( File directory, Object[] optional ) {
		super( directory, new String[] {  ( (String)optional[ 0 ] ) + "\\" + directory.getName() } );
		put( DBN.PARENT_UNC, (String)optional[ 0 ] );
		put( DBN.UNC, ( (String)optional[ 0 ] ) + "\\" + directory.getName() );
	}

	@Override
	protected void createChildren() {
		List<File> arr = new ArrayList<File>();
		List<String> names = new ArrayList<String>();
		for ( File f : file.listFiles() ) {
			if ( f.isFile() ) {
				if ( FileProperties.isType( f.getName(), FileProperties.EPISODE_TYPES ) ) {
					arr.add( f );
					names.add( f.getName() );
				}
			}
		}
		Collections.sort( names );
		for ( String s : names ) {
			
			System.out.println( s );
			
			for ( File f : arr ) {
				if ( f.getName().equals( s ) ) {
					addChild( new TVEpisode( f, optional ) );
//					break;
				}
			}
		}
	}
}