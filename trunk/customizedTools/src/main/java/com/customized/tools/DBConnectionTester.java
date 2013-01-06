package com.customized.tools;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.customized.tools.dbconntest.DBConnectionTesterException;
import com.customized.tools.startup.ToolsConsole;
import com.customized.tools.startup.ToolsProperties;

public class DBConnectionTester extends AbstractTools {
	
	private static final Logger logger = Logger.getLogger(DBConnectionTester.class);

	private static final String TABLE_CREATE = "create table tool_dbconn_kylin_test(id int)";
	private static final String TABLE_INSERT = "insert into tool_dbconn_kylin_test values(100)";
	private static final String TABLE_DROP = "drop table tool_dbconn_kylin_test";
	
	public DBConnectionTester(ToolsProperties props, ToolsConsole console) {
		super(props, console);
	}

	public void execute() throws Throwable {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			promptConnectionResult(conn);
			
			stmt = conn.createStatement();
			stmt.executeUpdate(TABLE_CREATE);
			console.prompt("\nCreate Table Success");
			
			stmt.execute(TABLE_INSERT);
			console.prompt("\nInsert Success");
			
			stmt.execute(TABLE_DROP);
			console.prompt("\nDrop Table Success");
			
		} catch (Exception e) {
			
			console.prompt("\n Test Failed, due to " + e.getMessage()  + "\n");
			
			e.printStackTrace();
			
		}finally {
			if(rs != null) {
				rs.close();
			}
			if(stmt != null) {
				stmt.close();
			}
			if(conn != null) {
				conn.close();
			}
		}
	}
	


	private void promptConnectionResult(Connection conn) throws SQLException {

		DatabaseMetaData meta = conn.getMetaData();
		
		StringBuffer sb = new StringBuffer();
		sb.append("Create Databse Connection [" + props.getProperty("tester.db.url") + " - " + props.getProperty("tester.db.user") + "/******]\n\n");
		sb.append("Databse Connection Test Success\n\n");
		sb.append(meta.getDatabaseProductVersion());
		
		console.prompt(sb.toString());
		
		logger.info(sb.toString());
		
	}

	private Connection getConnection() {

		try {
			Class c = Class.forName(props.getProperty("tester.db.driverClass",true));
			Driver d = (Driver) c.newInstance();
			DriverManager.registerDriver(d);
			Connection conn = DriverManager.getConnection(props.getProperty("tester.db.url",true), props.getProperty("tester.db.user",true), props.getProperty("tester.db.password",true));
			return conn;
		} catch (Exception e) {
			throw new DBConnectionTesterException("Create Connection Error ", e);
		}
	}
	
}
