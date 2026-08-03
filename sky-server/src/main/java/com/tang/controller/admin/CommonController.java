package com.tang.controller.admin;

import com.tang.constant.MessageConstant;
import com.tang.result.Result;
import com.tang.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {

    @Autowired
    private OssService ossService;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        try {
            String url = ossService.upload(file);
            return Result.success(url);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return Result.error(MessageConstant.UPLOAD_FILE_FAILED);
        }
    }
}
