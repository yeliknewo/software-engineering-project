
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
import com.kileyowen.utils.NullUtils;

public class DatabaseQuerier {

	private static final String DEFAULT_STRING_ADDRESS = "test.db";

	private static final ThreadLocal<Connection> CONNECTION = NullUtils.assertNotNull(ThreadLocal.withInitial(() -> {

		try {

			final Connection connection = NullUtils.assertNotNull(DriverManager.getConnection(DatabaseQuerier.makeDatabaseString(DatabaseQuerier.DEFAULT_STRING_ADDRESS)), "Failed to connect to database");

			connection.setAutoCommit(false);

			return connection;

		} catch (final SQLException e) {

			throw new RuntimeException(e);

		}

	}), "Connection is null");

	private static final ThreadLocal<Integer> COMMIT_COUNTER = NullUtils.assertNotNull(ThreadLocal.withInitial(() -> {

		return DatabaseQuerier.DEFAULT_COMMIT_COUNTER;

	}), "Commit Counter is null");

	private static final Integer DEFAULT_COMMIT_COUNTER = new Integer(10000);

	public static final void addPage(final WikiPage wikiPage) {

		try (final Statement statement = DatabaseQuerier.getConnection().createStatement()) {

			statement.setQueryTimeout(30);

			final String str = String.format("INSERT INTO page (wikipedia_page_id, page_title, last_updated_date) VALUES ('%s', '%s', CURRENT_DATE);", wikiPage.getWikiPageId().toString(), wikiPage.getPageTitle().getDatabasePageTitle());

			statement.executeUpdate(str);

		} catch (final SQLException e) {

			throw new RuntimeException(e);

		}

	}

	public static void addPageLinksToDatabase(final Page page, final List<WikiPage> wikiPages) {

		for (final WikiPage wikiPage : wikiPages) {

			try {

				DatabaseQuerier.getPageByPageTitle(wikiPage.getPageTitle());

			} catch (final ExceptionPageNotStored e1) {

				DatabaseQuerier.addPage(wikiPage);

			}

			try (final Statement statement = DatabaseQuerier.getConnection().createStatement()) {

				statement.setQueryTimeout(30);

				final String str = String.format("INSERT INTO path (from_page_id, to_page_id, distance, last_updated_date) VALUES ('%s', '%s', 1, CURRENT_DATE);", DatabaseQuerier.getPageByPageTitle(wikiPage.getPageTitle()).getDatabasePageId(), page.getDatabasePageId());

				statement.executeUpdate(str);

			} catch (final ExceptionPageNotStored e) {

				throw new RuntimeException("Unable to Add page to Database");

			} catch (final SQLException e) {

				throw new RuntimeException(e);

			}

		}

		try (final Statement statement = DatabaseQuerier.getConnection().createStatement()) {

			statement.setQueryTimeout(30);

			final String str = String.format("UPDATE page SET last_links_updated_date=CURRENT_DATE WHERE page_id=%s", page.getDatabasePageId());

			statement.executeUpdate(str);

		} catch (final SQLException e) {

			throw new RuntimeException(e);
		}

	}

	public static ThreadLocal<Integer> getCommitCounter() {

		return DatabaseQuerier.COMMIT_COUNTER;
	}

	private static final Connection getConnection() throws SQLException {

		DatabaseQuerier.getCommitCounter().set(new Integer(DatabaseQuerier.getCommitCounter().get().intValue() - 1));

		if (DatabaseQuerier.getCommitCounter().get().intValue() <= 0) {

			DatabaseQuerier.getCommitCounter().set(DatabaseQuerier.DEFAULT_COMMIT_COUNTER);

			DatabaseQuerier.CONNECTION.get().commit();

		}

		return DatabaseQuerier.CONNECTION.get();

	}

	public static final Page getPageByPageTitle(final PageTitle pageTitle) throws ExceptionPageNotStored {

		try (final Statement statement = DatabaseQuerier.getConnection().createStatement()) {

			statement.setQueryTimeout(30);

			final String databasePageTitle = pageTitle.getDatabasePageTitle();

			final String query = String.format("SELECT page_id, wikipedia_page_id, page_title, last_links_updated_date FROM page WHERE page_title='%s'", databasePageTitle);

			try (final ResultSet set = statement.executeQuery(query)) {

				if (set.isClosed()) {

					throw new ExceptionPageNotStored("ResultSet was closed on arrival");

				}

				final Page page = new Page(new DatabasePageId(set.getInt(1)), new WikiPageId(set.getInt(2)), new PageTitle(NullUtils.assertNotNull(set.getString(3), "PageTitleNull")), set.getString(4) != null);

				return page;

			}

		} catch (final SQLException e) {

			throw new RuntimeException(e);

		}

	}

	public static List<Page> getPageLinksByPage(final Page page) throws ExceptionPageLinksNotStored, ExceptionPageNotStored {

		DatabaseQuerier.getPageByPageTitle(page.getPageTitle());

		if (!page.areLinksUpToDate()) {

			throw new ExceptionPageLinksNotStored("Links not stored yet");

		}

		try (final Statement statement = DatabaseQuerier.getConnection().createStatement()) {

			statement.setQueryTimeout(30);

			try (final ResultSet set = statement.executeQuery(String.format("SELECT page_id, wikipedia_page_id, page_title, last_links_updated_date FROM page WHERE page.page_id IN(SELECT from_page_id FROM path WHERE path.to_page_id IN (SELECT page_id FROM page WHERE page_title='%s'))", page.getPageTitle().getDatabasePageTitle()))) {

				final List<Page> pages = new ArrayList<>();

				while (!set.isAfterLast()) {

					pages.add(new Page(new DatabasePageId(set.getInt(1)), new WikiPageId(set.getInt(2)), new PageTitle(NullUtils.assertNotNull(set.getString(3), "PageTitle was not found in database")), set.getString(4) != null));

					set.next();

				}

				return pages;
			}

		} catch (final SQLException e) {

			throw new RuntimeException(e);

		}

	}

	public static final void initDatabase() {

		try (final Statement statement = DatabaseQuerier.getConnection().createStatement()) {

			statement.setQueryTimeout(30);

			statement.executeUpdate("CREATE TABLE IF NOT EXISTS page ( page_id INTEGER NOT NULL PRIMARY KEY, wikipedia_page_id INTEGER NOT NULL UNIQUE, page_title TEXT NOT NULL, last_updated_date TEXT, last_links_updated_date TEXT ); CREATE TABLE IF NOT EXISTS path ( path_id INTEGER NOT NULL PRIMARY KEY, from_page_id INTEGER NOT NULL, to_page_id INTEGER NOT NULL, distance INTEGER NOT NULL, last_updated_date TEXT, FOREIGN KEY(from_page_id) REFERENCES page(page_id), FOREIGN KEY(to_page_id) REFERENCES page(page_id) ); CREATE TABLE IF NOT EXISTS path_link ( path_link_id INTEGER NOT NULL PRIMARY KEY, path_fk INTEGER NOT NULL, from_page_id INTEGER NOT NULL, to_page_id INTEGER NOT NULL, path_link_index INTEGER NOT NULL, last_updated_date TEXT, FOREIGN KEY(path_fk) REFERENCES path(path_id), FOREIGN KEY(from_page_id) REFERENCES page(page_id), FOREIGN KEY(to_page_id) REFERENCES page(page_id) );");

		} catch (final SQLException e) {

			throw new RuntimeException(e);

		}

	}

	final static String makeDatabaseString(final String databasePath) {

		return "jdbc:sqlite:" + databasePath;

	}

}
