package ui.frame;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;

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
	
	public PrimaryFrame() {
		super( "Inventory Tool" );
		this.setSize( 1024, 768 );
		this.setDefaultCloseOperation( EXIT_ON_CLOSE );
		
		this.setLayout( new BorderLayout() );
		this.add( new PrimaryMenuBar(), BorderLayout.NORTH );
		
		this.setVisible( true );
	}
	
	public void setCenterComponent( JComponent newComp ) {
		if ( comp != null ) {
			this.remove( comp );
			if ( comp instanceof ReplacableComponent ) {
				( (ReplacableComponent)comp ).shutdown();
			}
		}
		comp = newComp;
		this.add( comp, BorderLayout.CENTER );
		this.revalidate();
	}
}