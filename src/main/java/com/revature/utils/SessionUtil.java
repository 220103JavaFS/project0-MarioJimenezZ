package com.revature.utils;

import com.revature.models.User;
import com.revature.models.User.AccountType;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionUtil {

    private static final Logger log = LoggerFactory.getLogger(SessionUtil.class);

    @Nullable
    public static User UserValidate(@NotNull Context ctx, AccountType type) {
        boolean authorized = false;
        User u = null;
        if (ctx.req.getSession(false) != null) {

            u = (User) ctx.req.getSession().getAttribute("user");
            // Ordinals are in order of access level
            if (u != null && u.getAccountType().ordinal() >= type.ordinal()) {
                authorized = true;
            }
        }
        if (!authorized){
            ctx.html("<h1>UNAUTHORIZED_ACCESS</h1>");
            ctx.status(401);
            log.warn("USER TRIED VIEWING UNAUTHORIZED PAGE WITH PATH: [" + ctx.path() + "]");
            return null;
        }

        return u;
    }
}
