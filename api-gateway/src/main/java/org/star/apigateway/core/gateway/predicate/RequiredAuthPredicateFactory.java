//package org.star.apigateway.core.gateway.predicate;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
//import org.springframework.web.server.ServerWebExchange;
//
//import java.net.http.HttpHeaders;
//import java.util.Optional;
//import java.util.function.Consumer;
//import java.util.function.Predicate;
//
//public class RequiredAuthPredicateFactory extends AbstractRoutePredicateFactory<RequiredAuthPredicateFactory.Config> {
//    public RequiredAuthPredicateFactory(Class<Config> configClass) {
//        super(configClass);
//    }
//
//    @Override
//    public Predicate<ServerWebExchange> apply(Config config) {
//        return (exchange) -> Optional
//                .ofNullable(exchange.getRequest().getHeaders().get("Authorization")).isPresent();
//    }
//
//    @Getter
//    @Setter
//    public static class Config {
//        private Boolean required = true;
//    }
//}
