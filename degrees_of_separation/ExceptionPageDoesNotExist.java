
package com.kileyowen.degrees_of_separation;

public class ExceptionPageDoesNotExist extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1961448183442871289L;

	public ExceptionPageDoesNotExist(final String message) {

		super(message);

	}

	public ExceptionPageDoesNotExist(final String message, final Throwable cause) {

		super(message, cause);

	}

}
