
package com.kileyowen.degrees_of_separation;

import java.util.Optional;

import com.kileyowen.utils.ExceptionNull;
import com.kileyowen.utils.NullUtils;

public class Node {

	private final Optional<Node> fromOpt;

	private final int distance;

	private final Page page;

	public Node(final Page newPage) throws ExceptionNull {

		this.page = newPage;

		this.fromOpt = NullUtils.assertNotNull(Optional.empty(), "Optional.Empty was null");

		this.distance = 0;

	}

	public Node(final Page newPage, final Node newFrom, final int newDistance) throws ExceptionNull {

		this.page = newPage;

		this.fromOpt = NullUtils.assertNotNull(Optional.of(newFrom), "Optional.Of was null");

		this.distance = newDistance;

	}

	public int getDistance() {

		return this.distance;

	}

	public Node getFrom() throws ExceptionNull {

		if (this.hasFrom()) {

			return this.fromOpt.get();

		}

		throw new ExceptionNull("FromOpt is null");

	}

	public Page getPage() {

		return this.page;
	}

	public PageId getPageId() {

		return this.getPage().getPageId();

	}

	public String getPageTitle() {

		return this.getPage().getTitle();

	}

	public boolean hasFrom() {

		return this.fromOpt.isPresent();

	}

}
