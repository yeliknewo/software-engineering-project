
package com.kileyowen.degrees_of_separation.database;

import java.sql.SQLException;

import com.kileyowen.utils.ExceptionNull;
import com.kileyowen.utils.NullUtils;

public class DatabaseRequestPageId extends DatabaseRequest<DatabaseResultPageId> {

	private final String pageTitle;

	public DatabaseRequestPageId(final String newPageTitle) {

		this.pageTitle = newPageTitle;

	}

	@Override
	public DatabaseResultPageId build() throws SQLException, ExceptionNull {

		return new DatabaseResultPageId(this.buildInternal());

	}

	@Override
	String getQuery() throws ExceptionNull {

		return NullUtils.assertNotNull(String.format("select pageid from pages where title=\"%s\"", this.pageTitle), "String.Format was null");

	}

}
