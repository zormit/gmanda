package de.fu_berlin.inf.gmanda.exceptions;

public class WrappedException extends RuntimeException {

	public WrappedException(Throwable t){
		super(t instanceof WrappedException ? t.getCause() : t);
	}
	
	public WrappedException(String message){
		super(new RuntimeException(message));
	}
	
}
