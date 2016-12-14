package net.commands;

import process.StandardProcess;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Nov 28, 2013, 7:12:53 AM 
 */
public class ViewDeviceCommand extends ProcessCommand {

	public ViewDeviceCommand( String command, StandardProcess process ) {
		super( command, process );
	}

	@Override
	public boolean isDone( String line ) {
		return line.startsWith( "The command" );
	}

	@Override
	public boolean tryStart( String line ) {
		return line.startsWith( "\\" );
	}

}