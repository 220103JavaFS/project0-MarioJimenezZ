package com.revature.services;

import com.revature.dao.AccountDAO;
import com.revature.models.Account;
import com.revature.utils.Encryption;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AccountService {

    public enum saveAccountReply {
        SUCCESS, INVALID_EMAIL, ACCOUNT_EXIST, UNKNOWN_ERROR
    }

    private AccountDAO accountDAO = new AccountDAO();

    public Account getAccountById(int id) {
        return accountDAO.getAccountById(id);
    }

    public Account getAccountByEmail(String email) {
        return accountDAO.getAccountByEmail(email);
    }

    public ArrayList<Account> getAllAccounts() { return accountDAO.getAllAccounts(); }

    /**
     * Checks if new account can be registered.
     * @param a The account we're trying to register. cannot be null
     * @return returns a saveAccountReply with information regarding success:
     *  SUCCESS       - if account was registered successfully
     *  INVALID_EMAIL - if email is not a valid format
     *  ACCOUNT_EXIST - if email is valid format but an account already exists with this email
     *  UNKNOWN_ERROR - if there was any other error registering the account
     */

    public saveAccountReply saveAccount(@NotNull Account a) {
        // Checks if email is valid format
        if (!validateEmail(a.getEmail())) {
            return saveAccountReply.INVALID_EMAIL;
        }
        // Checks if account with email exists
        if (getAccountByEmail(a.getEmail()) != null){
            return saveAccountReply.ACCOUNT_EXIST;
        }
        // Encrypts password to MD5
        a.setPassword(Encryption.stringToMD5(a.getPassword()));
        // Saves Account to Database
        if (accountDAO.saveObject(a)) {
            return saveAccountReply.SUCCESS;
        }
        return saveAccountReply.UNKNOWN_ERROR;
    }

    /**
     * Checks if a string is a valid email by using regex.
     * @param e The email string we will be checking
     * @return returns true if string is a valid email format; false if it's not
     */

    private boolean validateEmail(@NotNull String e) {
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return  e.matches(regex);
    }

}
