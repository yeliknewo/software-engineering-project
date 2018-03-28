
package com.kileyowen.degrees_of_separation.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.kileyowen.degrees_of_separation.Page;
import com.kileyowen.degrees_of_separation.PageTitle;
import com.kileyowen.degrees_of_separation.wikipedia.WikiPageId;

public class DatabaseRead {

	public static final Page getPageByPageTitle(final String databasePath, final PageTitle pageTitle) throws ExceptionPageNotStored {

		final String databaseString = DatabaseUtils.makeDatabaseString(databasePath);

		try (final Connection connection = DriverManager.getConnection(databaseString)) {

			try (final Statement statement = connection.createStatement()) {

				statement.setQueryTimeout(30);

				final ResultSet set = statement.executeQuery(String.format("SELECT page_id, wikipedia_page_id, page_title FROM page WHERE page_title=\"%s\"", pageTitle.getDatabasePageTitle()));

				if (set.isClosed()) {

					throw new ExceptionPageNotStored("ResultSet was closed on arrival");

				}

				return new Page(new DatabasePageId(set.getInt(0)), new WikiPageId(set.getInt(1)), PageTitle.makePageTitleByDatabasePageTitle(set.getString(2)));

			}

		} catch (final SQLException e) {

			throw new RuntimeException(e);

		}

	}

}
