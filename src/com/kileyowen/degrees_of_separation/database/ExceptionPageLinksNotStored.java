
package com.kileyowen.degrees_of_separation.database;

/**
 * Thrown when the page in the database does not have the links downloaded from wikipedia
 *
 * @author Kiley Owen
 *
 */
public class ExceptionPageLinksNotStored extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -5447743558488566907L;

	/**
	 * Creates an exception with a message and no cause
	 *
	 * @param message
	 *            the message to be sent with the exception
	 */
	public ExceptionPageLinksNotStored(final String message) {

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
	public ExceptionPageLinksNotStored(final String message, final Throwable cause) {

		super(message, cause);

	}

}
