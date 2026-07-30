package com.tang.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tang.BaseContext;
import com.tang.constant.MessageConstant;
import com.tang.constant.StatusConstant;
import com.tang.dto.CategoryDTO;
import com.tang.dto.CategoryPageQueryDTO;
import com.tang.entity.Category;
import com.tang.exception.DeletionNotAllowedException;
import com.tang.mapper.CategoryMapper;
import com.tang.mapper.DishMapper;
import com.tang.mapper.SetmealMapper;
import com.tang.result.PageResult;
import com.tang.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;


    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        // 分类装填默认禁止0
        category.setStatus(StatusConstant.DISABLE);

        // 插入数据库
        categoryMapper.insert(category);
    }

    @Override
    public PageResult page(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        // 下一条sql进行分页，自动加入limit关键字分页
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void deleteById(Long id) {
        // 查询当前的id是否关联了菜品，如果关联了就抛异常
        Integer dishCount = dishMapper.countByCategoryId(id);
        if (dishCount > 0) {
            // 当前分类下关联了菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        // 查询当前分类是否关联了套餐，如果关联则抛出异常
        Integer setMealCount = setmealMapper.countByCategoryId(id);
        if (setMealCount > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        // 删除分类数据
        categoryMapper.deleteById(id);
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.update(category);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        categoryMapper.update(category);
    }

    @Override
    public List<Category> listByType(Integer type) {
        return categoryMapper.list(type);
    }
}
