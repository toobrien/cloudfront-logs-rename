package com.github.toobrien;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.event.S3EventNotification.S3Entity;
import com.amazonaws.AmazonClientException;
import java.util.UUID;

public class Handler {

  private AmazonS3 s3;
  private String destinationBucketName = "toobrien2";
  

  public Handler() {
    s3 = AmazonS3ClientBuilder.defaultClient();
  }

  public void handleRequest(S3Event event, Context context) {
    S3Entity e = event.getRecords().get(0).getS3();
    String sourceBucketName = e.getBucket().getName();
    String sourceKey = e.getObject().getKey();

    // for good s3 partitioning
    String destinationPrefix = UUID.randomUUID().toString(); 

    try {
      s3.copyObject(sourceBucketName, sourceKey, 
        destinationBucketName, destinationPrefix + "/" + sourceKey);
    } catch (AmazonClientException ex) {
      // handle exception
    }
  }
}

