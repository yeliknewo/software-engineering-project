
package com.kileyowen.utils;

import org.eclipse.jdt.annotation.Nullable;

/**
 * @author Kiley Owen
 *         Basic helper class for Null Verification to throw ExceptionNull when a value is null
 */
public class NullUtils {

	/**
	 * If obj is null throws ExceptionNull with the message nullMessage otherwise it returns obj.
	 *
	 * @param <T>
	 *            the type of obj
	 * @param obj
	 *            the object to test if is null
	 * @param nullMessage
	 *            the message to be sent with the exception if obj is null
	 * @return obj
	 * @throws ExceptionNull
	 *             thrown when obj is null
	 */
	public static final <T> T assertNotNull(final @Nullable T obj, final String nullMessage) {

		if (obj == null) {

			throw new ExceptionNull(nullMessage);

		}

		return obj;
	}
}