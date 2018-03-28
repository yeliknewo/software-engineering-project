
package com.kileyowen.degrees_of_separation;

/**
 * @author Kiley Owen
 *         Thrown when the end page title is invalid
 */
public class ExceptionBadEndPageTitle extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -8657781374727106146L;

	/**
	 * Creates a new exception with a message and no cause
	 * 
	 * @param message
	 *            the message to send with the exception
	 */
	public ExceptionBadEndPageTitle(final String message) {

		super(message);

	}

	/**
	 * Creates a new exception with a message and a cause
	 *
	 * @param message
	 *            the message to send with the exception
	 * @param cause
	 *            the exception that caused this exception
	 */
	public ExceptionBadEndPageTitle(final String message, final Throwable cause) {

		super(message, cause);

	}

}
