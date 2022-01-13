package com.revature.models;

import java.util.Objects;

public class Account {

    public enum AccountType {
        CUSTOMER, ADMINISTRATOR, SELLER, INVALID, PREMIUM
    }


    private String email;
    private String password;
    private AccountType accountType;
    private double balance;
    private int id;

    public Account() { }

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Account(String email, String password, AccountType accountType, double balance, int id) {
        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.balance = balance;
        this.id = id;
    }

    public Account(String email, String password, String accountType, double balance, int id) {
        this.email = email;
        this.password = password;
        this.accountType = stringToAccountType(accountType);
        this.balance = balance;
        this.id = id;
    }

    public Account(String email, String password, String accountType) {
        this.email = email;
        this.password = password;
        this.accountType = stringToAccountType(accountType);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Double.compare(account.balance, balance) == 0 && id == account.id && Objects.equals(email, account.email) && Objects.equals(password, account.password) && accountType == account.accountType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, accountType, balance, id);
    }

    private AccountType stringToAccountType(String t) {
        switch (t) {
            case "CUSTOMER":
                return AccountType.CUSTOMER;
            case "ADMINISTRATOR":
                return AccountType.ADMINISTRATOR;
            case "PREMIUM":
                return AccountType.PREMIUM;
            case "SELLER":
                return AccountType.SELLER;
            default:
                return AccountType.INVALID;
        }
    }
}