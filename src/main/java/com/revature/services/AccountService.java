package com.revature.services;

import com.revature.dao.AccountDAO;
import com.revature.dao.AccountDAOImpl;
import com.revature.models.Account;
import com.revature.utils.EncryptionUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AccountService {

    private AccountDAO accountDAO = new AccountDAOImpl();

    public Account getAccountById(int id) {
        return accountDAO.get(id);
    }

    public Account getAccountByEmail(String email) {
        if (!email.isEmpty() && validateEmail(email)) {
            return accountDAO.getByEmail(email);
        }
        return null;
    }

    public ArrayList<Account> getAllAccounts() { return accountDAO.getAll(); }

    /**
     * Checks if new account can be registered.
     * @param a The account we're trying to register. cannot be null
     * @return returns a saveAccountReply with information regarding success:
     *  SUCCESS       - if account was registered successfully
     *  INVALID_EMAIL - if email is not a valid format
     *  ACCOUNT_EXIST - if email is valid format but an account already exists with this email
     *  UNKNOWN_ERROR - if there was any other error registering the account
     */

    public ResponseType saveAccount(@NotNull Account a) {
        // Checks if first and last names are set
        if (a.getFirstName().isEmpty() || a.getLastName().isEmpty()){
            return ResponseType.INVALID_FIELDS;
        }

        // Checks if email is valid format
        if (!validateEmail(a.getEmail())) {
            return ResponseType.INVALID_EMAIL;
        }
        // Checks if account with email exists
        if (getAccountByEmail(a.getEmail()) != null){
            return ResponseType.ACCOUNT_EXIST;
        }
        // Encrypts password to MD5
        a.setPassword(EncryptionUtil.stringToMD5(a.getPassword()));
        // Saves Account to Database
        if (accountDAO.save(a)) {
            return ResponseType.SUCCESS;
        }
        return ResponseType.UNKNOWN_ERROR;
    }

    /**
     * Checks if account matches information in database.
     * @param a The account we're trying to validate. cannot be null
     * @return returns a saveAccountReply with information regarding success:
     *  SUCCESS          - if account email and password match database hash
     *  INVALID_EMAIL    - if email is not a valid format
     *  USER_NOT_FOUND   - if email is valid format but an account does not exist with email
     *  INVALID_PASSWORD - if password does not match encrypted hash in database
     */

    public ResponseType validateAccount(@NotNull Account a) {
        // Checks if email is valid format
        if (!validateEmail(a.getEmail())) {
            return ResponseType.INVALID_EMAIL;
        }
        // Checks if user exists
        Account user = getAccountByEmail(a.getEmail());
        if (user == null) {
            return ResponseType.USER_NOT_FOUND;
        }
        // Encrypts user password
        a.setPassword(EncryptionUtil.stringToMD5(a.getPassword()));
        // makes sure password hashes match
        if (!user.getPassword().equals(a.getPassword())){
            return ResponseType.INVALID_PASSWORD;
        }
        // Finally, returns success
        return ResponseType.SUCCESS;
    }

    public ResponseType deleteAccount(@NotNull Account a) {

        if (accountDAO.delete(a.getId())) {
            return ResponseType.SUCCESS;
        }
        return ResponseType.UNKNOWN_ERROR;
    }

    public ResponseType deleteAccount(int id) {

        Account a = getAccountById(id);

        if (a == null) {
            return ResponseType.INVALID_USER;
        }

        return deleteAccount(a);
    }

    public ResponseType updateAccount(@NotNull Account a) {
        // Checks if first and last names are set
        if (a.getFirstName().isEmpty() || a.getLastName().isEmpty()){
            return ResponseType.INVALID_FIELDS;
        }
        // Checks if email is valid format
        if (!validateEmail(a.getEmail())) {
            return ResponseType.INVALID_EMAIL;
        }
        // Encrypts password to MD5
        a.setPassword(EncryptionUtil.stringToMD5(a.getPassword()));
        // Updates object to database
        if (accountDAO.update(a)) {
            return ResponseType.SUCCESS;
        }
        return ResponseType.UNKNOWN_ERROR;
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
