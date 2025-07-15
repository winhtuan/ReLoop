package Utils.fileHandle;

import Utils.AppConfig;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.net.URL;
import software.amazon.awssdk.core.sync.RequestBody;

public class S3Util {

    private static final String ACCESS_KEY = new AppConfig().get("aws.accessKeyId");
    private static final String SECRET_KEY = new AppConfig().get("aws.secretAccessKey");
    private static final String BUCKET_NAME = new AppConfig().get("aws.bucketName");
    private static final Region REGION = Region.of(new AppConfig().get("aws.region")); // ví dụ: Singapore

    private static final S3Client s3 = S3Client.builder()
            .region(REGION)
            .credentialsProvider(
                    StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)
                    )
            )
            .build();

    public static String uploadFile(String fileName, InputStream inputStream, long size, String contentType) {
        PutObjectRequest putReq = PutObjectRequest.builder()
        .bucket(BUCKET_NAME)
        .key(fileName)
        .contentType(contentType)
        .build();


        s3.putObject(putReq, RequestBody.fromInputStream(inputStream, size));

        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                BUCKET_NAME, REGION.id(), fileName);
    }
}
