
package com.kileyowen.degrees_of_separation;

public class Page {

	private final PageId pageId;

	private final String title;

	public Page(final PageId newPageId, final String newTitle) {

		this.pageId = newPageId;

		this.title = newTitle;

	}

	public final PageId getPageId() {

		return this.pageId;

	}

	public String getTitle() {

		return this.title;
	}

}
