
package com.kileyowen.degrees_of_separation.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.kileyowen.degrees_of_separation.wikipedia.WikiPage;

public class DatabaseWrite {

	public static final void addPage(final String databasePath, final WikiPage wikiPage) {

		final String databaseString = DatabaseUtils.makeDatabaseString(databasePath);

		try (final Connection connection = DriverManager.getConnection(databaseString)) {

			try (final Statement statement = connection.createStatement()) {

				statement.setQueryTimeout(30);

				final String str = String.format("INSERT INTO page (wikipedia_page_id, page_title, last_updated_date) VALUES (\"%s\", \"%s\", CURRENT_DATE);", wikiPage.getWikiPageId().toString(), wikiPage.getPageTitle().getDatabasePageTitle());

				statement.executeUpdate(str);
			}

		} catch (final SQLException e) {

			throw new RuntimeException(e);

		}

	}

	public static final void initDatabase(final String databasePath) {

		final String databaseString = DatabaseUtils.makeDatabaseString(databasePath);

		try (final Connection connection = DriverManager.getConnection(databaseString)) {

			try (final Statement statement = connection.createStatement()) {

				statement.setQueryTimeout(30);

				statement.executeUpdate("CREATE TABLE IF NOT EXISTS page ( page_id INTEGER NOT NULL PRIMARY KEY, wikipedia_page_id INTEGER NOT NULL UNIQUE, page_title TEXT NOT NULL, last_updated_date TEXT ); CREATE TABLE IF NOT EXISTS path ( path_id INTEGER NOT NULL PRIMARY KEY, from_page_id INTEGER NOT NULL, to_page_id INTEGER NOT NULL, distance INTEGER NOT NULL, last_updated_date TEXT, FOREIGN KEY(from_page_id) REFERENCES page(page_id), FOREIGN KEY(to_page_id) REFERENCES page(page_id) ); CREATE TABLE IF NOT EXISTS path_to_path ( path_to_path_id INTEGER NOT NULL PRIMARY KEY, from_path_id INTEGER NOT NULL, to_path_id INTEGER NOT NULL, last_updated_date TEXT, FOREIGN KEY(from_path_id) REFERENCES path(path_id), FOREIGN KEY(to_path_id) REFERENCES path(path_id) );");

			}

		} catch (final SQLException e) {

			throw new RuntimeException(e);

		}

	}

}
