package ui.panels.entry;

import gui.entry.ComboBoxEntry;
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
public class ShareDiskEntryPanel extends EntryPanel {

	private static final long serialVersionUID = 1L;

	private ComboBoxEntry serial;
	
	public ShareDiskEntryPanel() {
		super( DBN.SHARE_DISK );
	}
	
	@Override
	public String convert( Row r ) {
		return r.getColumn( DBN.UNC ) + " : " + r.getColumn( DBN.SERIAL );
	}

	@Override
	protected void defineProps() {
		props.addVariable( DBN.UNC, new StringVariable() );
		props.addVariable( DBN.SERIAL, new StringVariable() );
	}
	
	@Override
	protected void defineExceptions() {
		exceptions.add( DBN.UNC );
		exceptions.add( DBN.SERIAL );
	}

	@Override
	protected void handleException( String s ) {
		if ( s.equals( DBN.UNC ) ) {
			ComboBoxEntry name = new ComboBoxEntry( StringUtils.toProperString( s ) + ":  ", props.getVariable( s ) );
			name.setContents( DBAccess.getShareUNCPaths() );
			name.addContent( new String() );
			addEntry( name );
		}
		if ( s.equals( DBN.SERIAL ) ) {
			serial = new ComboBoxEntry( StringUtils.toProperString( s ) + ":  ", props.getVariable( s ) );
			serial.setContents( DBAccess.getDiskSerials() );
			serial.addContent( new String() );
			addEntry( serial );
		}
	}
}