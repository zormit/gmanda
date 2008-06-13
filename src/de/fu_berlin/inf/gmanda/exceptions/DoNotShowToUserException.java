package de.fu_berlin.inf.gmanda.exceptions;

public class DoNotShowToUserException extends WrappedException {
	
	public DoNotShowToUserException(Throwable t){
		super(t);
	}
	
	public DoNotShowToUserException(String message){
		super(new RuntimeException(message));
	}

}
