package com.revature.controllers;

import com.revature.models.Account;
import com.revature.services.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginContext;

public class LoginController implements Controller {

    private AccountService accountService = new AccountService();
    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final Handler validateAccount = ctx -> {
        // Checks if session is already created
        if (ctx.req.getSession(false) != null) {

            Account a = (Account) ctx.req.getSession().getAttribute("user");

            if (a != null){
                ctx.html("<h1> ALREADY_LOGGED_IN </h1>");
                ctx.status(200);
                log.info("User logged in with session already set");
            }
            // No Session exists
        } else {
            Account a = ctx.bodyAsClass(Account.class);

            switch (accountService.validateAccount(a)){
                case SUCCESS:
                    Account customer = accountService.getAccountByEmail(a.getEmail());
                    ctx.req.getSession().setAttribute("user", customer);
                    ctx.json(customer);
                    ctx.status(200);
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
                    ctx.status(400);
                    break;
                case INVALID_PASSWORD:
                    ctx.html("<h1> Error Invalid Password: password doesn't match database password </h1>");
                    log.warn("Customer tried logging in with invalid password for email:" + a.getEmail());
                    ctx.status(400);
                    break;
            }

        }
    };

    private final Handler invalidateSession = ctx -> {
        // Checks if session is already created
        if (ctx.req.getSession(false) != null) {
            ctx.req.getSession().invalidate();
            ctx.status(200);
            ctx.html("<h1> USER_LOGGED_OUT </h1>");
            log.info("User Logged out");
        } else {
            ctx.html("<h1> USER_NOT_LOGGED_IN </h1>");
            log.info("User tried logging out without being logged in");
        }
    };

    @Override
    public void addRoutes(Javalin app) {
        app.post("/login", validateAccount);
        app.post("/logout", invalidateSession);
    }
}
