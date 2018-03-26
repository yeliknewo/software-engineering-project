
package com.kileyowen.degrees_of_separation;

import org.eclipse.jdt.annotation.Nullable;

public class PageId {

	private final int pageId;

	public PageId(final int newPageId) {

		this.pageId = newPageId;

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
		final PageId other = (PageId) obj;
		if (this.pageId != other.pageId) {
			return false;
		}
		return true;
	}

	public int getPageId() {

		return this.pageId;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + this.pageId;
		return result;
	}

	@Nullable
	@Override
	public String toString() {

		return Integer.toString(this.pageId);

	}

}
