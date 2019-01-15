package xin.aliyang.mmall.service;

import org.springframework.stereotype.Service;
import xin.aliyang.mmall.common.ServerResponse;
import xin.aliyang.mmall.pojo.Category;

import java.util.List;

/**
 * Created by lhy on 2019/1/7.
 */
public interface ICategoryService {
	ServerResponse addCategory(String categoryName, Integer partnerId);

	ServerResponse setCategoryName(Integer categoryId, String categoryName);

	ServerResponse<List<Category>> getDirectChildCategory(Integer categoryId);

	ServerResponse<List<Integer>> getDeepChildCategory(Integer categoryId);

	Category getParentCategory(Integer categoryId);
}
