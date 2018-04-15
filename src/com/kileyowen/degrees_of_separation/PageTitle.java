
package com.kileyowen.degrees_of_separation;

import org.eclipse.jdt.annotation.Nullable;

import com.kileyowen.utils.NullUtils;

public class PageTitle {

	private final String rawPageTitle;

	public PageTitle(final String newWikiPageTitle) {

		this.rawPageTitle = newWikiPageTitle;

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
		if (!this.rawPageTitle.equals(other.rawPageTitle)) {
			return false;
		}
		return true;
	}

	public String getDatabasePageTitle() {

		return NullUtils.assertNotNull(this.rawPageTitle.replaceAll("'", "''").replaceAll(" ", "_"), "Database page Title was null");

	}

	public String getRawPageTitle() {

		return this.rawPageTitle;

	}

	public String getWikiPageTitle() {

		return NullUtils.assertNotNull(this.rawPageTitle.replaceAll(" ", "%20"), "Wiki Page Title was null");

	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + this.rawPageTitle.hashCode();
		return result;
	}

	@Override
	public String toString() {

		return "PageTitle [wikiPageTitle=" + this.rawPageTitle + "]";
	}

}
