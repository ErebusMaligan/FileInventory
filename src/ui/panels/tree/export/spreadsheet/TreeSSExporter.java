package ui.panels.tree.export.spreadsheet;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import spreadsheet.data.SpreadSheetData;
import spreadsheet.data.SpreadSheetEditor;
import spreadsheet.export.CellStyleData;
import ui.panels.tree.node.TVEpisodeNode;
import ui.panels.tree.node.TVSeasonNode;
import ui.panels.tree.node.TVSeriesNode;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Jul 29, 2014, 4:32:12 PM 
 */
public class TreeSSExporter {
	
	private static Color ORANGE = new Color( 255, 150, 0 );
	private static Color RED = new Color( 185, 0, 0 );
	private static Color GREEN = new Color( 0, 150, 10 );
	private static Color BLUE = new Color( 35, 150, 165 );
	private static String EMPTY = null;
	
	public static SpreadSheetData getFullSpreadSheet( DefaultMutableTreeNode root, String[] columns, List<Integer> modList, boolean useColors, String sheetName ) {
		SpreadSheetData data = new SpreadSheetData( sheetName, columns );
//		if ( useColors ) {
//			data.setBackground( Color.BLACK );
//		}
		for ( int share = 0; share < root.getChildCount(); share++ ) {  //share
			DefaultMutableTreeNode shareNode = (DefaultMutableTreeNode)root.getChildAt( share );
			if ( !modList.contains( 0 ) ) {
				CellStyleData style = new CellStyleData();
				style.BG = ORANGE;
				style.FG = Color.BLACK;
				data.addRow( new String[] { shareNode.getUserObject().toString(), EMPTY, EMPTY, EMPTY, EMPTY }, useColors ? style : null );
			}
			addSeries( data, shareNode, modList, useColors );
		}
		SpreadSheetEditor.removeColumnsByIndex( modList, data );
		SpreadSheetEditor.trimBlankRows( data );
		return data;
	}
	
	private static void addSeries( SpreadSheetData data, DefaultMutableTreeNode shareNode, List<Integer> modList, boolean useColors ) {
		for ( int series = 0; series < shareNode.getChildCount(); series++ ) {  //series
			TVSeriesNode seriesNode = (TVSeriesNode)shareNode.getChildAt( series );
			if ( !modList.contains( 1 ) ) {
				CellStyleData style = new CellStyleData();
				style.BG = RED;
				style.FG = Color.WHITE;
				data.addRow( new String[] { EMPTY, seriesNode.getName(), EMPTY, EMPTY, seriesNode.getDispalySize() }, useColors ? style : null );
			}
			addSeasons( data, seriesNode, modList, useColors );
		}
	}
	
	private static void addSeasons( SpreadSheetData data, TVSeriesNode seriesNode, List<Integer> modList, boolean useColors ) {
		for ( int season = 0; season < seriesNode.getChildCount(); season++ ) { //season
			TVSeasonNode seasonNode = (TVSeasonNode)seriesNode.getChildAt( season );
			if ( !modList.contains( 2 ) ) {
				CellStyleData style = new CellStyleData();
				style.BG = BLUE;
				style.FG = Color.WHITE;
				data.addRow( new String[] { EMPTY, EMPTY, seasonNode.getName(), EMPTY, seasonNode.getDispalySize() }, useColors ? style : null );
			}
			addEpisodes( data, modList, seasonNode, useColors );
		}
	}
	
	private static void addEpisodes( SpreadSheetData data, List<Integer> modList, TVSeasonNode seasonNode, boolean useColors ) {
		for ( int episode = 0; episode < seasonNode.getChildCount(); episode++ ) {  //episode
			TVEpisodeNode episodeNode = (TVEpisodeNode)seasonNode.getChildAt( episode );
			if ( !modList.contains( 3 ) ) {
				CellStyleData style = new CellStyleData();
				style.BG = GREEN;
				style.FG = Color.WHITE;
				data.addRow( new String[] { EMPTY, EMPTY, EMPTY, episodeNode.getName(), episodeNode.getDispalySize() }, useColors ? style : null );
			}
		}
	}
	
	public static SpreadSheetData getOverview( DefaultMutableTreeNode root, String[] columns, boolean useColors ) {
		List<Integer> modList = new ArrayList<Integer>();
		modList.add( 2 );
		modList.add( 3 );
		return getFullSpreadSheet( root, columns, modList, useColors, "Overview" );
	}
	
	public static List<SpreadSheetData> getSeriesSheets( DefaultMutableTreeNode root, String[] columns, boolean useColors ) {
		List<SpreadSheetData> ret = new ArrayList<SpreadSheetData>();
		
		columns = new String[] { columns[ 2 ], columns[ 3 ], columns[ 4 ] };
		
		for ( int share = 0; share < root.getChildCount(); share++ ) {  //share
			DefaultMutableTreeNode shareNode = (DefaultMutableTreeNode)root.getChildAt( share );
			
			for ( int series = 0; series < shareNode.getChildCount(); series++ ) {  //series
				TVSeriesNode seriesNode = (TVSeriesNode)shareNode.getChildAt( series );
					
				SpreadSheetData data = new SpreadSheetData( seriesNode.getName() + " " + seriesNode.getDispalySize(), columns );
//				if ( useColors ) {
//					data.setBackground( Color.BLACK );
//				}
				
				for ( int season = 0; season < seriesNode.getChildCount(); season++ ) { //season
					TVSeasonNode seasonNode = (TVSeasonNode)seriesNode.getChildAt( season );
					CellStyleData style = new CellStyleData();
					style.BG = BLUE;
					style.FG = Color.WHITE;
					data.addRow( new String[] { seasonNode.getName(), EMPTY, seasonNode.getDispalySize() }, useColors ? style : null );
						
					for ( int episode = 0; episode < seasonNode.getChildCount(); episode++ ) {  //episode
						TVEpisodeNode episodeNode = (TVEpisodeNode)seasonNode.getChildAt( episode );
						CellStyleData s2 = new CellStyleData();
						s2.BG = GREEN;
						s2.FG = Color.WHITE;
						data.addRow( new String[] { EMPTY, episodeNode.getName(), episodeNode.getDispalySize() }, useColors ? s2 : null );
					}
				}
				ret.add( data );
			}
		}
		return ret;
	}
}