package org.star.apigateway.core.routes.auth;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class RoutesManager {
    private final HashMap<String, Routes> microservices;

    public RoutesManager(final RoutesReader reader) {
        microservices =  reader.read("routes.yml");
    }

    public AccessType getAccess(final String microservice, final String uri) {
        return microservices.get(microservice).getAccess(uri);
    }

    public Boolean containsMicroservice(final String microservice) {
        return microservices.containsKey(microservice);
    }
}
