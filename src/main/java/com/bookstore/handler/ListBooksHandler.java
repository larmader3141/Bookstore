package com.bookstore.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import com.bookstore.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class ListBooksHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final BookService service = new BookService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        try {
            List<Book> books = service.getAllBooks();

            return ResponseUtil.ok(
                    mapper.writeValueAsString(books)
            );

        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
}