
package com.kileyowen.degrees_of_separation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.kileyowen.degrees_of_separation.database.DatabaseRequestLinksHere;
import com.kileyowen.degrees_of_separation.database.DatabaseRequestPageId;
import com.kileyowen.degrees_of_separation.wikipedia.WikiRequestLinksHere;
import com.kileyowen.degrees_of_separation.wikipedia.WikiRequestPageId;
import com.kileyowen.utils.ExceptionNull;
import com.kileyowen.utils.NullUtils;

public class Querier {

	private static List<Page> getLinksHereByPageIdDatabase(final PageId pageId) throws SQLException, ExceptionNull {

		return new DatabaseRequestLinksHere(pageId).build().getPages();

	}

	private static List<Page> getLinksHereByPageIdWikipedia(final PageId pageId) throws IOException, ExceptionNull, ExceptionWikiQueryError {

		return new WikiRequestLinksHere(pageId).build().getPages();

	}

	private static Page getPageByTitleDatabase(final String pageTitle) throws SQLException, ExceptionNull {

		return new DatabaseRequestPageId(pageTitle).build().getPage();

	}

	private static Page getPageByTitleWikipedia(final String pageTitle) throws IOException, ExceptionNull, ExceptionWikiQueryError {

		return new WikiRequestPageId(pageTitle).build().getPage();

	}

	private final boolean online;

	public Querier(final boolean newOnline) {

		this.online = newOnline;

	}

	public List<Page> getLinksHereByPageId(final PageId pageId) throws ExceptionPageDoesNotExist, ExceptionNull, IOException {

		//		try {
		//			return Querier.getLinksHereByPageIdDatabase(pageId);
		//		} catch (final SQLException e) {
		//			e.printStackTrace();
		//
		//		}

		if (this.online) {

			try {

				return Querier.getLinksHereByPageIdWikipedia(pageId);

			} catch (final ExceptionWikiQueryError e) {

				e.printStackTrace();

			}

		}

		throw new ExceptionPageDoesNotExist(NullUtils.assertNotNull(pageId.toString(), "PageId.ToString was null"));

	}

	public Page getPageByTitle(final String pageTitle) throws ExceptionPageDoesNotExist, IOException, ExceptionNull {

		//		try {
		//			return Querier.getPageIdByTitleDatabase(pageTitle);
		//		} catch (final SQLException e) {
		//			e.printStackTrace();
		//		}

		if (this.online) {

			try {
				return Querier.getPageByTitleWikipedia(pageTitle);
			} catch (final ExceptionWikiQueryError e) {
				e.printStackTrace();
			}

		}

		throw new ExceptionPageDoesNotExist(pageTitle);

	}

}
