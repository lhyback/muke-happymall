package xin.aliyang.mmall.service.impl;


import net.sf.jsqlparser.schema.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.aliyang.mmall.common.ServerResponse;
import xin.aliyang.mmall.dao.CategoryMapper;
import xin.aliyang.mmall.pojo.Category;
import xin.aliyang.mmall.service.ICategoryService;

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
}
