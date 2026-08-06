package com.tang.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tang.constant.MessageConstant;
import com.tang.constant.StatusConstant;
import com.tang.dto.SetmealDTO;
import com.tang.dto.SetmealPageQueryDTO;
import com.tang.entity.Dish;
import com.tang.entity.Setmeal;
import com.tang.entity.SetmealDish;
import com.tang.exception.DeletionNotAllowedException;
import com.tang.mapper.DishMapper;
import com.tang.mapper.SetmealDishMapper;
import com.tang.mapper.SetmealMapper;
import com.tang.result.PageResult;
import com.tang.service.SetmealService;
import com.tang.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    @Override
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        // 向套餐表插入数据,并返回最新自增的id值
        setmealMapper.insert(setmeal);

        // 套餐菜品关系：一个套餐多个菜品
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        // 更新套餐菜品关系中套餐的最新id
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
        setmealDishMapper.insertBatch(setmealDishes);

    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page  = setmealMapper.pageQuery(setmealPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        // 判断套餐是否在起售中
        ids.forEach(id -> {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal.getStatus() == 1) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        // 删除套餐表和菜品关系
        setmealMapper.deleteBatch(ids);
        setmealDishMapper.deleteBySetmealIds(ids);
    }


    @Override
    public SetmealVO getByIdWithDish(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    @Transactional
    @Override
    public void updateWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        // 修改套餐表
        setmealMapper.update(setmeal);

        // 修改套餐菜品关系：先删除再批量修改
        Long setmealId= setmealDTO.getId();
        setmealDishMapper.deleteBySetmealIds(List.of(setmealId));

        // 批量插入套餐菜品关系:先更新一下套餐id
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Override
    public void startOrStop(Integer status, Long setmealId) {

        // 判断套餐内是否有停售菜品，有停售菜品提示"套餐内包含未启售菜品，无法启售"
        if (status == StatusConstant.ENABLE) {
            List<Dish> dishList = dishMapper.getDishBySetmealId(setmealId);
            // 判断套餐包含的菜品是否有停售
            dishList.forEach(dish -> {
                if (dish.getStatus() == StatusConstant.DISABLE) {
                    throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            });
        }

        // 先更新套餐表的状态
        Setmeal setmeal = Setmeal.builder().status(status).id(setmealId).build();
        setmealMapper.update(setmeal);
    }

}
