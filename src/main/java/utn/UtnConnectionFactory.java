package utn;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class UtnConnectionFactory
{
	private static Connection conn;

	public static Connection getConnection() throws IOException, SQLException {
		if(conn == null)
		{
			Properties prop = new Properties();
			InputStream is = UtnConnectionFactory.class.getResourceAsStream("/config.properties");
			prop.load(is);
			conn = DriverManager.getConnection(prop.getProperty("connection_string"),prop.getProperty("db_user"),prop.getProperty("db_pass"));
		}
		return conn;
	}
}
