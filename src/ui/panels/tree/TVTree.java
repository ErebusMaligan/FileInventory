package ui.panels.tree;

import gui.dialog.OKCancelDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import spreadsheet.data.SpreadSheetData;
import spreadsheet.export.XLSExporter;
import statics.GU;
import ui.icons.IconLoader;
import ui.manager.WindowManager;
import ui.panels.ReplacableComponent;
import ui.panels.tree.export.TreeExportOptionsDialog;
import ui.panels.tree.export.spreadsheet.TreeSSExporter;
import ui.panels.tree.menu.TVSeriesPopup;
import ui.panels.tree.node.TVEpisodeNode;
import ui.panels.tree.node.TVSeasonNode;
import ui.panels.tree.node.TVSeriesNode;
import ui.panels.tree.renderer.TVTreeCellRenderer;
import cache.handler.CacheListener;
import db.DBN;
import db.access.DBAccess;
import db.element.Row;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 12, 2014, 1:52:00 AM 
 */
public class TVTree extends JPanel implements ReplacableComponent, CacheListener {

	private static final long serialVersionUID = 1L;
	
	private DefaultMutableTreeNode root;
	
	private JTree tree;
	
	private JScrollPane scroll = new JScrollPane();
	
	
	public TVTree() {
		DBAccess.getHandler( DBN.TV_SERIES ).addListener( this );
		this.setLayout( new BorderLayout() );
		buildTree();
		this.add( getNorth(), BorderLayout.NORTH );
		scroll.setBorder( BorderFactory.createLineBorder( Color.BLACK, 5 ) );
		this.add( scroll, BorderLayout.CENTER );
	}
	
