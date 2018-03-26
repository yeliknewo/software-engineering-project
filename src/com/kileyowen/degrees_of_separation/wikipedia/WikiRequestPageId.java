
package com.kileyowen.degrees_of_separation.wikipedia;

import java.io.IOException;

import com.kileyowen.degrees_of_separation.ExceptionWikiQueryError;
import com.kileyowen.utils.ExceptionNull;

public class WikiRequestPageId extends WikiRequest<WikiResultPageId> {

	public WikiRequestPageId(final String pageTitle) {

		this.addKeyValuePair(WikiRequestKey.PAGE_TITLES, pageTitle);

	}

	@Override
	public WikiResultPageId build() throws IOException, ExceptionNull, ExceptionWikiQueryError {

		return new WikiResultPageId(this.buildInternal());

	}

}
