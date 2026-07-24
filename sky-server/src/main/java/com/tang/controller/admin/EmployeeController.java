package com.tang.controller.admin;


import com.tang.dto.EmployeeLoginDTO;
import com.tang.entity.Employee;
import com.tang.result.Result;
import com.tang.service.EmployeeService;
import com.tang.vo.EmployeeLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工管理
 */
@RestController
@RequestMapping("admin/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    /**
     * 用户登录
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {

        Employee employee = employeeService.login(employeeLoginDTO);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .username(employee.getUsername())
                .name(employee.getName())
                .token("11111111111111114444")
                .build();
        return Result.success(employeeLoginVO);
    }
}
