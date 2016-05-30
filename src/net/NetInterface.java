package net;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import net.commands.ProcessCommand;
import net.commands.ViewDeviceCommand;
import net.commands.ViewSharesCommand;
import process.TerminalProcess;
import ui.terminal.os.OSTerminalSettings;
import ui.terminal.panel.TerminalWindowManager;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Nov 28, 2013, 6:21:06 AM 
 */
public class NetInterface {
	
	private static final String NAME = "NET";
	
	private TerminalProcess process;
	
	private static NetInterface instance;
	
	private NetInterface() {
		TerminalWindowManager.getInstance().OS = OSTerminalSettings.WINDOWS;
		process = new TerminalProcess( NAME );
	}
	
	public static NetInterface getInstance() {
		if ( instance == null ) {
			instance = new NetInterface();
		}
		return instance;
	}
	
	public List<String> getRemoteSystemListing() {
		return sendCommand( new ViewDeviceCommand( "net view", process ) );
	}
	
	public List<String> getRemoteSystemShares( String system ) {
		return sendCommand( new ViewSharesCommand( "net view " + system, process ) );
	}
	
	private List<String> sendCommand( ProcessCommand c ) {
		List<String> results = null;
		FutureTask<List<String>> f = new FutureTask<List<String>>( c );
		f.run();
		try {
			results = f.get();
		} catch ( InterruptedException | ExecutionException e ) {
			e.printStackTrace();
		}
		if ( c.didError() ) {
			results = null;
			System.err.println( c.getCommand() + " failed to complete properly" );
		}
		return results;
	}

	public void shutdown() {
		try {
			process.closeResources();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
}