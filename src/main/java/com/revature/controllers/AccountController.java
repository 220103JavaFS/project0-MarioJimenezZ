package com.revature.controllers;

import com.revature.models.Account;
import com.revature.services.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class AccountController implements Controller{

    private Logger log = LoggerFactory.getLogger(AccountController.class);

    private AccountService accountService = new AccountService();

    public AccountController() {}

    private final Handler getAllAccounts = ctx -> {
        ArrayList<Account> list = accountService.getAllAccounts();

        ctx.json(list);
        ctx.status(200);
    };

    private final Handler addAccount = ctx -> {
        Account a = ctx.bodyAsClass(Account.class);
        switch (accountService.saveAccount(a)){
            case SUCCESS:
                Account customer = accountService.getAccountByEmail(a.getEmail());
                ctx.json(customer);
                ctx.status(201);
                log.info("Customer successfully registered with email: " + customer.getEmail());
                break;
            case INVALID_EMAIL:
                ctx.html("<h1> Error Creating Account: Invalid Email Format </h1>");
                ctx.status(400);
                log.warn("Customer tried registering with invalid email format: " + a.getEmail());
                break;
            case ACCOUNT_EXIST:
                ctx.html("<h1> Error Creating Account: Account Exist With Email Address </h1>");
                ctx.status(400);
                log.warn("Customer tried registering multiple accounts with email: " + a.getEmail());
                break;
            default:
            case UNKNOWN_ERROR:
                ctx.html("<h1> Error Creating Account: Unknown Error </h1>");
                log.error("Unknown Error Registering Customer Account => " + a);
                ctx.status(400);
                break;
        }
    };

    private final Handler getAccount = ctx -> {
        String pathId = ctx.pathParam("id");
        int id = Integer.parseInt(pathId);

        Account a = accountService.getAccountById(id);

        ctx.json(a);
        ctx.status(200);
    };

    private final Handler validateAccount = ctx -> {
        Account a = ctx.bodyAsClass(Account.class);

        switch (accountService.validateAccount(a)){
            case SUCCESS:
                Account customer = accountService.getAccountByEmail(a.getEmail());
                ctx.json(customer);
                ctx.status(201);
                log.info("Customer successfully logged in via email: " + customer.getEmail());
                break;
            case INVALID_EMAIL:
                ctx.html("<h1> Error Logging In: Invalid Email Format </h1>");
                log.warn("Customer tried logging in with invalid email: " + a.getEmail());
                ctx.status(400);
                break;
            case USER_NOT_FOUND:
                ctx.html("<h1> Error User Not Found: There's no customer registered to this email </h1>");
                log.warn("Customer tried logging in unregistered email: " + a.getEmail());
                break;
            case INVALID_PASSWORD:
                ctx.html("<h1> Error Invalid Password: password doesn't match database password </h1>");
                log.warn("Customer tried logging in with invalid password for email:" + a.getEmail());
                break;
        }
    };

    @Override
    public void addRoutes(Javalin app) {

        app.get("/accounts", getAllAccounts);
        app.get("/account/get/{id}", getAccount);

        app.post("/register", addAccount);
        app.post("/login", validateAccount);
    }
}
