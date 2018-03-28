
package com.kileyowen.degrees_of_separation;

import java.util.List;

import com.kileyowen.degrees_of_separation.database.DatabaseQuerier;
import com.kileyowen.degrees_of_separation.database.ExceptionPageLinksNotStored;
import com.kileyowen.degrees_of_separation.database.ExceptionPageNotStored;
import com.kileyowen.degrees_of_separation.wikipedia.ExceptionPageDoesNotExistOnWiki;
import com.kileyowen.degrees_of_separation.wikipedia.WikiPage;
import com.kileyowen.degrees_of_separation.wikipedia.WikipediaQuerier;

public class Querier {

	private final boolean online;

	private final String databasePath;

	public Querier(final boolean newOnline, final String newDatabasePath) {

		this.online = newOnline;

		this.databasePath = newDatabasePath;

		DatabaseQuerier.initDatabase(newDatabasePath);

	}

	public List<Page> getLinksHereByPage(final Page page) throws ExceptionPageLinksNotStored {

		try {

			return DatabaseQuerier.getPageLinksByPage(this.databasePath, page);

		} catch (final ExceptionPageLinksNotStored | ExceptionPageNotStored e) {

			//			e.printStackTrace();

		}

		if (this.online) {

			final List<WikiPage> wikiPages = WikipediaQuerier.getPageLinksByPage(page.getWikiPageId());

			DatabaseQuerier.addPageLinksToDatabase(this.databasePath, page, wikiPages);

			try {

				return DatabaseQuerier.getPageLinksByPage(this.databasePath, page);

			} catch (final ExceptionPageLinksNotStored | ExceptionPageNotStored e) {

				throw new RuntimeException("Failed to add page links to database", e);

			}

		}

		throw new ExceptionPageLinksNotStored(String.format("Links not stored for Page: %s", page));

	}

	public Page getPageByPageTitle(final PageTitle pageTitle) throws ExceptionPageDoesNotExistOnWiki {

		try {

			return DatabaseQuerier.getPageByPageTitle(this.databasePath, pageTitle);

		} catch (final ExceptionPageNotStored e) {

			//			e.printStackTrace();D

		}

		if (this.online) {

			final WikiPage wikiPage = WikipediaQuerier.getPageByPageTitle(pageTitle);

			DatabaseQuerier.addPage(this.databasePath, wikiPage);

			try {

				return DatabaseQuerier.getPageByPageTitle(this.databasePath, pageTitle);

			} catch (final ExceptionPageNotStored e) {

				throw new RuntimeException("Failed to add page to database", e);

			}

		}

		throw new ExceptionPageDoesNotExistOnWiki(pageTitle.toString());

	}

}
