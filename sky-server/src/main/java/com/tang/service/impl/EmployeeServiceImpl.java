package com.tang.service.impl;

import com.tang.constant.MessageConstant;
import com.tang.dto.EmployeeLoginDTO;
import com.tang.entity.Employee;
import com.tang.exception.AccountNotFoundException;
import com.tang.exception.PasswordErrorException;
import com.tang.mapper.EmployeeMapper;
import com.tang.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //3、密码比对:MD5加密后比对
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
       if (!password.equals(employee.getPassword())) {
           // 密码错误
           throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
       }


        return employee;
    }
}
