
package com.kileyowen.degrees_of_separation;

public class PageTitle {

	private static final String DATABASE_KEY = "";

	private static final String WIKI_KEY = "";

	private static final String databaseToWiki(final String databasePageTitle) {

		return databasePageTitle.replaceAll(PageTitle.DATABASE_KEY, PageTitle.WIKI_KEY);

	}

	public static final PageTitle makePageTitleByDatabasePageTitle(final String databasePageTitle) {

		return new PageTitle(PageTitle.databaseToWiki(databasePageTitle));

	}

	public static final PageTitle makePageTitleByWikiPageTitle(final String wikiPageTitle) {

		return new PageTitle(wikiPageTitle);

	}

	private static final String wikiToDatabase(final String wikiPageTitle) {

		return wikiPageTitle.replaceAll(PageTitle.WIKI_KEY, PageTitle.DATABASE_KEY);

	}

	final String wikiPageTitle;

	private PageTitle(final String newWikiPageTitle) {

		this.wikiPageTitle = newWikiPageTitle.replaceAll(" ", "_");

	}

	public String getDatabasePageTitle() {

		return PageTitle.wikiToDatabase(this.getWikiPageTitle());

	}

	public String getWikiPageTitle() {

		return this.wikiPageTitle;

	}

}
