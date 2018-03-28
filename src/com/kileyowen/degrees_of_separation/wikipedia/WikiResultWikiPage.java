
package com.kileyowen.degrees_of_separation.wikipedia;

import org.json.JSONObject;

import com.kileyowen.degrees_of_separation.PageTitle;

public class WikiResultWikiPage extends WikiResult {

	private final WikiPage page;

	public WikiResultWikiPage(final JSONObject rootJson) throws ExceptionPageDoesNotExistOnWiki {

		if (rootJson.has(WikiResult.BATCH_COMPLETE)) {

			if (rootJson.has(WikiResult.QUERY)) {

				final JSONObject queryObj = rootJson.getJSONObject(WikiResult.QUERY);

				if (queryObj.has(WikiResult.PAGES)) {

					final JSONObject pagesObj = queryObj.getJSONObject(WikiResult.PAGES);

					for (final String pagesKey : pagesObj.keySet()) {

						final JSONObject pageObj = pagesObj.getJSONObject(pagesKey);

						if (pageObj.has(WikiResult.PAGE_ID) && pageObj.has(WikiResult.PAGE_TITLE)) {

							final String pageTitleValue = pageObj.getString(WikiResult.PAGE_TITLE);

							pageTitleValue.replaceAll(" ", "_");

							this.page = new WikiPage(new WikiPageId(pageObj.getInt(WikiResult.PAGE_ID)), PageTitle.makePageTitleByWikiPageTitle(pageTitleValue));

							return;

						}

					}

				}

			}

		}

		throw new ExceptionPageDoesNotExistOnWiki("Page does not exist");

	}

	public WikiPage getPage() {

		return this.page;

	}

}
