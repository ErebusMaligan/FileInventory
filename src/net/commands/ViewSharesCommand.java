package net.commands;

import process.StandardProcess;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Nov 28, 2013, 7:33:01 AM 
 */
public class ViewSharesCommand extends ProcessCommand {

	public ViewSharesCommand( String command, StandardProcess process ) {
		super( command, process );
	}

	@Override
	public boolean isDone( String line ) {
		return line.contains( "The command completed successfully." );
	}

	@Override
	public boolean tryStart( String line ) {
		return line.contains( "Disk" );
	}
}