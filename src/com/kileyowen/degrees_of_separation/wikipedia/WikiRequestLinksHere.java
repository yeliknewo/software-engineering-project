
package com.kileyowen.degrees_of_separation.wikipedia;

public class WikiRequestLinksHere extends WikiRequest<WikiResultLinksHere> {

	public WikiRequestLinksHere(final WikiPageId wikiPageId) {

		this.setup(wikiPageId);

	}

	WikiRequestLinksHere(final WikiPageId wikiPageId, final String linksHereContinue) {

		this.setup(wikiPageId);

		this.addKeyValuePair(WikiRequestKey.LINKS_HERE_CONTINUE, linksHereContinue);

	}

	@Override
	public WikiResultLinksHere build() {

		return new WikiResultLinksHere(this.buildInternal());
	}

	private void setup(final WikiPageId wikiPageId) {

		this.addKeyValuePair(WikiRequestKey.PAGE_IDS, wikiPageId.toString());

		this.addKeyValuePair(WikiRequestKey.PROP, WikiRequest.PROP_VALUE_LINKS_HERE);

		this.addKeyValuePair(WikiRequestKey.LINKS_HERE_PROP, WikiRequest.LINKS_HERE_PROP_VALUE_PAGE_ID_AND_TITLE);

		this.addKeyValuePair(WikiRequestKey.LINKS_HERE_NAMESPACE, WikiRequest.NAMESPACE_MAIN);

		this.addKeyValuePair(WikiRequestKey.LINKS_HERE_LIMIT, WikiRequest.LINKS_HERE_LIMIT_MAX);

	}

}
