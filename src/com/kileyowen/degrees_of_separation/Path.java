
package com.kileyowen.degrees_of_separation;

import java.util.ArrayList;
import java.util.List;

public class Path {

	private final List<Page> pagesInOrder;

	public Path(final Node node) {

		this.pagesInOrder = new ArrayList<>();

		Node currentNode = node;

		while (currentNode.hasFrom()) {

			this.pagesInOrder.add(currentNode.getPage());

			currentNode = currentNode.getFrom();

		}

		this.pagesInOrder.add(currentNode.getPage());

	}

	@Override
	public String toString() {

		return "Path [pagesInOrder=" + this.pagesInOrder + "]";
	}

}
