package com.revature.dao;

import com.revature.models.Account;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class AccountDAO extends DAO<Account> {

    private final Logger log = LoggerFactory.getLogger(AccountDAO.class);

    public AccountDAO() {
        update();
    }

    public Account getAccountById(int id) {
        for (Account a : objects) {
            if (a.getId() == id) {
                return a;
            }
        }
        return null;
    }

    public Account getAccountByEmail(String email){
        for (Account a : objects) {
            if (a.getEmail().equals(email)) {
                return a;
            }
        }
        return null;
    }

    public ArrayList<Account> getAllAccounts() { return objects; }

    @Override
    public boolean update() {
        objects.clear();

        ResultSet accounts_query = database.executeQuery("SELECT * FROM accounts");

        try {
            while (accounts_query.next()) {
                objects.add(new Account(
                        accounts_query.getString("email"),
                        accounts_query.getString("password"),
                        accounts_query.getString("type"),
                        accounts_query.getDouble("balance"),
                        accounts_query.getInt("id")
                ));
            }
            accounts_query.close();
        }  catch(SQLException e){
            log.warn("Error while creating account list");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean saveObject(@NotNull Account a) {
        StringBuilder add_query = new StringBuilder();
        add_query.append("INSERT INTO accounts (email, password, balance, type) VALUES(");
        add_query.append("'" + a.getEmail() + "',");
        add_query.append("'" + a.getPassword() + "',");
        add_query.append(0 + ",");
        add_query.append("'CUSTOMER');");

        if (database.executeUpdate(add_query.toString())) {
            return update();
        }

        return false;
    }
}
