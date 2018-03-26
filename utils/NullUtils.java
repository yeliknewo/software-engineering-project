
package com.kileyowen.utils;

import org.eclipse.jdt.annotation.Nullable;

public class NullUtils {

	public static final <T> T assertNotNull(final @Nullable T obj, final String nullMessage) throws ExceptionNull {

		if (obj == null) {

			throw new ExceptionNull(nullMessage);

		}

		return obj;
	}
}