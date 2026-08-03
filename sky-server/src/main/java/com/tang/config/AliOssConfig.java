package com.tang.config;

import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.credentials.CredentialsProvider;
import com.aliyun.sdk.service.oss2.credentials.StaticCredentialsProvider;
import com.tang.properties.AliOssProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 创建 OSS Client Bean
 */
@Configuration
public class AliOssConfig {

    @Bean
    public OSSClient ossClient(AliOssProperties ossProperties) {

        CredentialsProvider credentialsProvider = new StaticCredentialsProvider(
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret()
        );

        OSSClient ossClient = OSSClient.newBuilder().
                credentialsProvider(credentialsProvider)
                .region(ossProperties.getRegion())
                .build();

        return ossClient;
    }

}