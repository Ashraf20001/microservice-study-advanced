package com.example.gateway.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;


@Component
public class FilterUtility {

    public static final String CORRELATION_ID = "microservice-correlation-id";

    public String getCorrelationId(HttpHeaders requestHeader){
       if(requestHeader != null && requestHeader.get(CORRELATION_ID) != null){
           List<String> requestHeaderValueList = requestHeader.get(CORRELATION_ID);
           return requestHeaderValueList.stream().findFirst().get();
       }
        return null;
    }

    public ServerWebExchange setRequestHeader(ServerWebExchange exchange , String name , String value){
        return exchange.mutate().request(exchange.getRequest().mutate().header(name,value).build()).build();
    }

    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId){
        return setRequestHeader(exchange,CORRELATION_ID,correlationId);
    }
}
