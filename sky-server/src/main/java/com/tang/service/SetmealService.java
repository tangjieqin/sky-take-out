package com.tang.service;

import com.tang.dto.SetmealDTO;
import com.tang.dto.SetmealPageQueryDTO;
import com.tang.result.PageResult;
import com.tang.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 新增套餐同时关联菜品
     * @param setmealDTO
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteBatch(List<Long> ids);


    /**
     * 根据id查询套餐信息,用于修改页面回显数据
     * @param id
     * @return
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * 修改套餐信息,同时关联菜品
     * @param setmealDTO
     */
    void updateWithDish(SetmealDTO setmealDTO);


    /**
     * 起售和停售套餐
     * @param status
     * @param setmealId
     */
    void startOrStop(Integer status, Long setmealId);
}
