package com.tang.controller.admin;

import com.tang.dto.CategoryDTO;
import com.tang.dto.CategoryPageQueryDTO;
import com.tang.entity.Category;
import com.tang.result.PageResult;
import com.tang.result.Result;
import com.tang.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理模块
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类：{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(@ParameterObject CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类分页查询:{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.page(categoryPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 根据分类id，删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public Result<String> deleteById(@RequestParam Long id) {
        log.info("删除分类：{}", id);
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类：{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * 启用、禁用分类
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> startOrStop(@PathVariable("status") Integer status, @RequestParam Long id) {
        log.info("启用、禁用分类：id={}, status={}", id, status);
        categoryService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 根据查询分类列表
     * @param type
     * @return
     */
    public Result<List<Category>> listByType(@RequestParam Integer type) {
        log.info("查询分类列表数据：type={}", type);
        List<Category> categoryList = categoryService.listByType(type);
        return Result.success(categoryList);
    }

}
