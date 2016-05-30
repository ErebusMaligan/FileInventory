package net.commands;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;

import process.ProcessManager;
import process.StandardProcess;
import process.io.ProcessStreamSiphon;

/**
 * @author Daniel J. Rivers
 *         2013
 *
 * Created: Nov 28, 2013, 6:59:15 AM 
 */
public abstract class ProcessCommand implements ProcessStreamSiphon, Callable<List<String>> {
	
	protected String command;
	
	protected StandardProcess process;
	
	protected boolean isStarted = false;
	
	protected List<String> results = new Vector<String>();
	
	protected List<String> errors = new Vector<String>();
	
	boolean error = false;
	
	private boolean isDone = false;
	
	public ProcessCommand( String command, StandardProcess process ) {
		this.command = command;
		this.process = process;
		errors.add( "error" );
		errors.add( "unknown" );
	}
	
	public List<String> call() {
		ProcessManager.getInstance().registerSiphon( process.getName(), this );
		process.sendCommand( command );
		while ( !isDone ) {
			try {
				Thread.sleep( 100 );  //TODO: get rid of this spinlock at some point replace with - sleep for max value/interrupt lock
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		}
		return results;
	}
	
	public abstract boolean isDone( String line );
	
	public abstract boolean tryStart( String line );
	
	@Override
	public void skimMessage( String name, String line ) {
//		System.out.println( line );  //debug process output
		error = checkError( line );
		if ( !isStarted && !error ) {
			isStarted = tryStart( line );
		}
		if ( error ) {
//			System.err.println( "ERROR!" );  //debug process output
			terminate();
		} else {
			if ( isStarted ) {
				if ( isDone( line ) ) {
					terminate();
				} else {
					results.add( line.trim() );  //TODO: remember trim was added here
				}
			}
		}
	}
	
	protected void terminate() {
		ProcessManager.getInstance().removeSiphon( process.getName(), this );
		isDone = true;			
	}
	
	protected boolean checkError( String line ) {
		boolean ret = false;
		for ( String s : errors ) {
			if ( line.contains( s ) ) {
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public boolean didError() {
		return error;
	}
	
	public String getCommand() {
		return command;
	}
	
	//not used -- at least for now
	@Override
	public void notifyProcessEnded( String arg0 ) {
	}

	@Override
	public void notifyProcessStarted( String arg0 ) {		
	}
}