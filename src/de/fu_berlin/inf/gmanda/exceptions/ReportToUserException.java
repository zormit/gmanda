package de.fu_berlin.inf.gmanda.exceptions;

public class ReportToUserException extends WrappedException {
	
	public ReportToUserException(Throwable t){
		super(t);
	}
	
	public ReportToUserException(String message){
		super(new RuntimeException(message));
	}
	
	public String getErrorMessage(){
		
		String message = getCause().getMessage();
		
		if (message == null || message.trim().length() == 0)
			return getCause().toString();
		
		return message;
	}
}
