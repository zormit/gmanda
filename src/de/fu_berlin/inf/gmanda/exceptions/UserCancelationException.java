package de.fu_berlin.inf.gmanda.exceptions;

public class UserCancelationException extends DoNotShowToUserException {

	public UserCancelationException(){
		this("Operation canceled by user");
	}
	
	public UserCancelationException(Throwable t){
		super(t);
	}
	
	public UserCancelationException(String message){
		super(new RuntimeException(message));
	}
	
}