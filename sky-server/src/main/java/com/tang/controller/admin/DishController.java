package com.tang.controller.admin;


import com.tang.dto.DishDTO;
import com.tang.dto.DishPageQueryDTO;
import com.tang.result.PageResult;
import com.tang.result.Result;
import com.tang.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品: {}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success("");
    }

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(@ParameterObject DishPageQueryDTO dishPageQueryDTO) {
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
    *批量删除菜品
     * @param ids
     * @return
     * @RequestParam：这个注解可以让是springMVC将字符串分割存到List中
     */
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
            dishService.deleteBatch(ids);
            return Result.success("");
    }

}
