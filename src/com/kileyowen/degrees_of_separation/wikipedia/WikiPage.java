
package com.kileyowen.degrees_of_separation.wikipedia;

import com.kileyowen.degrees_of_separation.PageTitle;

public class WikiPage {

	private final WikiPageId wikiPageId;

	private final PageTitle pageTitle;

	public WikiPage(final WikiPageId newWikiPageId, final PageTitle newPageTitle) {

		this.wikiPageId = newWikiPageId;

		this.pageTitle = newPageTitle;

	}

	public final PageTitle getPageTitle() {

		return this.pageTitle;

	}

	public final WikiPageId getWikiPageId() {

		return this.wikiPageId;

	}

}
