package org.star.apigateway.core.routes.auth;

import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.star.apigateway.web.exception.core.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Routes {
    private final HashMap<String, AccessType> routesAccess = new HashMap<>();

//    public Routes() {
//        routesAccess.put("/whoami", AccessType.AUTHORITY);
//        routesAccess.put("/signin", AccessType.PUBLIC);
//        routesAccess.put("/signup", AccessType.PUBLIC);
//    }


    public Routes(Map<String, List<String>> routes) {
        System.out.println("check4");
        routes.forEach((key, value) -> {
            value.forEach((uri) -> {
                routesAccess.put(uri, AccessType.valueOf(key.toUpperCase()));
            });
        });
        System.out.println(routesAccess.get("/whoami"));
    }

    public AccessType getAccess(@NotNull final String uri) {
        if (!routesAccess.containsKey(uri)) {
            throw new NotFoundException("Page not found");
        }
        return routesAccess.get(uri);
    }
}
