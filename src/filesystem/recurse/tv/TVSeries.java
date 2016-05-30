package filesystem.recurse.tv;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db.DBN;
import filesystem.recurse.FileRecord;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Nov 30, 2013, 1:45:13 AM 
 */
public class TVSeries extends FileRecord {

	private static final long serialVersionUID = 1L;

	public TVSeries( File directory,  Object[] optional ) {
		super( directory, new String[] {  ( (String)optional[ 0 ] ) + "\\" + directory.getName() } );
		put( DBN.PARENT_UNC, (String)optional[ 0 ] );
		put( DBN.UNC, ( (String)optional[ 0 ] ) + "\\" + directory.getName() );
		
//		put( FileProperties.SEASONS, String.valueOf( children.size() ) );
	}
	
	@Override
	protected void createChildren() {
		List<File> arr = new ArrayList<File>();
		List<String> names = new ArrayList<String>();
		for ( File f : file.listFiles() ) {
			if ( f.isDirectory() ) {
				if ( f.getName().contains( "Season" ) || f.getName().contains( "season" ) || f.getName().contains( "SEASON" ) ) {
					arr.add( f );
					names.add( f.getName() );
				}
			}
		}
		Collections.sort( names );
		for ( String s : names ) {
			for ( File f : arr ) {
				if ( f.getName().equals( s ) ) {
					addChild( new TVSeason( f, optional ) );
					break;
				}
			}
		}
	}
}