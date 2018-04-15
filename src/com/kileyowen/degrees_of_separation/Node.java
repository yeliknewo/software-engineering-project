
package com.kileyowen.degrees_of_separation;

import java.util.Optional;

import com.kileyowen.utils.ExceptionNull;
import com.kileyowen.utils.NullUtils;

public class Node {

	private final Optional<Node> fromOpt;

	private final int distance;

	private final Page page;

	public Node(final Page newPage) {

		this.page = newPage;

		this.fromOpt = NullUtils.assertNotNull(Optional.empty(), "Optional object was null");

		this.distance = 0;

	}

	public Node(final Page newPage, final Node newFrom, final int newDistance) {

		this.page = newPage;

		this.fromOpt = NullUtils.assertNotNull(Optional.of(newFrom), "Optional object was null");

		this.distance = newDistance;

	}

	public int getDistance() {

		return this.distance;

	}

	public Node getFrom() {

		if (this.hasFrom()) {

			return this.fromOpt.get();

		}

		throw new ExceptionNull("FromOpt is null");

	}

	public Page getPage() {

		return this.page;
	}

	public boolean hasFrom() {

		return this.fromOpt.isPresent();

	}

	@Override
	public String toString() {

		return "Node [fromOpt=" + this.fromOpt + ", distance=" + this.distance + ", page=" + this.page + "]";
	}

}
