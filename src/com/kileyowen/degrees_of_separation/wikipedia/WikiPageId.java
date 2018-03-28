
package com.kileyowen.degrees_of_separation.wikipedia;

import org.eclipse.jdt.annotation.Nullable;

public class WikiPageId {

	private final int wikiPageId;

	public WikiPageId(final int newWikiPageId) {

		this.wikiPageId = newWikiPageId;

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
		final WikiPageId other = (WikiPageId) obj;
		if (this.wikiPageId != other.wikiPageId) {
			return false;
		}
		return true;
	}

	public int getPageId() {

		return this.wikiPageId;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + this.wikiPageId;
		return result;
	}

	@Override
	public String toString() {

		return Integer.toString(this.wikiPageId);

	}

}
