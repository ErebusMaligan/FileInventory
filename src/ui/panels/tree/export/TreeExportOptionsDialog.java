package ui.panels.tree.export;

import gui.dialog.OKCancelDialog;
import gui.entry.CheckEntry;
import gui.props.variable.BooleanVariable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import statics.GU;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Jul 20, 2014, 6:40:35 PM 
 */
public class TreeExportOptionsDialog extends OKCancelDialog {

	private static final long serialVersionUID = 1L;
	
	public static String USE_COLORS = "Use Colors";
	
	public static String FULL_SHEET = "FULL_SHEET";
	
	public static String SUB_SHEET = "SUB_SHEET";
	
	private Map<String, BooleanVariable> map = new HashMap<String, BooleanVariable>();
	
	public TreeExportOptionsDialog( Frame owner, String[] columns ) {
		super( owner, "Choose Export Options", true );
		this.setSize( new Dimension( 425, 275 ) );
		JPanel center = new JPanel();
		center.setLayout( new BoxLayout( center, BoxLayout.Y_AXIS ) );
		this.setLayout( new BorderLayout() );
		this.add( center, BorderLayout.CENTER );
		this.add( getButtonPanel(), BorderLayout.SOUTH );
		
		JPanel col = new JPanel();
		col.setBorder( BorderFactory.createTitledBorder( "Columns on Full Sheet" ) );
		col.setLayout( new BoxLayout( col, BoxLayout.X_AXIS ) );
		for ( String s : columns ) {
			BooleanVariable var = new BooleanVariable( true );
			map.put( s, var );
			col.add( new CheckEntry( s, var ) );
			GU.spacer( col );
		}
		center.add( col );
		GU.spacer( center );
		
		JPanel form = new JPanel();
		form.setBorder( BorderFactory.createTitledBorder( "Format Options for All Sheets" ) );
		form.setLayout( new BoxLayout( form, BoxLayout.X_AXIS ) );
		for ( String s : new String[] { USE_COLORS } ) {
			BooleanVariable var = new BooleanVariable( true );
			map.put( s, var );
			form.add( new CheckEntry( s, var ) );
		}
		center.add( form );
		GU.spacer( center );
		
		JPanel sheets = new JPanel();
		sheets.setBorder( BorderFactory.createTitledBorder( "Sheets to Include" ) );
		sheets.setLayout( new BoxLayout( sheets, BoxLayout.X_AXIS ) );
		for ( String s : new String[] { FULL_SHEET, SUB_SHEET } ) {
			BooleanVariable var = new BooleanVariable( true );
			map.put( s, var );
			sheets.add( new CheckEntry( s, var ) );
		}
		center.add( sheets );
	}
	
	public Map<String, BooleanVariable> getMap() {
		return map;
	}
}