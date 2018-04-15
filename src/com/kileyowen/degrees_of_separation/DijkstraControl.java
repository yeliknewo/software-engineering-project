
package com.kileyowen.degrees_of_separation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.Nullable;

import com.kileyowen.degrees_of_separation.database.ExceptionPageLinksNotStored;
import com.kileyowen.degrees_of_separation.wikipedia.ExceptionPageDoesNotExistOnWiki;
import com.kileyowen.utils.NullUtils;

public class DijkstraControl {

	private static final boolean listContainsNodeWithPage(final @Nullable Page searchValue, final List<Node> nodes) {

		if (searchValue == null) {

			return false;

		}

		return nodes.parallelStream().anyMatch((final Node node) -> {

			return node.getPage().equals(searchValue);

		});

	}

	private final Querier querier;

	private final ConcurrentLinkedQueue<String> pageTitles;

	public DijkstraControl(final boolean online, final ConcurrentLinkedQueue<String> titles) {

		this.pageTitles = titles;

		this.querier = new Querier(online);

	}

	public void commit() {

		this.querier.commit();

	}

	public Path runSearch(final @Nullable PageTitle startPageTitle, final PageTitle endPageTitle) throws ExceptionBadStartPageTitle, ExceptionBadEndPageTitle, ExceptionPageLinksNotStored {

		final List<Node> openPages = new ArrayList<>();

		final List<Node> closedPages = new ArrayList<>();

		@Nullable
		Page startPage;

		try {

			if (startPageTitle == null) {

				startPage = null;
			} else {

				startPage = this.querier.getPageByPageTitle(startPageTitle);
			}

		} catch (final ExceptionPageDoesNotExistOnWiki e) {

			throw new ExceptionBadStartPageTitle("Start Page Title does not exist on Wikipedia", e);

		}

		Page endPage;

		try {

			endPage = this.querier.getPageByPageTitle(endPageTitle);

		} catch (final ExceptionPageDoesNotExistOnWiki e) {

			throw new ExceptionBadEndPageTitle("End Page Title does not exist on Wikipedia", e);

		}

		openPages.add(new Node(endPage));

		while (!DijkstraControl.listContainsNodeWithPage(startPage, closedPages)) {

			openPages.sort((final Node a, final Node b) -> {

				return Integer.compare(a.getDistance(), b.getDistance());

			});

			final Node openNode = openPages.remove(0);

			this.pageTitles.add(openNode.getPage().getPageTitle().getRawPageTitle());

			this.querier.getLinksHereByPage(openNode.getPage()).stream().forEach((final Page page) -> {

				if (!Stream.concat(closedPages.stream(), openPages.stream()).anyMatch((final Node node) -> {

					return node.getPage().equals(page);

				})) {
					openPages.add(new Node(NullUtils.assertNotNull(page, "Page was null"), openNode, openNode.getDistance() + 1));

				}

			});

			closedPages.add(openNode);

			this.commit();

		}

		return new Path(closedPages.stream().filter((final Node node) -> {

			return node.getPage().equals(startPage);

		}).collect(Collectors.toList()).get(0));

	}

}
