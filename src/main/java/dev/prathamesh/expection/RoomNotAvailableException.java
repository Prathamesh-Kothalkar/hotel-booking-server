package dev.prathamesh.expection;

public class RoomNotAvailableException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RoomNotAvailableException(String message) { super(message); }
}