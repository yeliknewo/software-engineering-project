
package com.kileyowen.degrees_of_separation.database;

import java.sql.SQLException;

import com.kileyowen.degrees_of_separation.PageId;
import com.kileyowen.utils.ExceptionNull;

public class DatabaseRequestLinksHere extends DatabaseRequest<DatabaseResultLinksHere> {

	private final PageId pageId;

	public DatabaseRequestLinksHere(final PageId newPageId) {

		this.pageId = newPageId;

	}

	@Override
	public DatabaseResultLinksHere build() throws SQLException, ExceptionNull {

		return new DatabaseResultLinksHere(this.buildInternal());

	}

	@Override
	String getQuery() throws ExceptionNull {

		//TODO Actually Do

		return "";
	}

}
