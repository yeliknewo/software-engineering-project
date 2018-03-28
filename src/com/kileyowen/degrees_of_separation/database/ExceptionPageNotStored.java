
package com.kileyowen.degrees_of_separation.database;

/**
 * @author Kiley Owen
 *         Thrown when the page being searched for is not stored in the database
 */
public class ExceptionPageNotStored extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 8315603786416614268L;

	/**
	 * Creates an exception with a message and no cause
	 *
	 * @param message
	 *            the message to be sent with the exception
	 */
	public ExceptionPageNotStored(final String message) {

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
	public ExceptionPageNotStored(final String message, final Throwable cause) {

		super(message, cause);

	}

}
