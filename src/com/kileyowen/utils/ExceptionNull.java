
package com.kileyowen.utils;

/**
 * @author Kiley Owen
 *         Thrown when a value is null
 */
public class ExceptionNull extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -3916575348644738286L;

	/**
	 * Creates an exception with a message and no cause
	 *
	 * @param message
	 *            the message to be sent with the exception
	 */
	public ExceptionNull(final String message) {

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
	public ExceptionNull(final String message, final Throwable cause) {

		super(message, cause);

	}

}
