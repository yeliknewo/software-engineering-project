
package com.kileyowen.degrees_of_separation.wikipedia;

import com.kileyowen.degrees_of_separation.PageTitle;

public class WikiPage {

	private final WikiPageId wikiPageId;

	private PageTitle pageTitle;

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

	@Override
	public String toString() {

		return "WikiPage [wikiPageId=" + this.wikiPageId + ", pageTitle=" + this.pageTitle + "]";
	}

	public void setPageTitle(PageTitle pageTitle) {

		this.pageTitle = pageTitle;
	}

}
