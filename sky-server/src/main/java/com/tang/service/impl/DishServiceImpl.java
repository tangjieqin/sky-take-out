package com.tang.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tang.constant.MessageConstant;
import com.tang.constant.StatusConstant;
import com.tang.dto.DishDTO;
import com.tang.dto.DishPageQueryDTO;
import com.tang.entity.Dish;
import com.tang.entity.DishFlavor;
import com.tang.exception.DeletionNotAllowedException;
import com.tang.mapper.DishFlavorMapper;
import com.tang.mapper.DishMapper;
import com.tang.mapper.SetmealDishMapper;
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

    @Autowired
    private SetmealDishMapper setmealDishMapper;

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

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {

        // 判断菜品是否为起售状态：有一个异常，则退出整个删除方法
        ids.forEach(id -> {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }

        });

        // 判断菜品是否有套餐关联，有一个关联则退出整个删除方法
        List<Long> setmealIds = setmealDishMapper.getSetmealIdByDishId(ids);
        if (!setmealIds.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 批量删除菜品和口味数据
        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);

    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        // 根据id查询菜品的信息
        Dish dish = dishMapper.getById(id);

        // 根据菜品id查询口味数据
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);

        // 数据合并
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);

        return dishVO;
    }

    @Override
    public void updateWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 更新菜品信息
        dishMapper.update(dish);

        // 删除旧的口味数据,再插入新的
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        // 更新口味信息
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            // 更新一下口味的dishId
             flavors.forEach(flavor -> flavor.setDishId(dishDTO.getId()));
            // 批量插入口味数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }
}
