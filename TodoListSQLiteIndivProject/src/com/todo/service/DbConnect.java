package com.todo.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnect {
	private static Connection conn = null;
	
	public static void closeConnection() {
		if(conn != null) {
			try {
				conn.close();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Connection getConnection() {
		if(conn == null) {
			try {
				//sqlite JDBC 체크 
				Class.forName("org.sqlite.JDBC");
				//sqlite 데이터베이스 파일에 연결
				conn = DriverManager.getConnection("jdbc:sqlite:" + "todolist.db");
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
}
