
package com.kileyowen.degrees_of_separation;

import java.util.Optional;

import com.kileyowen.utils.ExceptionNull;

public class Node {

	private final Optional<Node> fromOpt;

	private final int distance;

	private final Page page;

	public Node(final Page newPage) {

		this.page = newPage;

		this.fromOpt = Optional.empty();

		this.distance = 0;

	}

	public Node(final Page newPage, final Node newFrom, final int newDistance) {

		this.page = newPage;

		this.fromOpt = Optional.of(newFrom);

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

	public boolean hasFrom() {

		return this.fromOpt.isPresent();

	}

}
