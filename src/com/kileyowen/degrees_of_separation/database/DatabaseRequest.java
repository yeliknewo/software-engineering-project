
package com.kileyowen.degrees_of_separation.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.kileyowen.utils.ExceptionNull;
import com.kileyowen.utils.NullUtils;

public abstract class DatabaseRequest<T extends DatabaseResult> {

	private static final String DATABASE_LAUNCH_STRING = "jdbc:sqlite:test.db";

	public abstract T build() throws SQLException, ExceptionNull;

	protected ResultSet buildInternal() throws SQLException, ExceptionNull {

		try (final Connection connection = DriverManager.getConnection(DatabaseRequest.DATABASE_LAUNCH_STRING)) {

			try (final Statement statement = connection.createStatement()) {

				statement.setQueryTimeout(30);

				return NullUtils.assertNotNull(statement.executeQuery(this.getQuery()), "Statement Query Result Set is null");

			}

		}

	}

	abstract String getQuery() throws ExceptionNull;

}
