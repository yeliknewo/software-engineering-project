
package com.kileyowen.degrees_of_separation.wikipedia;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.jdt.annotation.Nullable;
import org.json.JSONObject;

import com.kileyowen.degrees_of_separation.ExceptionWikiQueryError;
import com.kileyowen.utils.ExceptionNull;
import com.kileyowen.utils.NullUtils;

public abstract class WikiRequest<T extends WikiResult> {

	protected static final String PROP_VALUE_LINKS_HERE = "linkshere";

	protected static final String LINKS_HERE_PROP_VALUE_PAGE_ID = "pageid";

	protected static final String LINKS_HERE_PROP_VALUE_PAGE_ID_AND_TITLE = "pageid%7Ctitle";

	protected static final String NAMESPACE_MAIN = "0";

	protected static final String LINKS_HERE_LIMIT_MAX = "500";

	private static final String BASE_URL = "https://en.wikipedia.org/w/api.php?action=query&format=json";

	private static String addUrlParameter(final String original, @Nullable final String key, final String value) throws ExceptionNull {

		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(original);
		stringBuilder.append(String.format("&%s=%s", key, value));
		return NullUtils.assertNotNull(stringBuilder.toString(), "String.format was null");

	}

	private final HashMap<WikiRequestKey, String> pairs;

	public WikiRequest() {

		this.pairs = new HashMap<>();

	}

	protected final void addKeyValuePair(final WikiRequestKey key, final String value) {

		this.pairs.put(key, value);

	}

	public abstract T build() throws IOException, ExceptionNull, ExceptionWikiQueryError;

	protected JSONObject buildInternal() throws ExceptionNull, IOException {

		String urlString = WikiRequest.BASE_URL;

		for (final Map.Entry<WikiRequestKey, String> entry : this.pairs.entrySet()) {

			urlString = WikiRequest.addUrlParameter(urlString, entry.getKey().toString(), entry.getValue());

		}

		final URL url = new URL(urlString);

		final HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("GET");

		String out = "";

		try (final Scanner scan = new Scanner(con.getInputStream())) {

			while (scan.hasNext()) {

				out += scan.next();

			}

		}

		return new JSONObject(out);

	}

}
