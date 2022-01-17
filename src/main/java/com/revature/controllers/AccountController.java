package com.revature.controllers;

import com.revature.models.Account;
import com.revature.models.Account.AccountType;
import com.revature.models.AccountDTO;
import com.revature.services.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class AccountController implements Controller{

    private final Logger log = LoggerFactory.getLogger(AccountController.class);

    private AccountService accountService = new AccountService();

    public AccountController() {}

    private final Handler getAccount = ctx -> {
        if (ctx.req.getSession(false) != null) {

            Account a = (Account) ctx.req.getSession().getAttribute("user");

            if (a != null) {
                Account user = accountService.getAccountByEmail(a.getEmail());
                if (user != null) {
                    ctx.json(user);
                    ctx.status(200);
                    log.info("User is viewing account " + a);
                }
            }
        } else {
            ctx.html("NOT_LOGGED_IN");
            ctx.status(401);
            log.warn("User tried viewing account without logging in");
        }
    };

    private final Handler getAllAccounts = ctx -> {
        if (ctx.req.getSession(false) != null) {

                Account a = (Account) ctx.req.getSession().getAttribute("user");

            // Only administrators can view all
            if (a != null && a.getAccountType() == AccountType.ADMINISTRATOR) {

                ArrayList<Account> list = accountService.getAllAccounts();

                ctx.json(list);
                ctx.status(200);
            } else {
                ctx.html("NOT_ADMINISTRATOR");
                ctx.status(401);
                log.warn("User tried viewing all accounts without proper permissions");
            }
        } else {
            ctx.html("NOT_LOGGED_IN");
            ctx.status(401);
            log.warn("User tried viewing all accounts without logging in");
        }
    };

    private final Handler addAccount = ctx -> {
        if (ctx.req.getSession(false) != null) {
            ctx.html("ALREADY_LOGGED_IN");
            ctx.status(400);
            log.info("User tried registering a new account while being logged in");
        } else {
            Account a = ctx.bodyAsClass(Account.class);
            switch (accountService.saveAccount(a)) {
                case SUCCESS:
                    Account new_user = accountService.getAccountByEmail(a.getEmail());
                    ctx.json(new_user);
                    ctx.req.getSession().setAttribute("user", new_user);
                    ctx.status(201);
                    log.info("Customer successfully registered with email: " + a.getEmail());
                    break;
                case INVALID_FIELDS:
                    ctx.html("<h1> Error Creating Account: First or Last Name Can't be Empty </h1>");
                    ctx.status(400);
                    log.warn("user tried registering with invalid empty first or last name: " + a);
                    break;
                case INVALID_EMAIL:
                    ctx.html("<h1> Error Creating Account: Invalid Email Format </h1>");
                    ctx.status(400);
                    log.warn("user tried registering with invalid email format: " + a.getEmail());
                    break;
                case ACCOUNT_EXIST:
                    ctx.html("<h1> Error Creating Account: Account Exist With Email Address </h1>");
                    ctx.status(400);
                    log.warn("user tried registering multiple accounts with email: " + a.getEmail());
                    break;
                default:
                case UNKNOWN_ERROR:
                    ctx.html("<h1> Error Creating Account: Unknown Error </h1>");
                    log.error("Unknown Error Registering Customer Account => " + a);
                    ctx.status(400);
                    break;
            }
        }
    };

    private final Handler updateAccount = ctx -> {
        // Checks if session is set
        if (ctx.req.getSession(false) != null) {
            // Grabs account from session
            Account a = (Account) ctx.req.getSession().getAttribute("user");
            // Data transfer object
            AccountDTO info = ctx.bodyAsClass(AccountDTO.class);

            // Updates account info with the information from the data transfer
            a.setFirstName(info.firstName);
            a.setLastName(info.lastName);
            a.setEmail(info.email);
            a.setPassword(info.password);

            // Tries to update the account
            switch (accountService.updateAccount(a)) {
                case SUCCESS:
                    Account customer = accountService.getAccountByEmail(a.getEmail());
                    ctx.json(customer);
                    ctx.status(200);
                    log.info("Customer successfully updated account with email: " + customer.getEmail());
                    break;
                case INVALID_FIELDS:
                    ctx.html("<h1>INVALID_FIELDS</h1>");
                    ctx.status(400);
                    log.warn("User tried updating with invalid empty first or last name: " + a);
                    break;
                case INVALID_EMAIL:
                    ctx.html("<h1> INVALID_EMAIL </h1>");
                    ctx.status(400);
                    log.warn("User tried updating with invalid email format: " + a.getEmail());
                    break;
                default:
                case UNKNOWN_ERROR:
                    ctx.html("<h1> UNKNOWN_ERROR </h1>");
                    log.error("Unknown Error Updating Account : " + a);
                    ctx.status(400);
                    break;
            }
        } else {
            ctx.html("NOT_LOGGED_IN");
            ctx.status(401);
            log.warn("User tried updating account without logging in");
        }
    };

    private final Handler getAccountById = ctx -> {
        if (ctx.req.getSession(false) != null) {

            Account a = (Account) ctx.req.getSession().getAttribute("user");

            if (a != null && a.getAccountType() == AccountType.ADMINISTRATOR) {
                String pathId = ctx.pathParam("id");
                int id = Integer.parseInt(pathId);

                Account user = accountService.getAccountById(id);

                if (user != null) {
                    ctx.json(user);
                    ctx.status(200);
                } else {
                    ctx.html("<h1> Cannot find the user with ID : " + id + "</h1>");
                    log.warn("User tried retrieving non-existing account with id : " + id);
                    ctx.status(400);
                }
            } else {
                ctx.html("NOT_ADMINISTRATOR");
                ctx.status(401);
                log.warn("User tried to get user by ID without being administrator");
            }
        } else {
            ctx.html("NOT_LOGGED_IN");
            ctx.status(401);
            log.warn("User tried to delete someone without being logged in");
        }
    };

    private final Handler deleteById = ctx -> {

        if (ctx.req.getSession(false) != null) {

            Account a = (Account) ctx.req.getSession().getAttribute("user");

            if (a != null && a.getAccountType() == AccountType.ADMINISTRATOR) {

                String pathId = ctx.pathParam("id");
                int id = Integer.parseInt(pathId);

                switch (accountService.deleteAccount(id)) {
                    case SUCCESS:
                        ctx.html("<h1> Deleted User ID : " + id + " Successfully </h1>");
                        log.info("Successfully deleted account by ID : " + id);
                        ctx.status(200);
                        break;
                    case INVALID_USER:
                        ctx.html("<h1>Error Invalid User ID : " + id + " </h1>");
                        log.info("User tried to delete account that does not exist by ID : " + id);
                        ctx.status(400);
                        break;
                    default:
                    case UNKNOWN_ERROR:
                        ctx.html("<h1> Unknown Error, Cannot Delete Account with ID : " + id + "</h1>");
                        ctx.status(400);
                        log.error("Unknown error while trying to delete account by ID: " + id);
                        break;

                }
            } else {
                ctx.html("NOT_ADMINISTRATOR");
                ctx.status(401);
                log.warn("User tried to delete user without being administrator");
            }
        } else {
            ctx.html("NOT_LOGGED_IN");
            ctx.status(401);
            log.warn("User tried to delete someone without being logged in");
        }
    };

    private final Handler deleteAccount = ctx ->  {
        if (ctx.req.getSession(false) != null) {

            Account a = (Account) ctx.req.getSession().getAttribute("user");

            if (a != null) {
                int id = a.getId();
                switch (accountService.deleteAccount(id)) {
                    case SUCCESS:
                        ctx.html("<h1> Deleted User ID : " + id + " Successfully </h1>");
                        log.info("Successfully deleted account by ID : " + id);
                        ctx.req.getSession().invalidate();
                        ctx.status(200);
                        break;
                    case INVALID_USER:
                        ctx.html("<h1>Error Invalid User ID : " + id + " </h1>");
                        log.info("User tried to delete account that does not exist by ID : " + id);
                        ctx.status(400);
                        break;
                    default:
                    case UNKNOWN_ERROR:
                        ctx.html("<h1> Unknown Error, Cannot Delete Account with ID : " + id + "</h1>");
                        ctx.status(400);
                        log.error("Unknown error while trying to delete account by ID: " + id);
                        break;

                }
            }
        } else {
            ctx.html("NOT_LOGGED_IN");
            ctx.status(401);
            log.warn("User tried viewing account without logging in");
        }
    };

    @Override
    public void addRoutes(Javalin app) {
        // ============ USER ROUTES ========== \\
        // Views User Account
        app.get("/account", getAccount);
        // Updates user Account
        app.put("/account", updateAccount);
        // Deletes user Account
        app.get("/account/delete", deleteAccount);
        // Registers user Account
        app.post("/register", addAccount);

        // ============ ADMIN ROUTES ========== \\
        // Views user by ID
        app.get("/account/view/{id}", getAccountById);
        // Deletes user by ID
        app.get("/account/delete/{id}", deleteById);
        // Views all user accounts
        app.get("/accounts", getAllAccounts);
    }
}