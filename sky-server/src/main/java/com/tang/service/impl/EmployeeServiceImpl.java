package com.tang.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tang.constant.MessageConstant;
import com.tang.constant.PasswordContant;
import com.tang.constant.StatusConstant;
import com.tang.dto.EmployeeDTO;
import com.tang.dto.EmployeeLoginDTO;
import com.tang.dto.EmployeePageQueryDTO;
import com.tang.entity.Employee;
import com.tang.exception.AccountNotFoundException;
import com.tang.exception.PasswordErrorException;
import com.tang.mapper.EmployeeMapper;
import com.tang.result.PageResult;
import com.tang.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

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

    @Override
    public void save(EmployeeDTO employeeDTO) {
        // 持久层是需要实体类，属性拷贝，必须要属性名一致
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        // 填充其他的属性：账号状态默认1，密码默认123456
        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordContant.DEFAULT_PASSWORD.getBytes(StandardCharsets.UTF_8)));
        // 保存到数据库
        employeeMapper.insert(employee);
    }

    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // 基于PageHelper插件实现动态分页查询，底层是mybatis,做了一个字符串的拼接
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        long total = page.getTotal();
        List<Employee> recordes = page.getResult();

        return new PageResult(total, recordes);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        // 更新员工：动态更新
        Employee employee = Employee.builder().id(id).status(status).build();
        employeeMapper.update(employee);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        Employee employee = employeeMapper.getEmployeeById(id);
        employee.setPassword("****");
        return employee;
    }

    @Override
    public void update(EmployeeDTO employeeDTO) {
        // Dto转换成employee
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employeeMapper.update(employee);
    }
}
