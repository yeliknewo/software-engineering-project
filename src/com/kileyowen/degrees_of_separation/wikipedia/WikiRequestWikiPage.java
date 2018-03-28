
package com.kileyowen.degrees_of_separation.wikipedia;

import com.kileyowen.degrees_of_separation.PageTitle;

public class WikiRequestWikiPage extends WikiRequest<WikiResultWikiPage> {

	public WikiRequestWikiPage(final PageTitle pageTitle) {

		this.addKeyValuePair(WikiRequestKey.PAGE_TITLES, pageTitle.getWikiPageTitle());

	}

	@Override
	public WikiResultWikiPage build() throws ExceptionPageDoesNotExistOnWiki {

		return new WikiResultWikiPage(this.buildInternal());

	}

}
