package com.bookstore.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.bookstore.service.BookService;
import com.bookstore.util.ResponseUtil;

public class SearchBookHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final BookService service = new BookService();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        String query = request.getQueryStringParameters().get("query");

        String result = service.search(query);

        return ResponseUtil.ok("{\"result\":\"" + result + "\"}");
    }
}