
package com.kileyowen.degrees_of_separation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.kileyowen.utils.ExceptionNull;
import com.kileyowen.utils.NullUtils;

public class DijkstraControl {

	private static final boolean listContainsNodeWithPageId(final PageId searchValue, final List<Node> nodes) {

		return nodes.parallelStream().anyMatch((final Node node) -> {
			return node.getPageId().equals(searchValue);
		});

	}

	private final Querier querier;

	public DijkstraControl(final boolean online) {

		this.querier = new Querier(online);

	}

	public Path runSearch(final String startPageTitle, final String endPageTitle) throws ExceptionPageDoesNotExist, IOException, ExceptionNull {

		final List<Node> openPages = new ArrayList<>();

		final List<Node> closedPages = new ArrayList<>();

		final Page startPage = this.querier.getPageByTitle(startPageTitle);

		final Page endPage = this.querier.getPageByTitle(endPageTitle);

		openPages.add(new Node(endPage));

		while (!DijkstraControl.listContainsNodeWithPageId(startPage.getPageId(), closedPages)) {

			openPages.sort((final Node a, final Node b) -> {

				return Integer.compare(a.getDistance(), b.getDistance());

			});

			final Node openNode = NullUtils.assertNotNull(openPages.remove(0), "Open Pages is empty");

			System.out.println(openNode.getPageTitle());

			this.querier.getLinksHereByPageId(openNode.getPageId()).stream().forEach((final Page page) -> {

				if (!Stream.concat(closedPages.stream(), openPages.stream()).anyMatch((final Node node) -> {

					return node.getPageId().equals(page.getPageId());

				})) {

					try {

						openPages.add(new Node(NullUtils.assertNotNull(page, "Page was null"), openNode, openNode.getDistance() + 1));

					} catch (final ExceptionNull e) {

						e.printStackTrace();

					}

				}

			});

			closedPages.add(openNode);

		}

		return new Path(closedPages.stream().filter((final Node node) -> {

			return node.getPageId().equals(startPage.getPageId());

		}).collect(Collectors.toList()).get(0));

	}

}
