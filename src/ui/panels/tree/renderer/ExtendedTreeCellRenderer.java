package ui.panels.tree.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.tree.TreeCellRenderer;

import statics.GU;
import ui.icons.IconLoader;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: May 12, 2014, 8:22:39 PM 
 */
public class ExtendedTreeCellRenderer extends JPanel implements TreeCellRenderer {

	private static final long serialVersionUID = 1L;
	
	protected Color bg = UIManager.getColor( "Tree.textBackground" );
	protected Color fg = UIManager.getColor( "Tree.textForeground" );
	protected Color sbg = UIManager.getColor( "Tree.selectionBackground" );
	protected Color sfg = UIManager.getColor( "Tree.selectionForeground" );
	
	protected JLabel valueLabel = new JLabel();
	
	protected JLabel expandedLabel = new JLabel();
	
	protected int inset = 1;
	
	protected Border selectedBorder = BorderFactory.createCompoundBorder( BorderFactory.createEmptyBorder( inset, inset, inset, inset ), BorderFactory.createDashedBorder( sfg ) );
	
	public ExtendedTreeCellRenderer() {
		build();
	}
	
	protected void build() {
		this.setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
		this.add( expandedLabel );
		GU.spacer( this );
		this.add( valueLabel );
		GU.spacer( this );		
	}
	
	protected JComponent[] getSelectionComponents() {
		return new JComponent[] { this, valueLabel, expandedLabel };
	}
	
	protected void handleSelected( boolean selected ) {
		for ( JComponent c : getSelectionComponents() ) {
			c.setForeground( selected ? sfg : fg );
			c.setBackground( selected ? sbg : bg );
		}
		this.setBorder( selected ? selectedBorder  : null );
	}
	
	public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus ) {
		valueLabel.setText( value.toString() );
		expandedLabel.setIcon( expanded ? IconLoader.getInstance().getIcon( IconLoader.DOWN_ARROW ) : IconLoader.getInstance().getIcon( IconLoader.RIGHT_ARROW ) );
		handleSelected( selected );			
		return this;
	}	
}