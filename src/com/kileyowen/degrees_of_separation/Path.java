
package com.kileyowen.degrees_of_separation;

import java.util.ArrayList;
import java.util.List;

import com.kileyowen.utils.ExceptionNull;

public class Path {

	private final List<Page> pagesInOrder;

	public Path(final Node node) {

		this.pagesInOrder = new ArrayList<>();

		Node currentNode = node;

		while (currentNode.hasFrom()) {

			this.pagesInOrder.add(currentNode.getPage());

			try {
				currentNode = currentNode.getFrom();

			} catch (final ExceptionNull e) {

				throw new RuntimeException(e);

			}

		}

		this.pagesInOrder.add(currentNode.getPage());

	}

}
