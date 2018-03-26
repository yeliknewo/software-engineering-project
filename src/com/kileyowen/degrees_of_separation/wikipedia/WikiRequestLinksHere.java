
package com.kileyowen.degrees_of_separation.wikipedia;

import java.io.IOException;

import com.kileyowen.degrees_of_separation.ExceptionWikiQueryError;
import com.kileyowen.degrees_of_separation.PageId;
import com.kileyowen.utils.ExceptionNull;
import com.kileyowen.utils.NullUtils;

public class WikiRequestLinksHere extends WikiRequest<WikiResultLinksHere> {

	public WikiRequestLinksHere(final PageId pageId) throws ExceptionNull {

		this.setup(pageId);

	}

	WikiRequestLinksHere(final PageId pageId, final String linksHereContinue) throws ExceptionNull {

		this.setup(pageId);

		this.addKeyValuePair(WikiRequestKey.LINKS_HERE_CONTINUE, linksHereContinue);

	}

	@Override
	public WikiResultLinksHere build() throws IOException, ExceptionNull, ExceptionWikiQueryError {

		return new WikiResultLinksHere(this.buildInternal());
	}

	private void setup(final PageId pageId) throws ExceptionNull {

		this.addKeyValuePair(WikiRequestKey.PAGE_IDS, NullUtils.assertNotNull(pageId.toString(), "PageId.toString was null"));

		this.addKeyValuePair(WikiRequestKey.PROP, WikiRequest.PROP_VALUE_LINKS_HERE);

		this.addKeyValuePair(WikiRequestKey.LINKS_HERE_PROP, WikiRequest.LINKS_HERE_PROP_VALUE_PAGE_ID_AND_TITLE);

		this.addKeyValuePair(WikiRequestKey.LINKS_HERE_NAMESPACE, WikiRequest.NAMESPACE_MAIN);

		this.addKeyValuePair(WikiRequestKey.LINKS_HERE_LIMIT, WikiRequest.LINKS_HERE_LIMIT_MAX);

	}

}
