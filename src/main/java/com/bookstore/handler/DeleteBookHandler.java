package com.bookstore.handler;
import com.amazonaws.services.lambda.runtime.*;
import com.amazonaws.services.lambda.runtime.events.*;

import com.bookstore.service.BookService;
import com.bookstore.util.ResponseUtil;

public class DeleteBookHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final BookService service = new BookService();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        try {
            String bookId = request.getPathParameters().get("bookId");

            service.deleteBook(bookId);

            return ResponseUtil.ok("{\"message\":\"Book deleted\"}");

        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
}