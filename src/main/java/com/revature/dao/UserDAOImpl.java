package com.revature.dao;

import com.revature.models.User;
import com.revature.utils.ConnectionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;

public class UserDAOImpl implements UserDAO {

    private final Logger log = LoggerFactory.getLogger(UserDAOImpl.class);

    @Override
    public User getByEmail(String email) {
        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM users WHERE email = ?;");

            PreparedStatement statement = conn.prepareStatement(sql.toString());

            statement.setString(1, email);

            ResultSet result = statement.executeQuery();

            if(result.next()) {
                User user = new User();
                user.setFirstName(result.getString("first_name"));
                user.setLastName(result.getString("last_name"));
                user.setEmail(result.getString("email"));
                user.setPassword(result.getString("password"));
                user.setBalance(result.getDouble("balance"));
                user.setId(result.getInt("id"));
                user.setAccountType(result.getString("type"));
                user.setNumOfOrders(result.getInt("orders"));

                return user;
            }
            return null;

        } catch (SQLException e){
            log.warn("Error while getting account by email: " + email);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ArrayList<User> getAll() {
        ArrayList<User> list = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM users ORDER BY id ASC");
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            while(result.next()) {
                User user = new User();
                user.setFirstName(result.getString("first_name"));
                user.setLastName(result.getString("last_name"));
                user.setEmail(result.getString("email"));
                user.setPassword(result.getString("password"));
                user.setBalance(result.getDouble("balance"));
                user.setAccountType(result.getString("type"));
                user.setId(result.getInt("id"));
                user.setNumOfOrders(result.getInt("orders"));

                list.add(user);
            }


        } catch (SQLException e){
            log.warn("Error while creating account list");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public User get(int id) {
        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM users WHERE id = " + id);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            if(result.next()) {
                User user = new User();
                user.setFirstName(result.getString("first_name"));
                user.setLastName(result.getString("last_name"));
                user.setEmail(result.getString("email"));
                user.setPassword(result.getString("password"));
                user.setBalance(result.getDouble("balance"));
                user.setId(result.getInt("id"));
                user.setAccountType(result.getString("type"));
                user.setNumOfOrders(result.getInt("orders"));

                return user;
            }


        } catch (SQLException e){
            log.warn("Error while getting account by ID: " + id);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean save(User a) {
        try(Connection conn = ConnectionUtil.getConnection()){
            StringBuffer sql = new StringBuffer("INSERT INTO  users (first_name, last_name, email, password," +
                    " balance, type) VALUES (?,?,?,?,?,?);");

            PreparedStatement statement = conn.prepareStatement(sql.toString());

            int count = 0;
            statement.setString(++count, a.getFirstName());
            statement.setString(++count, a.getLastName());
            statement.setString(++count, a.getEmail());
            statement.setString(++count, a.getPassword());
            statement.setDouble(++count, a.getBalance());
            statement.setString(++count, "CUSTOMER");

            statement.execute();

            return true;

        }catch (SQLException e){
            log.warn("Error while saving account : " + a);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(User a){
        try(Connection conn = ConnectionUtil.getConnection()){
            StringBuffer sql = new StringBuffer("UPDATE users SET" +
                    " first_name = ?, last_name = ?, email = ?, password = ?," +
                    " balance = ?, type = ?, orders = ? WHERE id = ?;");

            PreparedStatement statement = conn.prepareStatement(sql.toString());

            int count = 0;
            statement.setString(++count, a.getFirstName());
            statement.setString(++count, a.getLastName());
            statement.setString(++count, a.getEmail());
            statement.setString(++count, a.getPassword());
            statement.setDouble(++count, a.getBalance());
            statement.setString(++count, a.getAccountType().name());
            statement.setInt(++count, a.getNumOfOrders());
            statement.setInt(++count, a.getId());

            statement.execute();

            return true;

        }catch (SQLException e){
            log.warn("Error while updating account : " + a);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try(Connection conn = ConnectionUtil.getConnection()) {

            StringBuffer sql = new StringBuffer("DELETE FROM users WHERE id = " + id + ";");
            Statement statement = conn.createStatement();

            statement.executeUpdate(sql.toString());

            return true;

        } catch (SQLException e) {
            log.warn("Error while deleting account id : " + id);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
