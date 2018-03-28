
package com.kileyowen.degrees_of_separation.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseRequestPage extends DatabaseRequest<DatabaseResultPage> {

	private final String pageTitle;

	private final String databaseString;

	public DatabaseRequestPage(final String newPageTitle, final String databasePath) {

		this.pageTitle = newPageTitle;

		this.databaseString = DatabaseRequest.makeDatabaseString(databasePath);

	}

	@Override
	public DatabaseResultPage build() throws ExceptionPageNotStored {

		try {

			return this.buildInternal(DatabaseOperation.READ).orElseThrow(() -> {

				throw new RuntimeException("READ should return something");

			});

		} catch (final ExceptionPageLinksNotStored e) {

			throw new RuntimeException(e);

		}

	}

	@Override
	protected ResultSet buildInternalRead(final Statement statement) {

		try {

			final ResultSet set = statement.executeQuery(String.format("SELECT page_id, wikipedia_page_id, page_title FROM page WHERE page_title=\"%s\"", this.pageTitle));

			return set;

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
	protected DatabaseResultPage parse(final ResultSet resultSet) throws ExceptionPageNotStored {

		return new DatabaseResultPage(resultSet);

	}

}