	private JPanel getNorth() {
		JPanel north = new JPanel();
		north.setLayout( new BorderLayout() );
		JPanel p = new JPanel();
		p.setLayout( new BoxLayout( p, BoxLayout.X_AXIS ) );
		JButton b = new JButton( IconLoader.getInstance().getIcon( IconLoader.REFRESH ) );
		GU.setSizes( b, new Dimension( 36, 36 ) );
		b.setToolTipText( "Refresh Tree" );
		b.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent e ) { buildTree(); } } );
		p.add( b );
		GU.spacer( p );
		b = new JButton( IconLoader.getInstance().getIcon( IconLoader.PRINT ) );
		GU.setSizes( b, new Dimension( 36, 36 ) );
		b.setToolTipText( "Print to Spreadsheet" );
		b.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent e ) { printTree(); } } );
		p.add( b );
		north.add( p, BorderLayout.WEST );
		north.add( Box.createGlue(), BorderLayout.CENTER );
		north.add( Box.createRigidArea( new Dimension( 5, 5 ) ), BorderLayout.SOUTH );
		return north;
	}
	
	private void configureTree() {
		tree.setCellRenderer( new TVTreeCellRenderer() );
		tree.expandRow( 0 );
		tree.setRowHeight( 24 );
		tree.setRootVisible( false );
		tree.setShowsRootHandles( true );
		tree.addMouseListener(  new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent me ) {
				clicked( me );
			}
		} );
		scroll.setViewportView( tree );
	}
	
	private void buildTree() {
		root = new DefaultMutableTreeNode();
		tree = new JTree( root );
		
		//sort series alphabetically
		ArrayList<String> seriesNames = new ArrayList<>();
		for ( Row series : DBAccess.getHandler( DBN.TV_SERIES ).getAllRows() ) {
			seriesNames.add( series.getColumn( "UNC" ).toString() );
		}
		Collections.sort( seriesNames, String.CASE_INSENSITIVE_ORDER );
		//for each series row...
		buildSeries( seriesNames );
		
		configureTree();
	}
	
	private void buildSeries( List<String> seriesNames ) {
		for ( String serName : seriesNames ) { //go through sorted names and add in order
			for ( Row series : DBAccess.getHandler( DBN.TV_SERIES ).getAllRows() ) {
				if ( series.getColumn( "UNC" ).toString().equals( serName ) ) {
					TVSeriesNode serNode = new TVSeriesNode( series );
					DefaultMutableTreeNode share = getShareNode( series.getColumn( DBN.PARENT_UNC ).toString() );
					share.add( serNode );
					//sort season names alphabetically
					ArrayList<String> seasonNames = new ArrayList<>();
					for ( Row season : DBAccess.getSeasonsForSeries( series ) ) {
						seasonNames.add( season.getColumn( "UNC" ).toString() );
					}
					Collections.sort( seasonNames, String.CASE_INSENSITIVE_ORDER );
					//for each season row...
					buildSeasons( seasonNames, series, serNode );
					
					//Set disk share node size here
					break;
				}
			}
		}
	}
	
	private void buildSeasons( List<String> seasonNames, Row series, TVSeriesNode serNode ) {
		for ( String seaName : seasonNames ) { //go through sorted names and add in order
			for ( Row season : DBAccess.getSeasonsForSeries( series ) ) {
				if ( season.getColumn( "UNC" ).toString().equals( seaName ) ) {
					TVSeasonNode seaNode = new TVSeasonNode( season );
					serNode.add( seaNode );
					//sort episode file names alphabetically
					ArrayList<String> episodeNames = new ArrayList<>();
					for ( Row episode : DBAccess.getEpisodesForSeason( season ) ) {
						episodeNames.add( episode.getColumn( "NAME" ).toString() );
					}
					Collections.sort( episodeNames, String.CASE_INSENSITIVE_ORDER  );
					//for each episode row...
					buildEpisodes( episodeNames, season, seaNode );
					
					serNode.setSize( serNode.getSize() + seaNode.getSize() );
					break;
				}
			}
		}
	}
	
	private void buildEpisodes( List<String> episodeNames, Row season, TVSeasonNode seaNode ) {
		for ( String epName : episodeNames ) {  //go through sorted names and add in order
			for ( Row episode : DBAccess.getEpisodesForSeason( season ) ) {
				if ( episode.getColumn( "NAME" ).toString().equals( epName ) ) {
					seaNode.add( new TVEpisodeNode( episode ) );
					if ( episode.getColumn( DBN.SIZE ) != null ) {
						seaNode.setSize( seaNode.getSize() + (Float)episode.getColumn( DBN.SIZE ).getValue() );
					}
					break;
				}
			}
		}
	}
	
	private DefaultMutableTreeNode getShareNode( String unc ) {
		DefaultMutableTreeNode ret = null;
		for ( int i = 0; i < root.getChildCount(); i++ ) {
			DefaultMutableTreeNode n = (DefaultMutableTreeNode)root.getChildAt( i );
			if ( n.getUserObject().equals( unc ) ) {
				ret = n;
				break;
			}
		}
		if ( ret == null ) {
			ret = new DefaultMutableTreeNode( unc );
			root.add( ret );
		}
		return ret;
	}
	
	private void clicked( MouseEvent me ) {
		if ( tree.getSelectionPath() != null ) {
			Object o = tree.getSelectionPath().getLastPathComponent();
			if ( SwingUtilities.isLeftMouseButton( me ) && me.getClickCount() == 2 ) {
				if ( o instanceof TVEpisodeNode ) {
					try {
						Desktop.getDesktop().open( new File( DBAccess.getPathForFile( ( (TVEpisodeNode)o ).getRow() ) ) );
					} catch ( IOException e ) {
						e.printStackTrace();
					}
				}
			} else if ( SwingUtilities.isRightMouseButton( me ) && me.getClickCount() == 1 ) {
	            int row = tree.getClosestRowForLocation( me.getX(), me.getY() );
	            tree.setSelectionRow(row);
	            if ( o instanceof TVSeriesNode ) {
	            	new TVSeriesPopup( ( (TVSeriesNode)o ).getRow() ).show( this, me.getX(), me.getY() );
	            }
			}
		}
	}
	
	@Override
	public void shutdown() {
		DBAccess.getHandler( DBN.TV_SERIES ).removeListener( this );
	}

	@Override
	public void created( Row arg0 ) {
		buildTree();
	}

	@Override
	public void deleted( Row arg0 ) {
		buildTree();
	}

	@Override
	public void updated( Row arg0 ) {
		buildTree();
	}
	
	private void printTree() {
		String[] columns = new String[] { "Share Path", "Series", "Season", "Episode", "Size" };
		TreeExportOptionsDialog dialog = new TreeExportOptionsDialog( WindowManager.getInstance().getPrimaryFrame(), columns );
		dialog.setVisible( true );
		if ( dialog.getResult() == OKCancelDialog.OK ) {
			List<Integer> modList = new ArrayList<Integer>();
			for ( int i = 0; i < columns.length; i++ ) {
				if ( !(Boolean)dialog.getMap().get( columns[ i ] ).getValue() ) {
					modList.add( i );
				}
			}
			boolean colors = (Boolean)dialog.getMap().get( TreeExportOptionsDialog.USE_COLORS ).getValue();
			boolean full = (Boolean)dialog.getMap().get( TreeExportOptionsDialog.FULL_SHEET ).getValue();
			boolean sub = (Boolean)dialog.getMap().get( TreeExportOptionsDialog.SUB_SHEET ).getValue();
			List<SpreadSheetData> ss = new ArrayList<SpreadSheetData>();
			if ( sub ) {
				ss.add( TreeSSExporter.getOverview( root, columns, colors ) );
			}
			if ( full ) {
				ss.add( TreeSSExporter.getFullSpreadSheet( root, columns, modList, colors, "Series Tree" ) );
			}
			if ( sub ) {
				ss.addAll( TreeSSExporter.getSeriesSheets( root, columns, colors ) );
			}
			XLSExporter.exportSpreadSheet( WindowManager.getInstance().getPrimaryFrame(), ss, null );
		}
	}
}