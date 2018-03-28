
package com.kileyowen.degrees_of_separation;

/**
 * @author Kiley Owen
 *         Thrown when the Start Page Title is invalid
 */
public class ExceptionBadStartPageTitle extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -7408966045763171335L;

	/**
	 * Creates an exception with a message and no cause
	 *
	 * @param message
	 *            the message to be sent with the exception
	 */
	public ExceptionBadStartPageTitle(final String message) {

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
	public ExceptionBadStartPageTitle(final String message, final Throwable cause) {

		super(message, cause);

	}

}
