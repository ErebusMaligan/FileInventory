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
 * Created: May 4, 2014, 3:20:14 PM 
 */
public class DiskChassisEntryPanel extends EntryPanel {

	private static final long serialVersionUID = 1L;
	
	public DiskChassisEntryPanel() {
		super( DBN.DISK_CHASSIS );
	}
	
	@Override
	protected void defineExceptions() {
		exceptions.add( DBN.SERIAL );
		exceptions.add( DBN.NAME );
	}

	@Override
	protected void handleException( String s ) {
		if ( s.equals( DBN.NAME ) ) {
			ComboBoxEntry name = new ComboBoxEntry( StringUtils.toProperString( s ) + ":  ", props.getVariable( s ) );
			name.setContents( DBAccess.getChassisNames() );
			name.addContent( new String() );
			addEntry( name );
		}
		if ( s.equals( DBN.SERIAL ) ) {
			ComboBoxEntry serial = new ComboBoxEntry( StringUtils.toProperString( s ) + ":  ", props.getVariable( s ) );
			serial.setContents( DBAccess.getDiskSerials() );
			serial.addContent( new String() );
			addEntry( serial );
		}
	}

	@Override
	public String convert( Row r ) {
		return r.getColumn( DBN.NAME ) + " : " + r.getColumn( DBN.SERIAL );
	}

	@Override
	protected void defineProps() {
		props.addVariable( DBN.NAME, new StringVariable() );
		props.addVariable( DBN.SERIAL, new StringVariable() );
		props.addVariable( DBN.BAY, new IntVariable() );
	}
}