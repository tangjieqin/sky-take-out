package com.tang.service;

import com.tang.dto.DishDTO;
import com.tang.dto.DishPageQueryDTO;
import com.tang.result.PageResult;

public interface DishService {

    /**
     * 新增菜品和对应口味
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
