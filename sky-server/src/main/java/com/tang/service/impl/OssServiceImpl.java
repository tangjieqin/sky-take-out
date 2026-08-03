package com.tang.service.impl;

import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.models.PutObjectRequest;
import com.aliyun.sdk.service.oss2.transport.BinaryData;
import com.tang.constant.MessageConstant;
import com.tang.properties.AliOssProperties;
import com.tang.service.OssService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    private final OSSClient ossClient;

    private final AliOssProperties properties;

    public OssServiceImpl(OSSClient ossClient, AliOssProperties properties) {
        this.ossClient = ossClient;
        this.properties = properties;
    }



    @Override
    public String upload(MultipartFile file) throws IOException {
        // 文件为空
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(MessageConstant.UPLOAD_FILE_EMPTY);
        }

        // 原始文件名
        String originalFilename = file.getOriginalFilename();

        // 后缀
        String suffix = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 唯一文件名:目录结构
        String objectName = String.format(
                "%s/%s/%s/%s%s",
                properties.getDirectory(),
                LocalDate.now().getYear(),
                LocalDate.now().getMonthValue(),
                UUID.randomUUID(),
                suffix
        );

        try(InputStream inputStream = file.getInputStream()){
            // 上传请求
            PutObjectRequest request = PutObjectRequest.newBuilder()
                    .bucket(properties.getBucketName())
                    .key(objectName)
                    .body(BinaryData.fromStream(inputStream))
                    .build();

            // 上传
            ossClient.putObject(request);
        }


        // 返回OSS访问地址
        return String.format(
                "https://%s.%s/%s",
                properties.getBucketName(),
                properties.getEndpoint(),
                objectName
        );
    }
}
