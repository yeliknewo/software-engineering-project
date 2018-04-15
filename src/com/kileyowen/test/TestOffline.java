
package com.kileyowen.test;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.kileyowen.degrees_of_separation.DijkstraControl;
import com.kileyowen.degrees_of_separation.ExceptionBadEndPageTitle;
import com.kileyowen.degrees_of_separation.ExceptionBadStartPageTitle;
import com.kileyowen.degrees_of_separation.PageTitle;
import com.kileyowen.degrees_of_separation.Path;
import com.kileyowen.degrees_of_separation.database.ExceptionPageLinksNotStored;

public class TestOffline {

	public static void main(final String[] args) {

		final ConcurrentLinkedQueue<String> pageTitles = new ConcurrentLinkedQueue<>();

		final DijkstraControl dc = new DijkstraControl(false, pageTitles);

		Path path;

		try {

			path = dc.runSearch(new PageTitle("Walton_Correctional_Institution"), new PageTitle("Britton Hill"));

		} catch (final ExceptionBadStartPageTitle e) {

			e.printStackTrace();

			System.out.println("Bad Start Page Title");

			return;

		} catch (final ExceptionBadEndPageTitle e) {

			e.printStackTrace();

			System.out.println("Bad End Page Title");

			return;

		} catch (final ExceptionPageLinksNotStored e) {

			e.printStackTrace();

			System.out.println("Database was not complete enough for search");

			return;

		}

		System.out.println(path);

	}

}
