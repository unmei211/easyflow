//package org.star.apigateway.core.security.jwt;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.http.HttpHeaders;
//import org.springframework.lang.NonNull;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.star.apigateway.core.security.resolver.AuthRoleRequired;
//import org.star.apigateway.core.security.user.UserCredentials;
//import org.star.apigateway.core.service.security.JwtService;
//
//import java.lang.reflect.Method;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * Spring interceptor for JWT based authentication and authorization
// */
//@Slf4j
//@AllArgsConstructor
//@Qualifier("ServletInterceptor")
//public class ServletJwtInterceptor implements HandlerInterceptor {
//    /**
//     * user credentials name
//     */
//    public static final String USER_CREDENTIALS = "userCredentialsAttr";
//
//    private final JwtService jwtService;
//
//    @Override
//    public boolean preHandle(
//            @NonNull final HttpServletRequest request,
//            @NonNull final HttpServletResponse response,
//            @NonNull final Object handler
//    ) {
//        if (handler.getClass().equals(HandlerMethod.class)) {
//            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            Method method = handlerMethod.getMethod();
//            return annotationAuthorization(method, request, response);
//        }
//
//        return true;
//    }
//
//
//
//    private UserCredentials getUserCredentials(final HttpServletRequest request) {
//        try {
//            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
//            System.out.println(header + " is header");
//            if (header == null || !header.startsWith("Bearer ")) {
//                return null;
//            }
//            final int bearerIndex = 7;
//            String token = header.substring(bearerIndex);
//            UserCredentials credentials = jwtService.parseToken(token);
//            log.info("Found credentials in Authorization header: {}", credentials.getUserId());
//            request.setAttribute(USER_CREDENTIALS, credentials);
//            return credentials;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    private boolean annotationAuthorization(
//            final Method method,
//            final HttpServletRequest request,
//            final HttpServletResponse response
//    ) {
//        try {
//            UserCredentials userCredentials = getUserCredentials(request);
//
//            if (method.isAnnotationPresent(AuthRoleRequired.class)) {
//                if (userCredentials == null) {
//                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//                    return false;
//                }
//                AuthRoleRequired annotation = method.getAnnotation(AuthRoleRequired.class);
//
//                if (annotation.anyRole() && !userCredentials.getRoles().isEmpty()) {
//                    return true;
//                }
//
//                String[] roles;
//                if (annotation.oneOf().length > 0) {
//                    roles = annotation.oneOf();
//                } else {
//                    roles = new String[]{annotation.value()};
//                }
//
//                Set<String> userRoles = new HashSet<>();
//                if (userCredentials.getRoles() != null) {
//                    userRoles.addAll(userCredentials.getRoles());
//                }
//
//                boolean isAuthorized = false;
//
//                for (final String role : roles) {
//                    if (userRoles.contains(role)) {
//                        isAuthorized = true;
//                        break;
//                    }
//                }
//
//                if (!isAuthorized) {
//                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Not enough permissions");
//                    return false;
//                }
//            }
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}