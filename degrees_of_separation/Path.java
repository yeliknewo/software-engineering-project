
package com.kileyowen.degrees_of_separation;

import java.util.ArrayList;
import java.util.List;

import com.kileyowen.utils.ExceptionNull;

public class Path {

	private final List<Page> pagesInOrder;

	public Path(final Node node) throws ExceptionNull {

		this.pagesInOrder = new ArrayList<>();

		Node currentNode = node;

		while (currentNode.hasFrom()) {

			this.pagesInOrder.add(currentNode.getPage());

			currentNode = currentNode.getFrom();

		}

		this.pagesInOrder.add(currentNode.getPage());

	}

}
