package ui.panels.tree.renderer;

import java.awt.Component;

import javax.swing.JTree;

import ui.panels.tree.node.TVSeasonNode;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 12, 2014, 10:47:04 PM 
 */
public class TVSeasonTreeCellRenderer extends ExtendedTreeCellRenderer {

	private static final long serialVersionUID = 1L;

	public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus ) {
		super.getTreeCellRendererComponent( tree, value, selected, expanded, leaf, row, hasFocus );
		valueLabel.setText( ( (TVSeasonNode)value ).getLabelText() );
		return this;
	}
}