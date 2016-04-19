/**
 * 数据库链接工厂类
 */
package com.cwang.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {
	private static Connection conn = null;
	private static String DBDRIVER = "com.mysql.jdbc.Driver";
	private static String DBURL = "jdbc:mysql://localhost:3306/test"; 
	private static String DBUSER = "root";
	private static String DBPASS = "root";  
	
	/**
	 * private constructor
	 */
	private ConnectionFactory(){
		
	}
	
	/**
	 * 获取数据库连接
	 * @return
	 */
	public static Connection getConnection(){
		try{
			if(conn == null){
				Class.forName(DBDRIVER);
				conn = DriverManager.getConnection(DBURL,DBUSER,DBPASS); 
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return conn;
	}
	
	/**
	 * 释放资源
	 * @param rs		结果集
	 * @param st		Statement
	 * @param conn		连接
	 */
	public static void free(ResultSet rs , Statement st, Connection conn){
		try {
            if (rs != null) {
                rs.close(); // 关闭结果集
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) {
                    st.close(); // 关闭Statement
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.close(); // 关闭连接
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	
	/**
	 * getters and setters
	 */

	public static String getDBDRIVER() {
		return DBDRIVER;
	}

	public static void setDBDRIVER(String dBDRIVER) {
		DBDRIVER = dBDRIVER;
	}

	public static String getDBURL() {
		return DBURL;
	}

	public static void setDBURL(String dBURL) {
		DBURL = dBURL;
	}

	public static String getDBUSER() {
		return DBUSER;
	}

	public static void setDBUSER(String dBUSER) {
		DBUSER = dBUSER;
	}

	public static String getDBPASS() {
		return DBPASS;
	}

	public static void setDBPASS(String dBPASS) {
		DBPASS = dBPASS;
	}
	
	
}
