package com.tang.controller.admin;

import com.tang.config.JwtProperties;
import com.tang.constant.JwtClaimsConstant;
import com.tang.dto.EmployeeDTO;
import com.tang.dto.EmployeeLoginDTO;
import com.tang.dto.EmployeePageQueryDTO;
import com.tang.entity.Employee;
import com.tang.result.PageResult;
import com.tang.result.Result;
import com.tang.service.EmployeeService;
import com.tang.util.JwtUtil;
import com.tang.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtProperties jwtProperties;


    /**
     * 用户登录
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {

        Employee employee = employeeService.login(employeeLoginDTO);

        // 生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        claims.put(JwtClaimsConstant.USERNAME, employee.getUsername());

        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .username(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }


    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    @PostMapping
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("save employee: {}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO: 单个参数传入不是json,所以自动封装成dto，不用@RequestBody
     * @return
     * get没有请求体
     */
    @GetMapping("/page")
    public Result<PageResult> page(@ParameterObject EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("page query:{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /*
     * 创建启用禁用员工账号
     * @param status
     * @param id
     * @return
     */

    @PostMapping("/status/{status}")  // Spring会自动将路径中的 {id} 绑定到参数 id
    public Result startOrStop(@PathVariable Integer status, @RequestParam Long id) {
        log.info("启用禁用员工账号：{},{}",status,id);
        employeeService.startOrStop(status,id);
        return Result.success();
    }
}
