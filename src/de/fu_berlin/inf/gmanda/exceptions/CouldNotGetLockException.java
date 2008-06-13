package de.fu_berlin.inf.gmanda.exceptions;

public class CouldNotGetLockException extends ReportToUserException {
	public CouldNotGetLockException(Throwable t) {
		super(t);
	}

	public CouldNotGetLockException(String message) {
		super(new RuntimeException(message));
	}
}
