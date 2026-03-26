package com.bookstore.handler;

import com.amazonaws.services.lambda.runtime.*;
import com.amazonaws.services.lambda.runtime.events.*;

import com.bookstore.service.BookService;
import com.bookstore.util.ResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.UUID;

public class GenerateUploadUrlHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final BookService service = new BookService();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        // generate a unique file name
        String fileKey = "books/" + UUID.randomUUID() + ".pdf";

        String url = service.generateUploadUrl(fileKey);

        ObjectMapper mapper = new ObjectMapper();

        Map<String, String> response = Map.of(
                "uploadUrl", url,
                "fileKey", fileKey
        );

        try {
            return ResponseUtil.ok(mapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}