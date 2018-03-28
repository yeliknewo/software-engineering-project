
package com.kileyowen.degrees_of_separation.database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.kileyowen.degrees_of_separation.Page;

public class DatabaseResultLinksHere extends DatabaseResult {

	private final List<Page> pages;

	public DatabaseResultLinksHere(final ResultSet resultSet) throws ExceptionPageLinksNotStored {

		// TODO: Parse Result Set

		this.pages = new ArrayList<>();

	}

	public List<Page> getPages() {

		return this.pages;

	}

}
