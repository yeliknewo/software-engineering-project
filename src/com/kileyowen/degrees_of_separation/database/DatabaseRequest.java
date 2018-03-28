
package com.kileyowen.degrees_of_separation.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public abstract class DatabaseRequest<T extends DatabaseResult> {

	protected static final String DATABASE_LAUNCH_STRING = "jdbc:sqlite:";

	protected final static String makeDatabaseString(final String databasePath) {

		return DatabaseRequest.DATABASE_LAUNCH_STRING + databasePath;

	}

	public abstract T build() throws ExceptionPageNotStored, ExceptionPageLinksNotStored;

	protected Optional<T> buildInternal(final DatabaseOperation dataOp) throws ExceptionPageLinksNotStored, ExceptionPageNotStored {

		try (final Connection connection = DriverManager.getConnection(this.getDatabaseString())) {

			try (final Statement statement = connection.createStatement()) {

				statement.setQueryTimeout(30);

				switch (dataOp) {

					case READ:

						return Optional.of(this.parse(this.buildInternalRead(statement)));

					case WRITE:

						this.buildInternalWrite(statement);

						return Optional.empty();

					default:

						throw new RuntimeException("This should be impossible");

				}

			}

		} catch (final SQLException e) {

			throw new RuntimeException(e);

		}

	}

	protected abstract ResultSet buildInternalRead(final Statement statement);

	protected abstract void buildInternalWrite(final Statement statement);

	protected abstract String getDatabaseString();

	protected abstract T parse(ResultSet resultSet) throws ExceptionPageLinksNotStored, ExceptionPageNotStored;

}
