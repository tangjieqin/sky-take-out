package com.tang.service;

import com.tang.dto.EmployeeDTO;
import com.tang.entity.Employee;
import com.tang.dto.EmployeeLoginDTO;

public interface EmployeeService {

    /**
     * 用户登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO) ;

    /**
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);
}
