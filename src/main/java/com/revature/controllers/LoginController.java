package com.revature.controllers;

import com.revature.models.User;
import com.revature.models.UserDTO;
import com.revature.services.ResponseType;
import com.revature.services.UserService;
import com.revature.utils.SessionUtil;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController implements Controller {

    private UserService userService = new UserService();
    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final Handler validateAccount = ctx -> {
        // Checks if session is already created
        User u = SessionUtil.UserValidate(ctx, User.AccountType.CUSTOMER);
        if (u != null) {
            ctx.html("<h1> ALREADY_LOGGED_IN </h1>");
            ctx.status(200);
            log.info("User tried logging in with session already set" + u);
            // No Session exists
        } else {

            UserDTO dto = ctx.bodyAsClass(UserDTO.class);
            User a = new User(dto.email, dto.password);

            ResponseType result = userService.validateAccount(a);

            if (result == ResponseType.SUCCESS){
                // Grabs user info from database to set session
                User user = userService.getAccountByEmail(a.getEmail());
                // Sets user session to user Object
                ctx.req.getSession().setAttribute("user", user);
                // Shows user in json format
                ctx.json(user);
                // Sends ok Status
                ctx.status(200);
                // Logs information to our log
                log.info("Customer successfully logged in via email: " + user.getEmail());
            } else {
                ctx.html("<h1>ERROR_LOGGING_IN : " + result.name() + "</h1>");
                log.warn("ERROR_LOGGING_IN : \n" + a + " \n " + result.name());
                ctx.status(400);
            }
        }
    };

    private final Handler invalidateSession = ctx -> {
        // Checks if session is already created
        User u = SessionUtil.UserValidate(ctx, User.AccountType.CUSTOMER);
        if (u != null) {
            ctx.req.getSession().invalidate();
            ctx.status(200);
            ctx.html("<h1> USER_LOGGED_OUT </h1>");
            log.info("User Logged out : " + u);
        } else {
            ctx.html("<h1> USER_NOT_LOGGED_IN </h1>");
            ctx.status(400);
            log.info("User tried logging out without being logged in");
        }
    };

    @Override
    public void addRoutes(Javalin app) {
        app.post("/login", validateAccount);
        app.post("/logout", invalidateSession);
    }
}
