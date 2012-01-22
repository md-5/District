package com.md_5.district;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public final class Database {

    public static void init() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        writeRaw("CREATE TABLE IF NOT EXISTS `ds_regions` ("
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
        writeRaw("CREATE TABLE IF NOT EXISTS `ds_friends` ("
                + "`regionName` TEXT NOT NULL ,"
                + "`playerName` TEXT NOT NULL"
                + ") ENGINE = MYISAM ;");
    }

    private static boolean writeRaw(final String sql) {
        try {
            Connection conn = DriverManager.getConnection(Config.connectionString);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute(sql);
            conn.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public static boolean write(final String sql, final ArrayList<String> args) {
        try {
            Connection conn = DriverManager.getConnection(Config.connectionString);
            PreparedStatement ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.size(); i++) {
                try {
                    ps.setInt(i + 1, Integer.parseInt(args.get(i)));
                } catch (final NumberFormatException ex) {
                    ps.setString(i + 1, args.get(i));
                }
            }
            ps.executeUpdate();
            conn.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static HashMap<Integer, ArrayList<String>> read(final String sql, final ArrayList<String> args) {
        ResultSet rs = null;
        HashMap<Integer, ArrayList<String>> rows = new HashMap<Integer, ArrayList<String>>();
        try {
            Connection conn = DriverManager.getConnection(Config.connectionString);
            PreparedStatement ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.size(); i++) {
                try {
                    ps.setInt(i + 1, Integer.parseInt(args.get(i)));
                } catch (final NumberFormatException ex) {
                    ps.setString(i + 1, args.get(i));
                }
            }
            rs = ps.executeQuery();
            if (rs != null) {
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

    @Deprecated
    public static HashMap<Integer, ArrayList<String>> readRaw(final String sql) {
        ResultSet rs = null;
        HashMap<Integer, ArrayList<String>> rows = new HashMap<Integer, ArrayList<String>>();
        try {
            Connection conn = DriverManager.getConnection(Config.connectionString);
            PreparedStatement stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs != null) {
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
