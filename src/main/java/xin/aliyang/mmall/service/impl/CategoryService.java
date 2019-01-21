package xin.aliyang.mmall.service.impl;


import net.sf.jsqlparser.schema.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.aliyang.mmall.common.ServerResponse;
import xin.aliyang.mmall.dao.CategoryMapper;
import xin.aliyang.mmall.pojo.Category;
import xin.aliyang.mmall.service.ICategoryService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lhy on 2019/1/7.
 */
@Service("categoryService")
public class CategoryService implements ICategoryService {
	@Autowired
	CategoryMapper categoryMapper;

	@Override
	public ServerResponse addCategory(String categoryName, Integer partnerId) {
		if (partnerId == null || StringUtils.isBlank(categoryName)) {
			return ServerResponse.createByErrorMsg("添加品类失败，参数有误");
		}
		Category category = new Category();
		category.setParentId(partnerId);
		category.setName(categoryName);
		category.setStatus(true);  //设置类别状态为可用

		int row = categoryMapper.insertSelective(category);
		if (row > 0) {
			return ServerResponse.createBySuccessMsg("添加品类成功");
		}
		return ServerResponse.createByErrorMsg("添加品类失败");
	}

	@Override
	public ServerResponse setCategoryName(Integer categoryId, String categoryName) {
		if (StringUtils.isBlank(categoryName)) {
			return ServerResponse.createByErrorMsg("更新品类名称失败，参数有误");
		}
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if (category == null) {
			return ServerResponse.createByErrorMsg("更新品类名称失败，不存在该品类id");
		}
		category.setName(categoryName);
		int row = categoryMapper.updateByPrimaryKeySelective(category);
		if (row > 0) {
			return ServerResponse.createBySuccessMsg("更新品类名称成功");
		}
		return ServerResponse.createByErrorMsg("更新品类名称失败");
	}

	@Override
	public ServerResponse<List<Category>> getDirectChildCategory(Integer categoryId) {
		if (categoryId == null) {
			return ServerResponse.createByErrorMsg("查找直接子品类失败，参数有误");
		}

		List<Category> childCategoryList = categoryMapper.selectCategoryByParentId(categoryId);
		if (childCategoryList.isEmpty()) {
			return ServerResponse.createByErrorMsg("查找直接子品类失败，未找到子品类");
		}
		return ServerResponse.createBySuccessData(childCategoryList);
	}

	@Override
	public ServerResponse<List<Integer>> getDeepChildCategory(Integer categoryId) {
		if (categoryId == null) {
			return ServerResponse.createByErrorMsg("查找所有子品类失败，参数有误");
		}
		Set<Category> childSet = new HashSet<>();
		findDeepChildCategory(childSet, categoryId);
		if (childSet.isEmpty()) {
			return ServerResponse.createByErrorMsg("查找所有子品类失败，未找到子品类");
		}
		List<Integer> list = new ArrayList<>();
		for (Category category : childSet) {
			list.add(category.getId());
		}
		return ServerResponse.createBySuccessData(list);
	}

	@Override
	public Category getParentCategory(Integer categoryId) {
		Category category = categoryMapper.selectParentCategory(categoryId);
		return category;
	}

	/**
	 * 根据品类id，递归查询其所有子品类，返回包含所有子品类的set
	 * @param categoryId  待查询的品类id
	 * @return
	 */
	private void findDeepChildCategory(Set childSet, Integer categoryId) {
		List<Category> directChildList = categoryMapper.selectCategoryByParentId(categoryId);
		for (Category category : directChildList) {
			childSet.add(category);    //set自带去重(需重写Category的hashCode和equal方法)
			findDeepChildCategory(childSet, category.getId());    //递归查询
		}
	}
}
