package filesystem.recurse;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Nov 29, 2013, 5:54:21 AM 
 * @param <K>
 * @param <V>
 */
public abstract class GenericRecord extends HashMap<String, String> {

	private static final long serialVersionUID = 1L;
	
	protected List<GenericRecord> children = new Vector<GenericRecord>();
	
	public GenericRecord() {
		super();
	}

	public List<GenericRecord> getChildren() {
		return children;
	}
	
	public void addChild( GenericRecord r ) {
		children.add( r );
	}
	
	public void getInfo() {
		String out = "";
		for ( String s : keySet() ) {
			out += get( s ) + "  ";
		}
		System.out.println( out );
		for ( GenericRecord r : children ) {
			r.getInfo();
		}
	}
}