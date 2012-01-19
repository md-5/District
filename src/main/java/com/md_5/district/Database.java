package com.md_5.district;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public final class Database {

    public static void init() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        write(
                "CREATE TABLE IF NOT EXISTS `" + Config.prefix + "regions` ("
                + "`name` TEXT NOT NULL ,"
                + "`world` TEXT NOT NULL ,"
                + "`start_x` INT NOT NULL ,"
                + "`start_y` INT NOT NULL ,"
                + "`start_z` INT NOT NULL ,"
                + "`end_x` INT NOT NULL ,"
                + "`end_y` INT NOT NULL ,"
                + "`end_z` INT NOT NULL ,"
                + "`owner` TEXT NOT NULL"
                + ") ENGINE = MYISAM ;");
        write(
                "CREATE TABLE IF NOT EXISTS `" + Config.prefix + "friends` ("
                + "`regionName` TEXT NOT NULL ,"
                + "`playerName` TEXT NOT NULL"
                + ") ENGINE = MYISAM ;");
    }

    public static boolean write(final String sql) {
        try {
            Connection conn = DriverManager.getConnection(Config.connectionString);
            conn.createStatement().execute(sql);
            conn.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Query the database for an int, only returns first row / first field
     * @param sql
     * @return first int mathing the specifie sql query
     */
    public static Integer getInt(final String sql) {
        ResultSet rs = null;
        Integer result = 0;
        try {
            Connection conn = DriverManager.getConnection(Config.connectionString);
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt = conn.prepareStatement(sql);
            if (stmt.executeQuery() != null) {
                stmt.executeQuery();
                rs = stmt.getResultSet();
                if (rs.next()) {
                    result = rs.getInt(1);
                } else {
                    result = 0;
                }
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return result;
    }

    public static HashMap<Integer, ArrayList<String>> Read(final String sql) {
        ResultSet rs = null;
        HashMap<Integer, ArrayList<String>> rows = new HashMap<Integer, ArrayList<String>>();
        try {
            Connection conn = DriverManager.getConnection(Config.connectionString);
            PreparedStatement stmt = conn.prepareStatement(sql);
            if (stmt.executeQuery() != null) {
                stmt.executeQuery();
                rs = stmt.getResultSet();
                while (rs.next()) {
                    ArrayList<String> Col = new ArrayList<String>();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        Col.add(rs.getString(i));
                    }
                    rows.put(rs.getRow(), Col);
                }
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rows;
    }
}
