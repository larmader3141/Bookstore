package com.bookstore.service;

import com.bookstore.model.Book;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.opensearch.OpenSearchClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.*;

public class BookService {
    private final DynamoDbClient dynamo = DynamoDbClient.create();
    private final String tableName = System.getenv("TABLE_NAME");

    public Book save(Book book) {

        book.setBookId(UUID.randomUUID().toString());

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("bookId", AttributeValue.builder().s(book.getBookId()).build());
        item.put("title", AttributeValue.builder().s(book.getTitle()).build());
        item.put("author", AttributeValue.builder().s(book.getAuthor()).build());
        item.put("fileKey", AttributeValue.builder().s(book.getFileKey()).build());

        dynamo.putItem(PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build());

        return book;
    }

    public String search(String query) {
        return "Searching for: " + query;
    }

    public String generateUploadUrl(String fileKey) {

        S3Presigner presigner = S3Presigner.create();

        String bucket = System.getenv("BUCKET_NAME");
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileKey)
                .build();

        PutObjectPresignRequest presignRequest =
                PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .putObjectRequest(objectRequest)
                        .build();

        return presigner.presignPutObject(presignRequest)
                .url()
                .toString();
    }

    public String generateDownloadUrl(String fileKey) {

        String bucket = System.getenv("BUCKET_NAME");

        S3Presigner presigner = S3Presigner.create();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileKey)
                .build();

        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10)) // expires in 10 min
                        .getObjectRequest(getObjectRequest)
                        .build();

        return presigner.presignGetObject(presignRequest)
                .url()
                .toString();
    }

    public Book getBookById(String bookId) {

        Map<String, AttributeValue> key = Map.of(
                "bookId", AttributeValue.builder().s(bookId).build()
        );

        GetItemRequest request = GetItemRequest.builder()
                .tableName(System.getenv("TABLE_NAME"))
                .key(key)
                .build();

        Map<String, AttributeValue> item = dynamo.getItem(request).item();

        if (item == null || item.isEmpty()) {
            return null;
        }

        Book book = new Book();
        book.setBookId(item.get("bookId").s());
        book.setTitle(item.get("title").s());
        book.setAuthor(item.get("author").s());
        book.setFileKey(item.get("fileKey").s());

        return book;
    }

    public List<Book> getAllBooks() {

        ScanRequest request = ScanRequest.builder()
                .tableName(System.getenv("TABLE_NAME"))
                .build();

        ScanResponse response = dynamo.scan(request);

        List<Book> books = new ArrayList<>();

        for (Map<String, AttributeValue> item : response.items()) {
            Book book = new Book();

            book.setBookId(item.get("bookId").s());
            book.setTitle(item.get("title").s());
            book.setAuthor(item.get("author").s());
            book.setFileKey(item.get("fileKey").s());

            books.add(book);
        }

        return books;
    }

    public void indexBook(Book book) {

        OpenSearchClient client = OpenSearchClient.create();

        // simplified (real version uses HTTP or OpenSearch SDK JSON)
        System.out.println("Indexing book: " + book.getTitle());
    }
}
