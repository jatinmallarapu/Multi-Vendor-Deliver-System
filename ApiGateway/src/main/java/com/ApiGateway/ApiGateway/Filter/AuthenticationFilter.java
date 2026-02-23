package com.ApiGateway.ApiGateway.Filter;

import com.ApiGateway.ApiGateway.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpMethod;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>{
    @Autowired
    private RouteValidator routeValidator;

    //@Autowired
    //private RestTemplate template;

    @Autowired
    private JwtUtils jwtUtils;

    public AuthenticationFilter(){
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {

            // Allow CORS preflight requests to pass without authentication
            if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
                return chain.filter(exchange);
            }

            /*
            //Step 1 — Identify Secured Requests
            If the incoming URL is secured (not in openApiEndpoints), then:
            ->It must have a token
            ->We must validate token

            So,
            AuthenticationFilter → calls → RouteValidator
            If SECURED → Validate token
            If NOT secured → Skip validation
             */
            if(routeValidator.isSecured.test(exchange.getRequest())){
                /*
                //Step 2 — Check Header
                ->This ensures header contains: Authorization: Bearer <token>
                */
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing Authorization header");
                }
                //Step 3 — Extract Token: Removes "Bearer " prefix.
                String authHeader=exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                if(authHeader!=null && authHeader.startsWith("Bearer ")){
                    authHeader=authHeader.substring(7);
                    try{
                        //rest call to auth service
                        //System.out.println(authHeader);
                        //template.getForObject("http://IDENTITY-SERVICE//validateToken?token="+authHeader, String.class);
                        /*
                        //Step 4 — Validate Token
                        ->The AuthenticationFilter.java uses JwtUtils
                        ->Token is validated directly inside gateway
                        ->If token invalid → throw exception → request blocked
                         */
                        jwtUtils.validateToken(authHeader);
                    }catch (Exception e){
                        System.out.println("Invalid access");
                        throw new RuntimeException("un authorized access to application");
                    }
                }
            }
            /*
            //Step 5 — Forward Request
            Means:
            ->Filter passed → let the request move to microservice.
             */
            return chain.filter(exchange);
        }));
    }

    public static class Config{

    }

    /*
    How AuthenticationFilter interacts with other files:
    ->Uses RouteValidator to check which URLs need validation
    ->Uses JwtUtils to validate JWT token
    ->Triggered by SwiggyGatewayConfiguration on secured routes
    ->Runs BEFORE request is forwarded to the backend microservice
     */
}
