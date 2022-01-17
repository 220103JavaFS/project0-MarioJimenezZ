package com.revature.dao;

import com.revature.models.Account;
import com.revature.utils.ConnectionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;

public class AccountDAOImpl implements AccountDAO {

    private final Logger log = LoggerFactory.getLogger(AccountDAOImpl.class);

    @Override
    public Account getByEmail(String email) {
        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM accounts WHERE email = ?;");

            PreparedStatement statement = conn.prepareStatement(sql.toString());

            statement.setString(1, email);

            ResultSet result = statement.executeQuery();

            if(result.next()) {
                Account account = new Account();
                account.setFirstName(result.getString("first_name"));
                account.setLastName(result.getString("last_name"));
                account.setEmail(result.getString("email"));
                account.setPassword(result.getString("password"));
                account.setBalance(result.getDouble("balance"));
                account.setId(result.getInt("id"));
                account.setAccountType(result.getString("type"));

                return account;
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
    public ArrayList<Account> getAll() {
        ArrayList<Account> list = new ArrayList<>();

        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM accounts ORDER BY id ASC");
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            while(result.next()) {
                Account account = new Account();
                account.setFirstName(result.getString("first_name"));
                account.setLastName(result.getString("last_name"));
                account.setEmail(result.getString("email"));
                account.setPassword(result.getString("password"));
                account.setBalance(result.getDouble("balance"));
                account.setAccountType(result.getString("type"));
                account.setId(result.getInt("id"));

                list.add(account);
            }


        } catch (SQLException e){
            log.warn("Error while creating account list");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Account get(int id) {
        try(Connection conn = ConnectionUtil.getConnection()) {
            StringBuffer sql = new StringBuffer("SELECT * FROM accounts WHERE id = " + id);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());

            if(result.next()) {
                Account account = new Account();
                account.setFirstName(result.getString("first_name"));
                account.setLastName(result.getString("last_name"));
                account.setEmail(result.getString("email"));
                account.setPassword(result.getString("password"));
                account.setBalance(result.getDouble("balance"));
                account.setId(result.getInt("id"));
                account.setAccountType(result.getString("type"));

                return account;
            }


        } catch (SQLException e){
            log.warn("Error while getting account by ID: " + id);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean save(Account a) {
        try(Connection conn = ConnectionUtil.getConnection()){
            StringBuffer sql = new StringBuffer("INSERT INTO  accounts (first_name, last_name, email, password," +
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
    public boolean update(Account a){
        try(Connection conn = ConnectionUtil.getConnection()){
            StringBuffer sql = new StringBuffer("UPDATE ACCOUNTS SET" +
                    " first_name = ?, last_name = ?, email = ?, password = ?," +
                    " balance = ?, type = ? WHERE id = ?;");

            PreparedStatement statement = conn.prepareStatement(sql.toString());

            int count = 0;
            statement.setString(++count, a.getFirstName());
            statement.setString(++count, a.getLastName());
            statement.setString(++count, a.getEmail());
            statement.setString(++count, a.getPassword());
            statement.setDouble(++count, a.getBalance());
            statement.setString(++count, a.getAccountType().name());
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

            StringBuffer sql = new StringBuffer("DELETE FROM accounts WHERE id = " + id + ";");
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
