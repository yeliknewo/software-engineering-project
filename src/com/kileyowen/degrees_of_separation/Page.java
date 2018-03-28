
package com.kileyowen.degrees_of_separation;

import org.eclipse.jdt.annotation.Nullable;

import com.kileyowen.degrees_of_separation.database.DatabasePageId;
import com.kileyowen.degrees_of_separation.wikipedia.WikiPageId;

public class Page {

	private final DatabasePageId databasePageId;

	private final WikiPageId wikiPageId;

	private final PageTitle pageTitle;

	public Page(final DatabasePageId newDatabasePageId, final WikiPageId newWikiPageId, final PageTitle newPageTitle) {

		this.databasePageId = newDatabasePageId;

		this.wikiPageId = newWikiPageId;

		this.pageTitle = newPageTitle;

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
		final Page other = (Page) obj;
		if (!this.getDatabasePageId().equals(other.getDatabasePageId())) {
			return false;
		}
		if (!this.getPageTitle().equals(other.getPageTitle())) {
			return false;
		}
		if (!this.getWikiPageId().equals(other.getWikiPageId())) {
			return false;
		}
		return true;
	}

	public DatabasePageId getDatabasePageId() {

		return this.databasePageId;
	}

	public PageTitle getPageTitle() {

		return this.pageTitle;
	}

	public WikiPageId getWikiPageId() {

		return this.wikiPageId;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + this.getDatabasePageId().hashCode();
		result = prime * result + this.getPageTitle().hashCode();
		result = prime * result + this.getWikiPageId().hashCode();
		return result;
	}

	@Override
	public String toString() {

		return "Page [databasePageId=" + this.getDatabasePageId() + ", wikiPageId=" + this.getWikiPageId() + ", pageTitle=" + this.getPageTitle() + "]";
	}

}
