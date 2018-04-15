
package com.kileyowen.degrees_of_separation.wikipedia;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.jdt.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.kileyowen.degrees_of_separation.PageTitle;
import com.kileyowen.utils.NullUtils;

public class WikipediaQuerier {

	protected static final String PROP_VALUE_LINKS_HERE = "linkshere";

	protected static final String LINKS_HERE_PROP_VALUE_PAGE_ID = "pageid";

	protected static final String LINKS_HERE_PROP_VALUE_PAGE_ID_AND_TITLE = "pageid%7Ctitle";

	protected static final String NAMESPACE_MAIN = "0";

	protected static final String LINKS_HERE_LIMIT_MAX = "500";

	private static final String BASE_URL = "https://en.wikipedia.org/w/api.php?action=query&format=json";

	/**
	 * Root Level
	 * Marks all results collected
	 */
	private static final String BATCH_COMPLETE = "batchcomplete";

	/**
	 * Root Level
	 * Marks Batch Not Complete
	 * Contains Continue Values
	 */
	private static final String CONTINUE = "continue";

	/**
	 * Root Level
	 * Entry for Data
	 */
	private static final String QUERY = "query";

	/**
	 * Query Level
	 * Entry for Pages in Query
	 */
	private static final String PAGES = "pages";

	/**
	 * Page Level
	 * Marks Wikipedia PageId in Page
	 *
	 * Links Here key Value Object Level
	 * Key Value for int of pageid
	 */
	private static final String PAGE_ID = "pageid";

	/**
	 * Continue Level
	 * Key for Links Here Continue Value
	 */
	private static final String LINKS_HERE_CONTINUE = "lhcontinue";

	/**
	 * Page Level
	 * Contains Array of Objects that contain PageId:####
	 */
	private static final String LINKS_HERE = "linkshere";

	/**
	 * Page Level
	 * Key Value for Title of Page
	 */
	private static final String PAGE_TITLE = "title";

	//	/**
	//	 * Query Level
	//	 * Contains an array of redirect objects
	//	 */
	//	private static final String REDIRECTS = "redirects";
	//
	//	private static final String FROM = "from";
	//
	//	private static final String TO = "to";

	private static String addUrlParameter(final String original, @Nullable final String key, final String value) {

		final StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(original);

		stringBuilder.append(String.format("&%s=%s", key, value));

		return NullUtils.assertNotNull(stringBuilder.toString(), "StringBuilder made null");

	}

	public static final WikiPage getPageByPageTitle(final PageTitle pageTitle) {

		final HashMap<WikiRequestKey, String> pairs = new HashMap<>();

		pairs.put(WikiRequestKey.PAGE_TITLES, pageTitle.getWikiPageTitle());

		pairs.put(WikiRequestKey.REDIRECT, "1");

		final JSONObject rootJson = WikipediaQuerier.runQuery(pairs);

		if (rootJson.has(WikipediaQuerier.BATCH_COMPLETE)) {

			if (rootJson.has(WikipediaQuerier.QUERY)) {

				final JSONObject queryObj = rootJson.getJSONObject(WikipediaQuerier.QUERY);

				if (queryObj.has(WikipediaQuerier.PAGES)) {

					final JSONObject pagesObj = queryObj.getJSONObject(WikipediaQuerier.PAGES);

					for (final String pagesKey : pagesObj.keySet()) {

						final JSONObject pageObj = pagesObj.getJSONObject(pagesKey);

						if (pageObj.has(WikipediaQuerier.PAGE_ID) && pageObj.has(WikipediaQuerier.PAGE_TITLE)) {

							final String pageTitleValue = pageObj.getString(WikipediaQuerier.PAGE_TITLE);

							return new WikiPage(new WikiPageId(pageObj.getInt(WikipediaQuerier.PAGE_ID)), new PageTitle(NullUtils.assertNotNull(pageTitleValue, "Page Title Value was null")));

						}

					}

				}

			}

		}

		throw new RuntimeException("Something went wrong when parsing");

	}

	public static final List<WikiPage> getPageLinksByPage(final WikiPageId wikiPageId) {

		final HashMap<WikiRequestKey, String> pairs = new HashMap<>();

		pairs.put(WikiRequestKey.PAGE_IDS, wikiPageId.toString());

		return WikipediaQuerier.getPageLinksByPagePairs(pairs);

	}

