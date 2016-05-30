package ui.menubar;

import gui.dialog.BusyDialog;
import gui.menubar.GenericMenuBar;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

import net.NetUtils;
import scan.ScanExecutor;
import statics.GU;
import statics.StringUtils;
import ui.dialog.ConfigureScansDialog;
import ui.dialog.ScanAsTypeDialog;
import ui.manager.WindowManager;
import ui.menubar.action.TableViewAction;
import ui.panels.chassis.ChassisEditorPanel;
import ui.panels.entry.DiskChassisEntryPanel;
import ui.panels.entry.ShareDiskEntryPanel;
import ui.panels.table.MovieDataTable;
import ui.panels.tree.TVTree;
import application.ApplicationConstants;
import db.DBN;
import db.DataStore;
import db.access.DBAccess;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Apr 19, 2014, 12:38:58 AM 
 */
public class PrimaryMenuBar extends GenericMenuBar {

	private static final long serialVersionUID = 1L;

	private Map<String, JMenu> menus = new HashMap<String, JMenu>();

	public PrimaryMenuBar() {
//		try {
//			DataStore.getInstance().create( false );
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		}
		createScan();
		createView();
		createAdmin();
		if ( ApplicationConstants.DEBUG_MODE ) {
			createDebug();
		}
	}

	private void createDebug() {
		JMenu debug = setMenu( getMenu( "Debug" ) );
		createItem( "Print Data", e -> DataStore.getInstance().printData() );
		
		JMenu tables = new JMenu ( "Tables" );
		debug.add( setMenu( tables ) );

		createItem( StringUtils.toProperString( DBN.LOG ), new TableViewAction( DBN.LOG, new String[] { DBN.TIME, DBN.MSG, DBN.CATEGORY } ) );
		
		JMenu hardware = new JMenu( "Hardware" );
		setMenu( hardware );
		createItem( StringUtils.toProperString( DBN.CHASSIS ), new TableViewAction( DBN.CHASSIS, new String[] { DBN.IP, DBN.NAME, DBN.BAYS } ) );
		createItem( StringUtils.toProperString( DBN.DISK_CHASSIS ), new TableViewAction( DBN.DISK_CHASSIS ) );
		createItem( StringUtils.toProperString( DBN.DISK ), new TableViewAction( DBN.DISK ) );
		
		JMenu net = new JMenu( "Network" );
		setMenu( net );
		createItem( StringUtils.toProperString( DBN.NETWORK_SOURCE ), new TableViewAction( DBN.NETWORK_SOURCE, new String[] { DBN.IP, DBN.UNC, DBN.SHARES } ) );
		createItem( StringUtils.toProperString( DBN.NETWORK_SHARE ), new TableViewAction( DBN.NETWORK_SHARE, new String[] { DBN.UNC, DBN.NAME, DBN.LOCAL_MAPPING, DBN.CONTENT_TYPE } ) );
		
		JMenu content = new JMenu( "Content" );
		setMenu( content );
		
		String[] movieCol = new String[] { DBN.NAME, DBN.PARENT_UNC, DBN.SIZE, DBN.FILE_TYPE, DBN.DISC_COUNT };
		createItem( StringUtils.toProperString( DBN.MOVIE ), new TableViewAction( DBN.MOVIE, movieCol, new MovieDataTable( DBN.MOVIE, movieCol ) ) );
		createItem( StringUtils.toProperString( DBN.TV_SERIES ), new TableViewAction( DBN.TV_SERIES, new String[] { DBN.UNC, DBN.EXPECTED } ) );
		createItem( StringUtils.toProperString( DBN.TV_SEASON ), new TableViewAction( DBN.TV_SEASON, new String[] { DBN.UNC, DBN.EXPECTED } ) );
		createItem( StringUtils.toProperString( DBN.TV_EPISODE ), new TableViewAction( DBN.TV_EPISODE, new String[] { DBN.NAME, DBN.PARENT_UNC, DBN.FILE_TYPE, DBN.SIZE } ) );
		
		
		JMenu map = new JMenu( "Mapping" );
		debug.add( map );
		setMenu( map );
		createItem( StringUtils.toProperString( DBN.DISK ) + " -> " + StringUtils.toProperString( DBN.CHASSIS ), e -> WindowManager.getInstance().getPrimaryFrame().setCenterComponent( new DiskChassisEntryPanel() ) );
		createItem( StringUtils.toProperString( DBN.NETWORK_SHARE ) + " -> " + StringUtils.toProperString( DBN.DISK ), e -> WindowManager.getInstance().getPrimaryFrame().setCenterComponent( new ShareDiskEntryPanel() ) );
		tables.add( hardware );
		tables.add( new JSeparator() );
		tables.add( net );
		tables.add( new JSeparator() );
		tables.add( content );
		
		
	}
	
	private void createScan() {
		setMenu( getMenu( "Scan" ) );
		createItem( "Scan Network...", e -> {
			DBAccess.log( "Network Scan Started...", DBN.LL.INFO );
			new BusyDialog( WindowManager.getInstance().getPrimaryFrame() ) {
				private static final long serialVersionUID = 1L;
				@Override
				public void executeTask() {
					NetUtils.scanNetworkShares();
					DBAccess.log( "Network Scan Completed.", DBN.LL.INFO );
					
				}
			};
		} );
		createItem( "Scan as TV Episodes...",  e -> {
			new ScanAsTypeDialog( WindowManager.getInstance().getPrimaryFrame() ) {
				private static final long serialVersionUID = 1L;
				@Override
				public void scan( String u ) {
					ScanExecutor.scanTV( u );
				}
			}.setVisible( true );
		} );
		createItem( "Scan as Movies...", e -> {
			new ScanAsTypeDialog( WindowManager.getInstance().getPrimaryFrame() ) {
				private static final long serialVersionUID = 1L;
				@Override
				public void scan( String u ) {
					ScanExecutor.scanMovie( u );
				}
			}.setVisible( true );
		} );
		menu.add( new JSeparator() );
		createItem( "Configure Pre-Set Scans", e -> new ConfigureScansDialog( WindowManager.getInstance().getPrimaryFrame() ).setVisible( true ) );
		createItem( "Execute Pre-Set Scans", e -> ScanExecutor.scan() );
	}

	private void createAdmin() {
		JMenu admin = setMenu( getMenu( "Admin" ) );
		setMenu( admin );
		createItem( "Reset Data", ex -> {
			if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog( WindowManager.getInstance().getPrimaryFrame(), "Reset Data", "This will clear all data.  Are you sure?", JOptionPane.YES_NO_OPTION ) ) {
				try {
					DataStore.getInstance().create( true );
					DBAccess.log( "Database Data Dropped/Schema Rebuilt.", DBN.LL.INFO );
				} catch ( ClassNotFoundException | SQLException e ) {
					e.printStackTrace();
				}
			}
		} );
	}
	
	private void createView() {
		setMenu( getMenu( "Data" ) );
		createItem( "Chassis Editor", e -> WindowManager.getInstance().getPrimaryFrame().setCenterComponent( new ChassisEditorPanel() ) );
		createItem( "TV Tree", e -> WindowManager.getInstance().getPrimaryFrame().setCenterComponent( new TVTree() ) );
		String[] movieCol = new String[] { DBN.NAME, DBN.PARENT_UNC, DBN.SIZE, DBN.FILE_TYPE, DBN.DISC_COUNT };
		createItem( StringUtils.toProperString( DBN.MOVIE ), new TableViewAction( DBN.MOVIE, movieCol, new MovieDataTable( DBN.MOVIE, movieCol ) ) );
	}

	public JMenu getMenu( String s ) {
		if ( !menus.containsKey( s ) ) {
			menus.put( s, new JMenu( s ) );
			this.add( menus.get( s ) );
			GU.spacer( this );
		}
		return menus.get( s );
	}
}