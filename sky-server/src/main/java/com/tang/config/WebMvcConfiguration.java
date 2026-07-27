package com.tang.config;

import com.tang.interceptor.JwtTokenAdminInterceptor;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;


    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                // 用户必须先登录获取令牌，之后才能访问其他需要权限的接口
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");
    }


    // 定义一个 API 分组


    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("default")  // 分组名称是 "default"，在文档页面会显示为"default"标签
                .packagesToScan("com.tang.controller")  // 扫描 com.tang.controller 包下的所有控制器
                .pathsToMatch("/**")  // 扫描所有路径（/** 表示所有接口）
                .build();
    }
    /* @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("管理端接口")
                .pathsToMatch("/controller/admin/**")   // 只扫描 /admin 开头的接口
                .build();
    }*/

    @Bean
    public OpenAPI customOpenAPI() {  //定义文档的全局信息
        return new OpenAPI()
                .info(new Info()  // 设置文档信息
                        .title("苍穹外卖 API 文档")  //
                        .version("1.0")  //
                        .description("苍穹外卖项目接口文档"));  //

    }


    /**
    *
     * 配置静态资源映射
     * @param registry
     *//*
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 让 doc.html 能访问到
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        // 让 webjars 资源能访问到
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }*/

}