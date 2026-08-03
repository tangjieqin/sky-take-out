package com.tang.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云OSS配置类: 配置属性类，直接从application.yml中读取配置项封装成Java对象
 */
@Component
@ConfigurationProperties(prefix = "sky.oss")
@Data
public class AliOssProperties {

    private String endpoint;
    private String region;
    private String bucketName;
    private String accessKeyId;
    private String accessKeySecret;

    /**
     * OSS文件目录
     */
    private String directory = "images";

}
