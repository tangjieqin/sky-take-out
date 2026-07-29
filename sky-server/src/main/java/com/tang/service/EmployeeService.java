package com.tang.service;

import com.tang.dto.EmployeeDTO;
import com.tang.dto.EmployeePageQueryDTO;
import com.tang.entity.Employee;
import com.tang.dto.EmployeeLoginDTO;
import com.tang.result.PageResult;

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

    /**
     * 分页查询员工信息
     * @param employeePageQueryDTO
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 创建启用禁用员工账号
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 通过id查询员工
     * @param id
     * @return
     */
    Employee getEmployeeById(Long id);

    /**
     * 编辑员工信息
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);
}
