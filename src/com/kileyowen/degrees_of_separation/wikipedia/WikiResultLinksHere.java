
package com.kileyowen.degrees_of_separation.wikipedia;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kileyowen.degrees_of_separation.PageTitle;

public class WikiResultLinksHere extends WikiResult {

	private final List<WikiPage> pages;

	public WikiResultLinksHere(final JSONObject rootJson) {

		this.pages = new ArrayList<>();

		if (rootJson.has(WikiResult.CONTINUE)) {

			Optional<WikiPageId> pageIdOpt = Optional.empty();

			if (rootJson.has(WikiResult.QUERY)) {
				final JSONObject queryObj = rootJson.getJSONObject(WikiResult.QUERY);

				if (queryObj.has(WikiResult.PAGES)) {

					final JSONObject pagesObj = queryObj.getJSONObject(WikiResult.PAGES);

					for (final String pagesKey : pagesObj.keySet()) {

						final JSONObject pageObj = pagesObj.getJSONObject(pagesKey);

						if (pageObj.has(WikiResult.PAGE_ID)) {

							pageIdOpt = Optional.of(new WikiPageId(pageObj.getInt(WikiResult.PAGE_ID)));

						}

						//						if (pageObj.has(WikiResult.PAGE_TITLE)) {
						//
						//							System.out.println(pageObj.getString(WikiResult.PAGE_TITLE));
						//
						//						}

					}

				}

			}

			final JSONObject continueObj = rootJson.getJSONObject(WikiResult.CONTINUE);

			if (continueObj.has(WikiResult.LINKS_HERE_CONTINUE)) {

				final int linksHereContinue = continueObj.getInt(WikiResult.LINKS_HERE_CONTINUE);

				this.pages.addAll(new WikiRequestLinksHere(pageIdOpt.get(), Integer.toString(linksHereContinue)).build().getPages());

			}

		}

		if (rootJson.has(WikiResult.QUERY)) {
			final JSONObject queryObj = rootJson.getJSONObject(WikiResult.QUERY);

			if (queryObj.has(WikiResult.PAGES)) {

				final JSONObject pagesObj = queryObj.getJSONObject(WikiResult.PAGES);

				for (final String pagesKey : pagesObj.keySet()) {

					final JSONObject pageObj = pagesObj.getJSONObject(pagesKey);

					if (pageObj.has(WikiResult.LINKS_HERE)) {

						final JSONArray linksHereArray = pageObj.getJSONArray(WikiResult.LINKS_HERE);

						for (final Object keyValueObj : linksHereArray) {

							if (!(keyValueObj instanceof JSONObject)) {

								throw new RuntimeException("KeyValueObj was not JSONObject");

							}

							final JSONObject keyValueJsonObj = (JSONObject) keyValueObj;

							if (keyValueJsonObj.has(WikiResult.PAGE_ID) && keyValueJsonObj.has(WikiResult.PAGE_TITLE)) {

								this.pages.add(new WikiPage(new WikiPageId(keyValueJsonObj.getInt(WikiResult.PAGE_ID)), PageTitle.makePageTitleByWikiPageTitle(keyValueJsonObj.getString(WikiResult.PAGE_TITLE))));

							}

						}

					}

				}

			}

		}

	}

	public List<WikiPage> getPages() {

		return this.pages;

	}

}
