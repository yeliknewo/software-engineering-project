
package com.kileyowen.degrees_of_separation;

public class ExceptionWikiQueryError extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -1985619870281345453L;

	public ExceptionWikiQueryError(final String message) {

		super(message);

	}

	public ExceptionWikiQueryError(final String message, final Throwable cause) {

		super(message, cause);

	}

}
