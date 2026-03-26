package com.bookstore.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import com.bookstore.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UploadBookHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ObjectMapper mapper = new ObjectMapper();
    private final BookService service = new BookService();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        try {
            Book book = mapper.readValue(request.getBody(), Book.class);

            Book saved = service.save(book);

            return ResponseUtil.ok(mapper.writeValueAsString(saved));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error("Error: " + e.getMessage());
        }
    }
}