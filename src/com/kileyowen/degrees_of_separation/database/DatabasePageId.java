
package com.kileyowen.degrees_of_separation.database;

import org.eclipse.jdt.annotation.Nullable;

public class DatabasePageId {

	private final int databasePageId;

	public DatabasePageId(final int newDatabasePageId) {

		this.databasePageId = newDatabasePageId;

	}

	@Override
	public boolean equals(final @Nullable Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final DatabasePageId other = (DatabasePageId) obj;
		if (this.databasePageId != other.databasePageId) {
			return false;
		}
		return true;
	}

	public final int getDatabasePageId() {

		return this.databasePageId;

	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + this.databasePageId;
		return result;
	}

	@Override
	public String toString() {

		return Integer.toString(this.databasePageId);
	}

}
