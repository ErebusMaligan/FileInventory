package ui.panels.tree.renderer;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;

import ui.panels.tree.node.TVEpisodeNode;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 12, 2014, 10:47:04 PM 
 */
public class TVEpisodeTreeCellRenderer extends ExtendedTreeCellRenderer {

	private static final long serialVersionUID = 1L;

	private Map<String, Icon> ft = new HashMap<String, Icon>();
	
	public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus ) {
		super.getTreeCellRendererComponent( tree, value, selected, expanded, leaf, row, hasFocus );
		TVEpisodeNode node = (TVEpisodeNode)value;
		String type = node.getType();
		if ( !ft.containsKey( type ) ) {
			try {
				File f = File.createTempFile( "icon", "." + type );
				ft.put( type, FileSystemView.getFileSystemView().getSystemIcon( f ) );
				f.delete();
			} catch ( IOException e ) {
				e.printStackTrace();
			}
		}
		valueLabel.setText( node.getLabelText() );
		expandedLabel.setIcon( ft.get( type ) );
		return this;
	}
}