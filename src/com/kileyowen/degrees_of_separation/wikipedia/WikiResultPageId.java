
package com.kileyowen.degrees_of_separation.wikipedia;

import org.json.JSONException;
import org.json.JSONObject;

import com.kileyowen.degrees_of_separation.ExceptionWikiQueryError;
import com.kileyowen.degrees_of_separation.Page;
import com.kileyowen.degrees_of_separation.PageId;
import com.kileyowen.utils.ExceptionNull;
import com.kileyowen.utils.NullUtils;

public class WikiResultPageId extends WikiResult {

	private final Page page;

	public WikiResultPageId(final JSONObject rootJson) throws ExceptionWikiQueryError, JSONException, ExceptionNull {

		if (rootJson.has(WikiResult.BATCH_COMPLETE)) {

			if (rootJson.has(WikiResult.QUERY)) {

				final JSONObject queryObj = rootJson.getJSONObject(WikiResult.QUERY);

				if (queryObj.has(WikiResult.PAGES)) {

					final JSONObject pagesObj = queryObj.getJSONObject(WikiResult.PAGES);

					for (final String pagesKey : pagesObj.keySet()) {

						final JSONObject pageObj = pagesObj.getJSONObject(pagesKey);

						if (pageObj.has(WikiResult.PAGE_ID) && pageObj.has(WikiResult.PAGE_TITLE)) {

							this.page = new Page(new PageId(pageObj.getInt(WikiResult.PAGE_ID)), NullUtils.assertNotNull(pageObj.getString(WikiResult.PAGE_TITLE), "PageTitle was null"));
							return;
						}

					}

				}

			}

		}

		throw new ExceptionWikiQueryError("Unable to Read PageId from query");

	}

	public Page getPage() {

		return this.page;

	}

}
