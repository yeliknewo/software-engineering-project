
package com.kileyowen.degrees_of_separation.wikipedia;

/**
 * @author Kiley Owen
 *         Thrown when the page does not exist on Wikipedia. Probably caused by a bad title.
 */
public class ExceptionPageDoesNotExistOnWiki extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1961448183442871289L;

	/**
	 * Creates an exception with a message and no cause
	 *
	 * @param message
	 *            the message to be sent with the exception
	 */
	public ExceptionPageDoesNotExistOnWiki(final String message) {

		super(message);

	}

	/**
	 * Creates an exception with a message and a cause
	 *
	 * @param message
	 *            the message to be sent with the exception
	 * @param cause
	 *            the exception that caused this exception
	 */
	public ExceptionPageDoesNotExistOnWiki(final String message, final Throwable cause) {

		super(message, cause);

	}

}
