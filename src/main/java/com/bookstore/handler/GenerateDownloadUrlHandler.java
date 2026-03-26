package com.bookstore.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.bookstore.service.BookService;
import com.bookstore.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class GenerateDownloadUrlHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final BookService service = new BookService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        String fileKey = request.getQueryStringParameters().get("fileKey");

        String url = service.generateDownloadUrl(fileKey);

        try {
            return ResponseUtil.ok(
                    mapper.writeValueAsString(Map.of("downloadUrl", url))
            );
        } catch (Exception e) {
            return ResponseUtil.error("Error generating URL");
        }
    }
}