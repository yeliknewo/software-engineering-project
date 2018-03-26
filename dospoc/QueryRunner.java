
package com.kileyowen.dospoc;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class QueryRunner {

	private static final String PAGEID = "pageid";
	private static final String LINKSHERE = "linkshere";
	private static final String PAGES = "pages";
	private static final String GET = "GET";
	private static final String LHCONTINUE = "lhcontinue";
	private static final String QUERY = "query";
	private static final String LHCONTINUE_APPEND_STRING_URL = "&lhcontinue=%s";
	private static final String PAGEID_APPEND_STRING_URL = "&pageids=%s";
	private static final String CONTINUE = "continue";
	private static final String BASE_URL = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=linkshere&lhnamespace=0&lhlimit=500&lhprop=pageid";

	private final static HttpURLConnection buildGet(final int pageId, final String lhcontinue) throws IOException {

		StringBuilder urlBuilder = new StringBuilder(BASE_URL);

		urlBuilder.append(String.format(PAGEID_APPEND_STRING_URL, Integer.toString(pageId)));

		if (lhcontinue != null) {

			urlBuilder.append(String.format(LHCONTINUE_APPEND_STRING_URL, lhcontinue));

		}

		final URL url = new URL(urlBuilder.toString());

		final HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod(GET);

		return con;

	}

	private final static JSONObject runGet(final int pageId, final String lhcontinue) throws IOException {

		final HttpURLConnection con = QueryRunner.buildGet(pageId, lhcontinue);

		String out = "";

		try (final Scanner scan = new Scanner(con.getInputStream())) {

			while (scan.hasNext()) {

				out += scan.next();

			}

		}

		return new JSONObject(out);

	}

	private final DataStorage dataStorage;

	public QueryRunner(final DataStorage dataStorage) {

		this.dataStorage = dataStorage;

	}

	private final void addJsonToData(final JSONObject rootJson) {

		int basePageId;

		Set<Integer> pageIds = new HashSet<>();

		if (rootJson.has(QUERY)) {

			final JSONObject queryObj = rootJson.getJSONObject(QUERY);

			if (queryObj.has(PAGES)) {

				final JSONObject pagesObj = queryObj.getJSONObject(PAGES);

				for (final String basePageIdI : pagesObj.keySet()) {

					basePageId = Integer.parseInt(basePageIdI);

					final JSONObject basePageObj = pagesObj.getJSONObject(basePageIdI);

					if (basePageObj.has(LINKSHERE)) {

						final JSONArray linksHereArray = basePageObj.getJSONArray(LINKSHERE);

						linksHereArray.forEach((linksHereObjObj) -> {

							if (linksHereObjObj instanceof JSONObject) {

								final JSONObject linksHereObj = (JSONObject) linksHereObjObj;

								if (linksHereObj.has(PAGEID)) {

									final int pageId = linksHereObj.getInt(PAGEID);

									pageIds.add(pageId);

								}

							}

						});

					}

				}

			}

		}
		
		this.dataStorage.addToDatabase(basePageId, pageIds);

	}

	public final void runQuery(final int pageId) throws IOException {

		String lhcontinue = null;

		while (true) {

			final JSONObject rootJson = QueryRunner.runGet(pageId, lhcontinue);

			this.addJsonToData(rootJson);

			if (rootJson.has(CONTINUE) && rootJson.getJSONObject(CONTINUE).has(LHCONTINUE)) {

				lhcontinue = rootJson.getJSONObject(CONTINUE).getString(LHCONTINUE);

			} else {

				break;

			}

		}

	}

}
