
package com.kileyowen.degrees_of_separation;

import java.util.List;

import com.kileyowen.degrees_of_separation.database.DatabaseRead;
import com.kileyowen.degrees_of_separation.database.DatabaseRequestLinksHere;
import com.kileyowen.degrees_of_separation.database.DatabaseWrite;
import com.kileyowen.degrees_of_separation.database.ExceptionPageLinksNotStored;
import com.kileyowen.degrees_of_separation.database.ExceptionPageNotStored;
import com.kileyowen.degrees_of_separation.wikipedia.ExceptionPageDoesNotExistOnWiki;
import com.kileyowen.degrees_of_separation.wikipedia.WikiPage;
import com.kileyowen.degrees_of_separation.wikipedia.WikiPageId;
import com.kileyowen.degrees_of_separation.wikipedia.WikiRequestLinksHere;
import com.kileyowen.degrees_of_separation.wikipedia.WikiRequestWikiPage;

public class Querier {

	private static List<Page> getLinksHereByPageDatabase(final Page page, final String databasePath) throws ExceptionPageLinksNotStored {

		return new DatabaseRequestLinksHere(page, databasePath).build().getPages();

	}

	private static List<WikiPage> getLinksHereByWikiPageIdWikipedia(final WikiPageId wikiPageId) {

		return new WikiRequestLinksHere(wikiPageId).build().getPages();

	}

	private static Page getPageByPageTitleDatabase(final PageTitle pageTitle, final String databasePath) throws ExceptionPageNotStored {

		return DatabaseRead.getPageByPageTitle(databasePath, pageTitle);

	}

	private static WikiPage getPageByPageTitleWikipedia(final PageTitle pageTitle) throws ExceptionPageDoesNotExistOnWiki {

		return new WikiRequestWikiPage(pageTitle).build().getPage();

	}

	private final boolean online;

	private final String databasePath;

	public Querier(final boolean newOnline, final String newDatabasePath) {

		this.online = newOnline;

		this.databasePath = newDatabasePath;

		DatabaseWrite.initDatabase(newDatabasePath);

	}

	private void addPageLinksToDatabase(final Page page, final List<WikiPage> wikiPages) {

	}

	private void addPageToDatabase(final WikiPage wikiPage) {

		DatabaseWrite.addPage(this.databasePath, wikiPage);

	}

	public List<Page> getLinksHereByPage(final Page page) throws ExceptionPageLinksNotStored {

		try {

			return Querier.getLinksHereByPageDatabase(page, this.databasePath);

		} catch (final ExceptionPageLinksNotStored e) {

			e.printStackTrace();

		}

		if (this.online) {

			final List<WikiPage> wikiPages = Querier.getLinksHereByWikiPageIdWikipedia(page.getWikiPageId());

			this.addPageLinksToDatabase(page, wikiPages);

			try {

				return Querier.getLinksHereByPageDatabase(page, this.databasePath);

			} catch (final ExceptionPageLinksNotStored e) {

				throw new RuntimeException("Failed to add page links to database", e);

			}

		}

		throw new ExceptionPageLinksNotStored(String.format("Links not stored for Page: %s", page));

	}

	public Page getPageByPageTitle(final PageTitle pageTitle) throws ExceptionPageDoesNotExistOnWiki {

		try {

			return Querier.getPageByPageTitleDatabase(pageTitle, this.databasePath);

		} catch (final ExceptionPageNotStored e) {

			e.printStackTrace();

		}

		if (this.online) {

			final WikiPage wikiPage = Querier.getPageByPageTitleWikipedia(pageTitle);

			this.addPageToDatabase(wikiPage);

			try {

				return Querier.getPageByPageTitleDatabase(pageTitle, this.databasePath);

			} catch (final ExceptionPageNotStored e) {

				throw new RuntimeException("Failed to add page to database", e);

			}

		}

		throw new ExceptionPageDoesNotExistOnWiki(pageTitle.toString());

	}

}
