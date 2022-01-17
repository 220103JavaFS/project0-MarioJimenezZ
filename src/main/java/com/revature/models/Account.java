package com.revature.models;

import java.util.Objects;

public class Account {

    public enum AccountType {
        CUSTOMER, ADMINISTRATOR, SELLER, PREMIUM, INVALID
    }

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private AccountType accountType;
    private double balance;
    private int id;

    public Account() { }

    public Account(String firstName, String lastName, String email, String password) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public void setAccountType(String accountType) { this.accountType = stringToAccountType(accountType); }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    @Override
    public String toString() {
        return "Account{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", id=" + id +
                '}';
    }
}