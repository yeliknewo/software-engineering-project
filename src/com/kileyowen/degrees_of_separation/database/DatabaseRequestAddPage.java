
package com.kileyowen.degrees_of_separation.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.kileyowen.degrees_of_separation.wikipedia.WikiPage;

public class DatabaseRequestAddPage extends DatabaseRequest<DatabaseResult> {

	private final String databaseString;

	private final WikiPage wikiPage;

	public DatabaseRequestAddPage(final String databasePath, final WikiPage newWikiPage) {

		this.wikiPage = newWikiPage;

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

			statement.executeUpdate(String.format("INSERT INTO page (wikipedia_page_id, page_title, last_updated_date) VALUES (\"%s\", \"%s\", CURRENT_DATE);", this.wikiPage.getWikiPageId().toString(), this.wikiPage.getPageTitle()));

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
