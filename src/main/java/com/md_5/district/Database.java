package com.md_5.district;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {

    private District plugin;
    private String connectionString;

    public Database(final District plugin) {
        this.plugin = plugin;
        this.connectionString = Config.connectionString;
        // Load the driver instance
        try {
            Class.forName("com.mysql.jdbc.Driver");
            DriverManager.getConnection(connectionString);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        createStructure();
    }

    //Create the DB structure
    public void createStructure() {
        write("CREATE TABLE IF NOT EXISTS `" + Config.prefix + "regions` ("
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
        write("CREATE TABLE IF NOT EXISTS `" + Config.prefix + "friends` ("
                + "`regionName` TEXT NOT NULL ,"
                + "`playerName` TEXT NOT NULL"
                + ") ENGINE = MYISAM ;");
    }

    // write query
    public boolean write(String sql) {
        try {
            Connection conn = DriverManager.getConnection(connectionString);
            conn.createStatement().execute(sql);
            conn.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Get Int
    // only return first row / first field
    public Integer getInt(String sql) {
        ResultSet rs = null;
        Integer result = 0;
        try {
            Connection conn = DriverManager.getConnection(connectionString);
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

    // read query
    public HashMap<Integer, ArrayList<String>> Read(String sql) {
        ResultSet rs = null;
        HashMap<Integer, ArrayList<String>> Rows = new HashMap<Integer, ArrayList<String>>();
        try {
            Connection conn = DriverManager.getConnection(connectionString);
            PreparedStatement stmt = conn.prepareStatement(sql);
            if (stmt.executeQuery() != null) {
                stmt.executeQuery();
                rs = stmt.getResultSet();
                while (rs.next()) {
                    ArrayList<String> Col = new ArrayList<String>();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        Col.add(rs.getString(i));
                    }
                    Rows.put(rs.getRow(), Col);
                }
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Rows;
    }
}
