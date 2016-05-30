package ui.panels.entry;

import gui.props.variable.FloatVariable;
import gui.props.variable.StringVariable;

import java.util.Arrays;
import java.util.List;

import db.DBN;
import db.element.Row;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 4, 2014, 3:05:09 PM 
 */
public class DiskEntryPanel extends EntryPanel {

	private static final long serialVersionUID = 1L;

	public DiskEntryPanel() {
		super( DBN.DISK );
	}
	
	@Override
	protected void defineProps() {
		props.addVariable( DBN.MANUFACTURER, new StringVariable() );
		props.addVariable( DBN.TYPE, new StringVariable() );
		props.addVariable( DBN.SERIAL, new StringVariable() );
		props.addVariable( DBN.MODEL, new StringVariable() );
		props.addVariable( DBN.SIZE, new FloatVariable() );
		props.addVariable( DBN.NOTES, new StringVariable() );
	}
	
	protected List<String> getNames() {
		return Arrays.asList( new String[] { DBN.MANUFACTURER, DBN.TYPE, DBN.MODEL, DBN.SERIAL, DBN.SIZE, DBN.NOTES } );
	}
	
	@Override
	public String convert( Row r ) {
		return r.getColumn( DBN.SERIAL ).toString() + " : " + r.getColumn( DBN.MODEL ).toString();
	}
}