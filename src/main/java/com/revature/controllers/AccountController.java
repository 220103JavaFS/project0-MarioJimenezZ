package com.revature.controllers;

import com.revature.dao.AccountDAO;
import com.revature.models.Account;
import com.revature.services.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class AccountController implements Controller{

    private Logger log = LoggerFactory.getLogger(AccountDAO.class);

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
                log.warn("Customer successfully registered with email: " + customer.getEmail());
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

    @Override
    public void addRoutes(Javalin app) {

        app.get("/accounts", getAllAccounts);
        app.get("/account/get/{id}", getAccount);

        app.post("/account/add", addAccount);
    }
}
