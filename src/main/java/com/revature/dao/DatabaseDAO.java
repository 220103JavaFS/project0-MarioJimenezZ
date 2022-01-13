package com.revature.dao;

import java.sql.*;

public class DatabaseDAO {

    private static DatabaseDAO instance;
    private Connection conn;

    private final String url = "jdbc:postgresql://javafs.cigojrhsanom.us-west-1.rds.amazonaws.com:5432/project0";
    private final String user = "postgres";
    private final String password = "password";

    public static DatabaseDAO getInstance() {
        if (instance == null){
            instance = new DatabaseDAO();
        }
        return instance;
    }

    public ResultSet executeQuery(String q) {
        ResultSet result = null;

        createConnection();
        try {

            Statement statement = conn.createStatement();
            result = statement.executeQuery(q);

        } catch (SQLException e) {
            System.out.println("Error executing Query: " + q);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        closeConnection();

        return result;
    }

    public boolean executeUpdate(String q) {
        try {
            createConnection();

            Statement statement = conn.createStatement();
            statement.executeUpdate(q);

            closeConnection();

        } catch (SQLException e) {
            System.out.println("Error executing Query: " + q);
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }

        return true;

    }

    private void createConnection() {

        try {

            conn = DriverManager.getConnection(url, user, password);
            createSchema();

        } catch (SQLException e) {
            System.out.println("Error Creating connection");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }

    private void closeConnection() {

        try {

            if (conn != null && !conn.isClosed()){
                conn.close();
            }

        }catch (SQLException e) {
            System.out.println("Error closing Database Connection");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void close(ResultSet set) {
        try {
            set.close();
        }catch (SQLException e) {
            System.out.println("Error closing ResultSet");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void createSchema() throws SQLException {

        String customerTable = "CREATE TABLE IF NOT EXISTS accounts(" +
                "id SERIAL PRIMARY KEY," +
                "username varchar(20) NOT NULL," +
                "password varchar(60) NOT NULL" +
                ");";
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(customerTable);
        stmt.close();

    }

    public DatabaseDAO(){}

}
