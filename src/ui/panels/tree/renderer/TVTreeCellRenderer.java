package ui.panels.tree.renderer;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import ui.panels.tree.node.TVEpisodeNode;
import ui.panels.tree.node.TVSeasonNode;
import ui.panels.tree.node.TVSeriesNode;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 13, 2014, 12:10:15 AM 
 */
public class TVTreeCellRenderer extends ExtendedTreeCellRenderer {

	private static final long serialVersionUID = 1L;
	
	private ShareTreeCellRenderer share = new ShareTreeCellRenderer();
	
	private TVSeriesTreeCellRenderer series = new TVSeriesTreeCellRenderer();
	
	private TVSeasonTreeCellRenderer season = new TVSeasonTreeCellRenderer();
	
	private TVEpisodeTreeCellRenderer episode = new TVEpisodeTreeCellRenderer();
	
	public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus ) {
		Component ret = super.getTreeCellRendererComponent( tree, value, selected, expanded, leaf, row, hasFocus );
		if ( value instanceof TVSeriesNode ) {
				ret = series.getTreeCellRendererComponent( tree, value, selected, expanded, leaf, row, hasFocus );
		} else if ( value instanceof TVSeasonNode ) {
			ret = season.getTreeCellRendererComponent( tree, value, selected, expanded, leaf, row, hasFocus );
		} else if ( value instanceof TVEpisodeNode ) {
			ret = episode.getTreeCellRendererComponent( tree, value, selected, expanded, leaf, row, hasFocus );
		} else if ( value instanceof DefaultMutableTreeNode ) {
			ret = share.getTreeCellRendererComponent( tree, value, selected, expanded, leaf, row, hasFocus );
		}
		return ret;
	}	

}
