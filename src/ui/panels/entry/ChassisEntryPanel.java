package ui.panels.entry;

import gui.entry.ComboBoxEntry;
import gui.props.variable.IntVariable;
import gui.props.variable.StringVariable;
import statics.StringUtils;
import db.DBN;
import db.access.DBAccess;
import db.element.Row;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Apr 30, 2014, 10:49:45 PM 
 */
public class ChassisEntryPanel extends EntryPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param table
	 */
	public ChassisEntryPanel() {
		super( DBN.CHASSIS );
	}
	
	@Override
	protected void defineExceptions() {
		exceptions.add( DBN.IP );
	}
	
	@Override
	protected void defineProps() {
		props.addVariable( DBN.NAME, new StringVariable() );
		props.addVariable( DBN.IP, new StringVariable() );
		props.addVariable( DBN.BAYS, new IntVariable() );
	}
	
	@Override
	protected void handleException( String s ) {
		if ( s.equals( DBN.IP ) ) {
			ComboBoxEntry ips = new ComboBoxEntry( StringUtils.toProperString( s ) + ":  ", props.getVariable( s ) );
			ips.setContents( DBAccess.getIPs() );
			ips.addContent( new String() );
			addEntry( ips );
		}
	}

	@Override
	public String convert( Row r ) {
		return r.getColumn( DBN.NAME ).toString() + " (" + r.getColumn( DBN.IP ) + ")";
	}
}