package com.kernelsquare.domains3.domain.image.repository;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domains3.domain.image.command.ImageCommand;
import com.kernelsquare.domains3.domain.image.info.ImageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ImageReaderImpl implements ImageReader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public ImageInfo findAll(ImageCommand.FindAllImages command) {
        String prefix = command.createdDate() + "/";

        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request()
            .withBucketName(bucket)
            .withPrefix(prefix);

        ListObjectsV2Result listObjectsV2Result = amazonS3Client.listObjectsV2(listObjectsV2Request);

        List<S3ObjectSummary> objectSummaries = listObjectsV2Result.getObjectSummaries();

        List<String> imageList = objectSummaries.stream()
            .map(objectSummary -> {
                String key = objectSummary.getKey();
                S3Object object = amazonS3Client.getObject(bucket, key);
                return ImageUtils.makeImageUrl(object.getKey());
            })
            .toList();

        return ImageInfo.from(imageList);
    }
}
