package com.tang.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeLoginVO implements Serializable {

    private Long id;   // 主键值

    private String username;  // 用户名

    private String name;  // 姓名

    private String password;

    private String token;  // 登录凭证jwt


}
