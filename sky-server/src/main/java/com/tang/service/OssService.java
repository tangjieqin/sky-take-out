package com.tang.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OssService {
    /**
     * 上传文件
     *
     * @param file 上传文件
     * @return 文件访问地址
     */
    String upload(MultipartFile file) throws IOException;
}
