package com.tang.controller.admin;

import com.tang.dto.SetmealDTO;
import com.tang.dto.SetmealPageQueryDTO;
import com.tang.result.PageResult;
import com.tang.result.Result;
import com.tang.service.SetmealService;
import com.tang.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @PostMapping
    public Result<String> save(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐: {}", setmealDTO);
        setmealService.saveWithDish(setmealDTO);
        return Result.success("");
    }


    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(@ParameterObject SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
    * 批量删除套餐
     * @param ids
     * @return
     * @RequestParam：这个注解可以让是springMVC将字符串分割存到List中
     */
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
        setmealService.deleteBatch(ids);
        return Result.success("");
    }

    /**
     * 根据id查询套餐信息,用于修改页面回显数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealVO> getSetmealById(@PathVariable Long id) {
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐信息
     * @param setmealDTO
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody SetmealDTO setmealDTO) {
        log.info("更新套餐: {}", setmealDTO);
        setmealService.updateWithDish(setmealDTO);
        return Result.success("");
    }


    /**
     * 起售和停售套餐
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> startOrStop(@PathVariable("status") Integer status, @RequestParam Long id) {
        setmealService.startOrStop(status, id);
        return Result.success("");
    }

}
