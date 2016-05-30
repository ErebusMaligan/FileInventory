package ui.panels.chassis.export;

import java.io.File;

import net.NetUtils;
import db.DBN;
import db.access.DBAccess;
import db.element.Row;

/**
 * @author Daniel J. Rivers
 *         2015
 *
 * Created: Mar 8, 2015, 10:46:50 PM 
 */
public class DiskData {
	private static long OFFSET = 1073741824;
	private int bay, percent;
	private float size;
	private String name, mft, mdl, sn, type, notes;
	private long max, free;
	private double used;
	private boolean empty = true;
	private boolean hasShare = false;
	
	public DiskData( Row disk, int bay ) {
		this.bay = bay;
		if ( disk != null ) {
			empty = false;
			mft = disk.getColumn( DBN.MANUFACTURER ).toString();
			mdl = disk.getColumn( DBN.MODEL ).toString();
			sn = disk.getColumn( DBN.SERIAL ).toString();
			size = (Float)disk.getColumn( DBN.SIZE ).getValue();
			type = disk.getColumn( DBN.TYPE ).toString();
			notes = disk.getColumn( DBN.NOTES ).toString();
			Row share = DBAccess.getShareForDisk( disk );
			if ( share != null ) {
				hasShare = true;
				name = share.getColumn( DBN.UNC ).toString();
				File f = new File( name );
				name = NetUtils.stripUNCEnd( name );
				max = f.getTotalSpace() / OFFSET;
				free = f.getUsableSpace() / OFFSET;
				used = max - free; //note the promotion to double
				percent = (int)(used / max * 100l); //double makes the whole calculation double and then int... if used is not double, result is 0
			}
		}
	}
	
	@Override
	public String toString() {
		return bay + "\t" +  name + "\t" + size + "\t" + mft  + "\t" + type + "\t" + mdl  + "\t" + sn + "\t" + max + "\t" + used + "\t" + free + "\t" + percent;
	}

	public String getType() {
		return type;
	}
	
	/**
	 * @return the bay
	 */
	public int getBay() {
		return bay;
	}

	/**
	 * @return the percent
	 */
	public int getPercent() {
		return percent;
	}

	/**
	 * @return the size
	 */
	public float getSize() {
		return size;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the mft
	 */
	public String getMFT() {
		return mft;
	}

	/**
	 * @return the mdl
	 */
	public String getMDL() {
		return mdl;
	}

	/**
	 * @return the sn
	 */
	public String getSN() {
		return sn;
	}

	/**
	 * @return the max
	 */
	public long getMax() {
		return max;
	}

	/**
	 * @return the free
	 */
	public long getFree() {
		return free;
	}

	/**
	 * @return the used
	 */
	public double getUsed() {
		return used;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public boolean hasShare() {
		return hasShare;
	}
	
	public boolean isEmpty() {
		return empty;
	}
}