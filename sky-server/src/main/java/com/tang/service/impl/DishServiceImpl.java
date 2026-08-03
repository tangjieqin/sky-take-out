package com.tang.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tang.dto.DishDTO;
import com.tang.dto.DishPageQueryDTO;
import com.tang.entity.Dish;
import com.tang.entity.DishFlavor;
import com.tang.mapper.DishFlavorMapper;
import com.tang.mapper.DishMapper;
import com.tang.result.PageResult;
import com.tang.service.DishService;
import com.tang.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    //涉及到菜品和口味表，需要事务，确保数据的一致性，启动类已经开启事务
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 向菜品表插入一条
        dishMapper.insert(dish);

        // 向口味表插入多条
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            // 遍历口味列表，将菜品id赋值给每个口味的dishId属性
            flavors.forEach(flavor -> flavor.setDishId(dish.getId()));
            // 批量插入口味数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {

        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> pageResult = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(pageResult.getTotal(), pageResult.getResult());
    }
}
