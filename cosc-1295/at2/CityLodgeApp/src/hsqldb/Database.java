package hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	// Declaring the Database utility strings for City Lodge
	public static final String JDBC_DRIVER = "org.hsqldb.jdbc.JDBCDriver";
	public static final String DB_PATHWAY = "jdbc:hsqldb:file:database/CityLodge";
	public static final String DB_USER = "SA";
	public static final String DB_PW = "";
	public static Connection con;

	// Connecting to the Database
	public static Connection connect() {
		
		Connection con = null;
		
		try {
			// Opening the HSQLDB JDBC driver and connecting to the DB
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_PATHWAY, DB_USER, DB_PW);

			if (con!= null){
				System.out.println("Connection created successfully");
			} 

		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());

		} catch (SQLException e) {
			System.out.println(e.getMessage());

		}
		return con;

	}

	// Execute Select Queries to the Database
	public static ResultSet selectQuery(String query) {

		ResultSet rs = null;
		Statement stmt = null;

		try(Connection con = DriverManager.getConnection(DB_PATHWAY, DB_USER,DB_PW)) {

			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

		} catch (Exception e) {
			System.out.println(e.getMessage());

		}

		return rs;

	}

	// Execute an Addition, Edit, or Delete query to the Database
	public static void updateQuery(String query) {

		// Testing connection to the DB and creating a connection
		try(Connection con = DriverManager.getConnection(DB_PATHWAY, DB_USER,DB_PW)) {

			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);

		} catch (SQLException e) {
			System.out.println(e.getMessage());

		}
	}
}


