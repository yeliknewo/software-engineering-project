
package com.kileyowen.degrees_of_separation.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.kileyowen.degrees_of_separation.Page;
import com.kileyowen.degrees_of_separation.PageTitle;
import com.kileyowen.degrees_of_separation.wikipedia.WikiPage;
import com.kileyowen.degrees_of_separation.wikipedia.WikiPageId;

public class DatabaseQuerier {

	public static final void addPage(final String databasePath, final WikiPage wikiPage) {

		final String databaseString = DatabaseQuerier.makeDatabaseString(databasePath);

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

	public static void addPageLinksToDatabase(final String databasePath, final Page page, final List<WikiPage> wikiPages) {

		final String databaseString = DatabaseQuerier.makeDatabaseString(databasePath);

		for (final WikiPage wikiPage : wikiPages) {

			try {

				DatabaseQuerier.getPageByPageTitle(databasePath, wikiPage.getPageTitle());

			} catch (final ExceptionPageNotStored e1) {

				DatabaseQuerier.addPage(databasePath, wikiPage);

			}

			try (final Connection connection = DriverManager.getConnection(databaseString)) {

				try (final Statement statement = connection.createStatement()) {

					statement.setQueryTimeout(30);

					final String str = String.format("INSERT INTO path (from_page_id, to_page_id, distance, last_updated_date) VALUES (\"%s\", \"%s\", 1, CURRENT_DATE);", DatabaseQuerier.getPageByPageTitle(databasePath, wikiPage.getPageTitle()).getDatabasePageId(), page.getDatabasePageId());

					statement.executeUpdate(str);

				} catch (final ExceptionPageNotStored e) {

					throw new RuntimeException("Unable to Add page to Database");

				}

			} catch (final SQLException e) {

				throw new RuntimeException(e);

			}

		}

	}

	public static final Page getPageByPageTitle(final String databasePath, final PageTitle pageTitle) throws ExceptionPageNotStored {

		final String databaseString = DatabaseQuerier.makeDatabaseString(databasePath);

		try (final Connection connection = DriverManager.getConnection(databaseString)) {

			try (final Statement statement = connection.createStatement()) {

				statement.setQueryTimeout(30);

				final String query = String.format("SELECT page_id, wikipedia_page_id, page_title FROM page WHERE page_title=\"%s\"", pageTitle.getDatabasePageTitle());

				System.out.println(query);

				final ResultSet set = statement.executeQuery(query);

				if (set.isClosed()) {

					throw new ExceptionPageNotStored("ResultSet was closed on arrival");

				}

				return new Page(new DatabasePageId(set.getInt(1)), new WikiPageId(set.getInt(2)), PageTitle.makePageTitleByDatabasePageTitle(set.getString(3)));

			}

		} catch (final SQLException e) {

			throw new RuntimeException(e);

		}

	}

	public static List<Page> getPageLinksByPage(final String databasePath, final Page page) throws ExceptionPageLinksNotStored, ExceptionPageNotStored {

		final String databaseString = DatabaseQuerier.makeDatabaseString(databasePath);

		try (final Connection connection = DriverManager.getConnection(databaseString)) {

			try (final Statement statement = connection.createStatement()) {

				statement.setQueryTimeout(30);

				try (final ResultSet set = statement.executeQuery(String.format("SELECT page_id, wikipedia_page_id, page_title FROM page WHERE page.page_id IN(SELECT from_page_id FROM path WHERE path.to_page_id IN (SELECT page_id FROM page WHERE page_title=\"%s\"))", page.getPageTitle().getDatabasePageTitle()))) {

					if (set.isClosed()) {

						throw new ExceptionPageNotStored("ResultSet was closed on arrival");

					}

					final List<Page> pages = new ArrayList<Page>();

					while (!set.isAfterLast()) {

						pages.add(new Page(new DatabasePageId(set.getInt(1)), new WikiPageId(set.getInt(2)), PageTitle.makePageTitleByDatabasePageTitle(set.getString(3))));

						set.next();

					}

					return pages;
				}

			}

		} catch (final SQLException e) {

			throw new RuntimeException(e);

		}

	}

	public static final void initDatabase(final String databasePath) {

		final String databaseString = DatabaseQuerier.makeDatabaseString(databasePath);

		try (final Connection connection = DriverManager.getConnection(databaseString)) {

			try (final Statement statement = connection.createStatement()) {

				statement.setQueryTimeout(30);

				statement.executeUpdate("CREATE TABLE IF NOT EXISTS page ( page_id INTEGER NOT NULL PRIMARY KEY, wikipedia_page_id INTEGER NOT NULL UNIQUE, page_title TEXT NOT NULL, last_updated_date TEXT ); CREATE TABLE IF NOT EXISTS path ( path_id INTEGER NOT NULL PRIMARY KEY, from_page_id INTEGER NOT NULL, to_page_id INTEGER NOT NULL, distance INTEGER NOT NULL, last_updated_date TEXT, FOREIGN KEY(from_page_id) REFERENCES page(page_id), FOREIGN KEY(to_page_id) REFERENCES page(page_id) ); CREATE TABLE IF NOT EXISTS path_to_path ( path_to_path_id INTEGER NOT NULL PRIMARY KEY, from_path_id INTEGER NOT NULL, to_path_id INTEGER NOT NULL, last_updated_date TEXT, FOREIGN KEY(from_path_id) REFERENCES path(path_id), FOREIGN KEY(to_path_id) REFERENCES path(path_id) );");

			}

		} catch (final SQLException e) {

			throw new RuntimeException(e);

		}

	}

	final static String makeDatabaseString(final String databasePath) {

		return "jdbc:sqlite:" + databasePath;

	}

}
