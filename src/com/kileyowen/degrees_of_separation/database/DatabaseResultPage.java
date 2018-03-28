
package com.kileyowen.degrees_of_separation.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.kileyowen.degrees_of_separation.Page;
import com.kileyowen.degrees_of_separation.PageTitle;
import com.kileyowen.degrees_of_separation.wikipedia.WikiPageId;

public class DatabaseResultPage extends DatabaseResult {

	private final Page page;

	public DatabaseResultPage(final ResultSet resultSet) throws ExceptionPageNotStored {

		try {

			if (resultSet.isClosed()) {

				throw new ExceptionPageNotStored("ResultSet was closed on arrival");

			}

			this.page = new Page(new DatabasePageId(resultSet.getInt("page_id")), new WikiPageId(resultSet.getInt("wikipedia_page_id")), PageTitle.makePageTitleByDatabasePageTitle(resultSet.getString("page_title")));

		} catch (final SQLException e) {

			throw new RuntimeException(e);

		}

	}

	public Page getPage() {

		return this.page;

	}

}
