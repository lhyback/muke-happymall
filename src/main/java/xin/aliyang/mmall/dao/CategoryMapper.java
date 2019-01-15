package xin.aliyang.mmall.dao;

import xin.aliyang.mmall.pojo.Category;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Category> selectCategoryByParentId(Integer parnetId);

    Category selectParentCategory(Integer categoryParentId);
}