	private static final List<WikiPage> getPageLinksByPagePairs(final HashMap<WikiRequestKey, String> pairs) {

		pairs.put(WikiRequestKey.PROP, WikipediaQuerier.PROP_VALUE_LINKS_HERE);

		pairs.put(WikiRequestKey.LINKS_HERE_PROP, WikipediaQuerier.LINKS_HERE_PROP_VALUE_PAGE_ID_AND_TITLE);

		pairs.put(WikiRequestKey.LINKS_HERE_NAMESPACE, WikipediaQuerier.NAMESPACE_MAIN);

		pairs.put(WikiRequestKey.LINKS_HERE_LIMIT, WikipediaQuerier.LINKS_HERE_LIMIT_MAX);

		final JSONObject rootJson = WikipediaQuerier.runQuery(pairs);

		final List<WikiPage> pages = new ArrayList<>();

		if (rootJson.has(WikipediaQuerier.CONTINUE)) {

			Optional<WikiPageId> pageIdOpt = Optional.empty();

			if (rootJson.has(WikipediaQuerier.QUERY)) {

				final JSONObject queryObj = rootJson.getJSONObject(WikipediaQuerier.QUERY);

				if (queryObj.has(WikipediaQuerier.PAGES)) {

					final JSONObject pagesObj = queryObj.getJSONObject(WikipediaQuerier.PAGES);

					for (final String pagesKey : pagesObj.keySet()) {

						final JSONObject pageObj = pagesObj.getJSONObject(pagesKey);

						if (pageObj.has(WikipediaQuerier.PAGE_ID)) {

							pageIdOpt = Optional.of(new WikiPageId(pageObj.getInt(WikipediaQuerier.PAGE_ID)));

						}

					}

				}

			}

			final JSONObject continueObj = rootJson.getJSONObject(WikipediaQuerier.CONTINUE);

			if (continueObj.has(WikipediaQuerier.LINKS_HERE_CONTINUE)) {

				final int linksHereContinue = continueObj.getInt(WikipediaQuerier.LINKS_HERE_CONTINUE);

				pages.addAll(WikipediaQuerier.getPageLinksByPageWithContinue(pageIdOpt.get(), NullUtils.assertNotNull(Integer.toString(linksHereContinue), "Integer to string was null")));

			}

		}

		if (rootJson.has(WikipediaQuerier.QUERY)) {

			final JSONObject queryObj = rootJson.getJSONObject(WikipediaQuerier.QUERY);

			if (queryObj.has(WikipediaQuerier.PAGES)) {

				final JSONObject pagesObj = queryObj.getJSONObject(WikipediaQuerier.PAGES);

				for (final String pagesKey : pagesObj.keySet()) {

					final JSONObject pageObj = pagesObj.getJSONObject(pagesKey);

					if (pageObj.has(WikipediaQuerier.LINKS_HERE)) {

						final JSONArray linksHereArray = pageObj.getJSONArray(WikipediaQuerier.LINKS_HERE);

						for (final Object keyValueObj : linksHereArray) {

							if (!(keyValueObj instanceof JSONObject)) {

								throw new RuntimeException("KeyValueObj was not JSONObject");

							}

							final JSONObject keyValueJsonObj = (JSONObject) keyValueObj;

							if (keyValueJsonObj.has(WikipediaQuerier.PAGE_ID) && keyValueJsonObj.has(WikipediaQuerier.PAGE_TITLE)) {

								pages.add(new WikiPage(new WikiPageId(keyValueJsonObj.getInt(WikipediaQuerier.PAGE_ID)), new PageTitle(NullUtils.assertNotNull(keyValueJsonObj.getString(WikipediaQuerier.PAGE_TITLE), "Json Value String was null"))));

							}

						}

					}

				}

			}

		}

		return pages;

	}

	public static final List<WikiPage> getPageLinksByPageWithContinue(final WikiPageId wikiPageId, final String lhcontinue) {

		final HashMap<WikiRequestKey, String> pairs = new HashMap<>();

		pairs.put(WikiRequestKey.PAGE_IDS, wikiPageId.toString());

		pairs.put(WikiRequestKey.LINKS_HERE_CONTINUE, lhcontinue);

		return WikipediaQuerier.getPageLinksByPagePairs(pairs);

	}

	private static final JSONObject runQuery(final HashMap<WikiRequestKey, String> pairs) {

		String urlString = WikipediaQuerier.BASE_URL;

		for (final Map.Entry<WikiRequestKey, String> entry : pairs.entrySet()) {

			urlString = WikipediaQuerier.addUrlParameter(urlString, entry.getKey().toString(), entry.getValue());

		}

		try {

			final URL url = new URL(urlString);

			final HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("GET");

			return new JSONObject(new JSONTokener(con.getInputStream()));

		} catch (final MalformedURLException e) {

			throw new RuntimeException(e);

		} catch (final IOException e) {

			throw new RuntimeException(e);

		}

	}

}
