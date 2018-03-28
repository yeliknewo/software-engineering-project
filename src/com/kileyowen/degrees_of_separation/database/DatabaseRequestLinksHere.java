
package com.kileyowen.degrees_of_separation.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.kileyowen.degrees_of_separation.Page;

public class DatabaseRequestLinksHere extends DatabaseRequest<DatabaseResultLinksHere> {

	private final Page pageId;

	private final String databaseString;

	public DatabaseRequestLinksHere(final Page newPage, final String databasePath) {

		this.pageId = newPage;

		this.databaseString = DatabaseRequest.makeDatabaseString(databasePath);

	}

	@Override
	public DatabaseResultLinksHere build() throws ExceptionPageLinksNotStored {

		try {

			return this.buildInternal(DatabaseOperation.READ).orElseThrow(() -> {

				throw new RuntimeException("READ DatabaseOperation should return something");

			});

		} catch (final ExceptionPageNotStored e) {

			throw new RuntimeException(e);

		}

	}

	@Override
	protected ResultSet buildInternalRead(final Statement statement) {

		try {

			return statement.executeQuery(String.format("SELECT path.from_page_id, page.page_title FROM path WHERE to_page_id=%d ", this.pageId.getDatabasePageId()));

		} catch (final SQLException e) {

			throw new RuntimeException(e);

		}

	}

	@Override
	protected void buildInternalWrite(final Statement statement) {

		throw new RuntimeException("Not a Write Request");

	}

	@Override
	protected String getDatabaseString() {

		return this.databaseString;

	}

	@Override
	protected DatabaseResultLinksHere parse(final ResultSet resultSet) throws ExceptionPageLinksNotStored {

		return new DatabaseResultLinksHere(resultSet);

	}

}
