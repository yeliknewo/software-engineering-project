
package com.kileyowen.degrees_of_separation.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.kileyowen.degrees_of_separation.Page;
import com.kileyowen.degrees_of_separation.PageId;
import com.kileyowen.utils.ExceptionNull;
import com.kileyowen.utils.NullUtils;

public class DatabaseResultPageId extends DatabaseResult {

	private static final String PAGE_ID = "pageid";

	private static final String TITLE = "title";

	private final Page page;

	public DatabaseResultPageId(final ResultSet resultSet) throws SQLException, ExceptionNull {

		this.page = new Page(new PageId(resultSet.getInt(DatabaseResultPageId.PAGE_ID)), NullUtils.assertNotNull(resultSet.getString(DatabaseResultPageId.TITLE), "Title was null"));

	}

	public Page getPage() {

		return this.page;
	}

}
