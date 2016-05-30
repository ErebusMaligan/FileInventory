package ui.panels.chassis.export;

import java.awt.Color;
import java.util.List;

import spreadsheet.data.SpreadSheetData;
import spreadsheet.export.CellStyleData;

/**
 * @author Daniel J. Rivers
 *         2015
 *
 * Created: Mar 8, 2015, 11:55:33 PM 
 */
public class ChassisSSExporter {
	
//	private static Color ORANGE = new Color( 255, 150, 0 );
//	private static Color RED = new Color( 185, 0, 0 );
//	private static Color GREEN = new Color( 0, 150, 10 );
//	private static Color BLUE = new Color( 35, 150, 165 );
//	private static Color MAGENTA = new Color( 255, 0, 255 );
//	private static Color YELLOW = new Color( 255, 255, 0 );
//	private static Color NULL = null;
//	private static String EMPTY = null;

	private static String[] columns = new String[] { "Location", "Bay", "Name", "Size (TB)", "MFT", "Type", "MDL", "SN", "Notes", "Usable Space (GiB)", "Free Space (GiB)", "% Full" };
	
	public static SpreadSheetData createSheet( List<ChassisData> data ) {
		SpreadSheetData ret = createSheet();
		Object[] r = newRow();
		r[ 0 ] = "TOTALS";
		r[ 3 ] = 0f;
		r[ 9 ] = 0l;
		r[ 10 ] = 0l;
		r[ 11 ] = 0;
		for ( ChassisData s : data ) {
			Object[] cb = createChassisBlock( ret, s );
			r[ 3 ] = (float)r[ 3 ] + (float)cb[ 3 ]; 
			r[ 9 ] = (long)r[9] + (long)cb[9];
			r[ 10 ] = (long)r[10] + (long)cb[10];
		}
		r[ 11 ] = (int)( ( (double)( (long)r[9] - (long)r[10] ) ) / (long)r[9] * 100l );
		ret.addRow( newRow() );
		CellStyleData style = new CellStyleData();
		style.BG = Color.BLACK;
		ret.addRow( newRow(), style );
		ret.addRow( r );
		return ret;
	}
	
	private static SpreadSheetData createSheet() {
		SpreadSheetData ret = new SpreadSheetData( "Chassis", columns );
		CellStyleData style = new CellStyleData();
		style.BG = Color.BLACK;
		ret.addRow( newRow(), style );
		return ret;
	}
	
	private static Object[] newRow() {
		return new Object[ columns.length ];
	}
	
	private static Object[] createChassisBlock( SpreadSheetData data, ChassisData server ) {
		Object[] r = newRow();
		r[ 0 ] = server.getChassisID();
		CellStyleData style = new CellStyleData();
		style.BG = Color.BLACK;
		style.FG = Color.WHITE;
		style.BOLD = true;
		data.addRow( r, style );
		data.addRow( newRow() );
		for ( DiskData d : server.getDisks() ) {
			createDiskRow( data, d );
		}
		data.addRow( newRow(), style );
		r = newRow();
		r[ 3 ] = server.getSize();
		r[ 9 ] = server.getMax();
		r[ 10 ] = server.getFree();
		r[ 11 ] = server.getPercent();
		data.addRow( r );
		data.addRow( newRow() );
		style = new CellStyleData();
		style.BG = Color.LIGHT_GRAY;
		data.addRow( newRow(), style );
		return r;
	}
	
	private static void createDiskRow( SpreadSheetData data, DiskData d ) {
		Object[] r = newRow();
		r[ 1 ] = d.getBay();
		if ( !d.isEmpty() ) {
			r[ 3 ] = d.getSize();
			r[ 4 ] = d.getMFT();
			r[ 5 ] = d.getType();
			r[ 6 ] = d.getMDL();
			r[ 7 ] = d.getSN();
			r[ 8 ] = d.getNotes();
			if ( d.hasShare() ) {
				r[ 2 ] = d.getName();
				r[ 9 ] = d.getMax();
				r[ 10 ] = d.getFree();
				r[ 11 ] = d.getPercent();
			}
		}
		data.addRow( r );
	}
}