
package com.kileyowen.degrees_of_separation.wikipedia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kileyowen.degrees_of_separation.ExceptionWikiQueryError;
import com.kileyowen.degrees_of_separation.Page;
import com.kileyowen.degrees_of_separation.PageId;
import com.kileyowen.utils.ExceptionNull;
import com.kileyowen.utils.NullUtils;

public class WikiResultLinksHere extends WikiResult {

	private final List<Page> pages;

	public WikiResultLinksHere(final JSONObject rootJson) throws IOException, ExceptionNull, ExceptionWikiQueryError {

		this.pages = new ArrayList<>();

		if (rootJson.has(WikiResult.CONTINUE)) {

			Optional<PageId> pageIdOpt = Optional.empty();

			if (rootJson.has(WikiResult.QUERY)) {
				final JSONObject queryObj = rootJson.getJSONObject(WikiResult.QUERY);

				if (queryObj.has(WikiResult.PAGES)) {

					final JSONObject pagesObj = queryObj.getJSONObject(WikiResult.PAGES);

					for (final String pagesKey : pagesObj.keySet()) {

						final JSONObject pageObj = pagesObj.getJSONObject(pagesKey);

						if (pageObj.has(WikiResult.PAGE_ID)) {

							pageIdOpt = Optional.of(new PageId(pageObj.getInt(WikiResult.PAGE_ID)));

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

				this.pages.addAll(new WikiRequestLinksHere(pageIdOpt.get(), NullUtils.assertNotNull(Integer.toString(linksHereContinue), "Integer.ToString was null")).build().getPages());

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

								throw new ExceptionWikiQueryError("KeyValueObj was not JSONObject");

							}

							final JSONObject keyValueJsonObj = (JSONObject) keyValueObj;

							if (keyValueJsonObj.has(WikiResult.PAGE_ID) && keyValueJsonObj.has(WikiResult.PAGE_TITLE)) {

								this.pages.add(new Page(new PageId(keyValueJsonObj.getInt(WikiResult.PAGE_ID)), NullUtils.assertNotNull(keyValueJsonObj.getString(WikiResult.PAGE_TITLE), "PageTitle was null")));

							}

						}

					}

				}

			}

		}

	}

	public List<Page> getPages() {

		return this.pages;

	}

}
