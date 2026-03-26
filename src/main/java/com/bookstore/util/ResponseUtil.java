package com.bookstore.util;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.Map;

public class ResponseUtil {

    public static APIGatewayProxyResponseEvent ok(String body) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withHeaders(Map.of("Content-Type", "application/json"))
                .withBody(body);
    }

    public static APIGatewayProxyResponseEvent error(String message) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(500)
                .withBody(message);
    }
}
