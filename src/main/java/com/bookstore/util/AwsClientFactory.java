package com.bookstore.util;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;

public class AwsClientFactory {

    public static DynamoDbClient dynamo() {
        return DynamoDbClient.create();
    }

    public static S3Client s3() {
        return S3Client.create();
    }
}