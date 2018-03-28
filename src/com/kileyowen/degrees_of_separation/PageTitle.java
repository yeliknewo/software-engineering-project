//

package com.kileyowen.degrees_of_separation;

import org.eclipse.jdt.annotation.Nullable;

public class PageTitle {

	private static final String databaseToWiki(final String databasePageTitle) {

		return databasePageTitle;//databasePageTitle.replaceAll(PageTitle.DATABASE_KEY, PageTitle.WIKI_KEY);

	}

	public static final PageTitle makePageTitleByDatabasePageTitle(final String databasePageTitle) {

		return new PageTitle(PageTitle.databaseToWiki(databasePageTitle));

	}

	public static final PageTitle makePageTitleByWikiPageTitle(final String wikiPageTitle) {

		return new PageTitle(wikiPageTitle);

	}

	private static final String wikiToDatabase(final String wikiPageTitle) {

		return wikiPageTitle.replaceAll(" ", "_").replaceAll("\"", "\\\"");

	}

	final String wikiPageTitle;

	private PageTitle(final String newWikiPageTitle) {

		this.wikiPageTitle = newWikiPageTitle;

	}

	@Override
	public boolean equals(final @Nullable Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final PageTitle other = (PageTitle) obj;
		if (!this.wikiPageTitle.equals(other.wikiPageTitle)) {
			return false;
		}
		return true;
	}

	public String getDatabasePageTitle() {

		return PageTitle.wikiToDatabase(this.getWikiPageTitle());

	}

	public String getWikiPageTitle() {

		return this.wikiPageTitle;

	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + (this.wikiPageTitle == null ? 0 : this.wikiPageTitle.hashCode());
		return result;
	}

	@Override
	public String toString() {

		return "PageTitle [wikiPageTitle=" + this.wikiPageTitle + "]";
	}

}
