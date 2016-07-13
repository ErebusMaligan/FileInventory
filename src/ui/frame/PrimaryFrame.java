package ui.frame;

import gui.textarea.DefaultJTextAreaStreamManager;
import gui.textarea.JTextAreaLineLimitDocument;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import ui.menubar.PrimaryMenuBar;
import ui.panels.ReplacableComponent;

/**
 * @author Daniel J. Rivers
 *         2014
 *
 * Created: Apr 19, 2014, 3:17:44 AM 
 */
public class PrimaryFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JComponent comp;
	
	private DefaultJTextAreaStreamManager out = new DefaultJTextAreaStreamManager();
	
	private JSplitPane split = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
	
	
	public PrimaryFrame() {
		super( "Inventory Tool" );
		this.setSize( 1024, 768 );
		this.setDefaultCloseOperation( EXIT_ON_CLOSE );
		this.setLayout( new BorderLayout() );
		this.add( new PrimaryMenuBar(), BorderLayout.NORTH );
		split.setBottomComponent( createSouthComponent() );
		split.setDividerLocation( .75d );
		this.add( split, BorderLayout.CENTER );
		this.setVisible( true );
	}
	
	public void setCenterComponent( JComponent newComp ) {
		if ( comp != null ) {
			split.remove( comp );
			if ( comp instanceof ReplacableComponent ) {
				( (ReplacableComponent)comp ).shutdown();
			}
		}
		comp = newComp;
		split.setTopComponent( comp );
		split.setDividerLocation( .75d );
		split.revalidate();
	}
	
	private JPanel createSouthComponent() {
		JPanel south = new JPanel();
		JTextArea area = new JTextArea( 24, 100 );
		out.registerArea( area );
		int b = 10;
		area.setBorder( BorderFactory.createEmptyBorder( b, b, b, b ) );
		area.setDocument( new JTextAreaLineLimitDocument( area, 20 ) );
		area.setEditable( false );
		south.add( new JScrollPane( area ), BorderLayout.CENTER );
		return south;
	}
}