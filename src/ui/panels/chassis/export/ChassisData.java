package ui.panels.chassis.export;

import java.util.ArrayList;
import java.util.List;

import db.DBN;
import db.access.DBAccess;
import db.element.Row;

/**
 * @author Daniel J. Rivers
 *         2015
 *
 * Created: Mar 8, 2015, 10:43:32 PM 
 */
public class ChassisData {
	
	private String name, IP, UNC;

	private List<DiskData> disks = new ArrayList<>();
	
	private long max = 0l;
	private long free = 0l;
	private double used;
	private int percent = 0;
	private float size = 0f;
	
	public ChassisData( Row chassis ) {
		this.name = chassis.getColumn( DBN.NAME ).toString();
		this.IP = chassis.getColumn( DBN.IP ).toString();
		Row netSource = DBAccess.getNetworkSourceFromIP( IP );
		if ( netSource != null ) {
			this.UNC = DBAccess.getNetworkSourceFromIP( IP ).getColumn( DBN.UNC ).toString();
		}
		int bays = (Integer)chassis.getColumn( DBN.BAYS ).getValue();
		for ( int i = 1; i <= bays; i++ ) {
			Row r = DBAccess.getDiskRowForBay( chassis, i );
			DiskData d = new DiskData( r, i );
			disks.add( d );
			max = max + d.getMax();
			free = free + d.getFree();
			size = size + d.getSize();
		}
		used = max - free;
		percent = (int)(used / max * 100);
	}
	
	public String getChassisID() {
		return name + " [" + UNC + "] " + "(" + IP + ")";
	}
	
	@Override
	public String toString() {
		String ret = name + "\n";
		for ( DiskData d : disks ) {
			ret += d.toString() + "\n";
		}
		return ret;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the iP
	 */
	public String getIP() {
		return IP;
	}

	/**
	 * @return the uNC
	 */
	public String getUNC() {
		return UNC;
	}

	/**
	 * @return the disks
	 */
	public List<DiskData> getDisks() {
		return disks;
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
	 * @return the percent
	 */
	public int getPercent() {
		return percent;
	}
	
	public float getSize() {
		return size;
	}
	
	
}