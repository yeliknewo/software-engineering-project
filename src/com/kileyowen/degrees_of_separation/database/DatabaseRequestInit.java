
package com.kileyowen.degrees_of_separation.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseRequestInit extends DatabaseRequest<DatabaseResult> {

	private final String databaseString;

	public DatabaseRequestInit(final String databasePath) {

		this.databaseString = DatabaseRequest.makeDatabaseString(databasePath);

	}

	@Override
	public DatabaseResult build() {

		try {

			this.buildInternal(DatabaseOperation.WRITE);

		} catch (ExceptionPageLinksNotStored | ExceptionPageNotStored e) {

			throw new RuntimeException(e);

		}

		return new DatabaseResult();

	}

	@Override
	protected ResultSet buildInternalRead(final Statement statement) {

		throw new RuntimeException("Not a read operation");

	}

	@Override
	protected void buildInternalWrite(final Statement statement) {

		try {

			statement.execute("CREATE TABLE IF NOT EXISTS page (page_id INTEGER NOT NULL PRIMARY KEY, wikipedia_page_id INTEGER NOT NULL UNIQUE, page_title TEXT NOT NULL, last_updated_date TEXT);");

			statement.execute("CREATE TABLE IF NOT EXISTS path (path_id INTEGER NOT NULL PRIMARY KEY, from_page_id INTEGER NOT NULL, to_page_id INTEGER NOT NULL, distance INTEGER NOT NULL, last_updated_date TEXT, FOREIGN KEY(from_page_id) REFERENCES page(page_id), FOREIGN KEY(to_page_id) REFERENCES page(page_id));");

			statement.execute("CREATE TABLE IF NOT EXISTS path_to_path (path_to_path_id INTEGER NOT NULL PRIMARY KEY, from_path_id INTEGER NOT NULL, to_path_id INTEGER NOT NULL, last_updated_date TEXT, FOREIGN KEY(from_path_id) REFERENCES path(path_id), FOREIGN KEY(to_path_id) REFERENCES path(path_id));");

		} catch (final SQLException e) {

			throw new RuntimeException(e);

		}

	}

	@Override
	protected String getDatabaseString() {

		return this.databaseString;

	}

	@Override
	protected DatabaseResult parse(final ResultSet resultSet) {

		return new DatabaseResult();

	}

}
