package org.superbiz.moviefun.blob;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.superbiz.moviefun.blob.BlobStore;

import java.io.IOException;
import java.util.Optional;

public class S3Store implements BlobStore {
    private AmazonS3Client s3Client;
    private String s3BucketName;

    public S3Store(AmazonS3Client s3Client, String s3BucketName) {
        this.s3Client = s3Client;
        this.s3BucketName = s3BucketName;

        if( !s3Client.doesBucketExist(s3BucketName)) {
            s3Client.createBucket(s3BucketName);
        }
    }

    @Override
    public void put(Blob blob) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.addUserMetadata("contentType", blob.contentType);

        s3Client.putObject(s3BucketName, blob.name, blob.inputStream, objectMetadata);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        S3Object object = s3Client.getObject(s3BucketName, name);

        Blob blob = new Blob(object.getKey(), object.getObjectContent(), object.getObjectMetadata().getUserMetaDataOf("contentType"));
        return Optional.of(blob);
    }

    @Override
    public void deleteAll() {

    }
}
