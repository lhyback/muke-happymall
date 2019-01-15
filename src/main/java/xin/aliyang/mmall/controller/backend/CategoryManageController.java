package xin.aliyang.mmall.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xin.aliyang.mmall.common.Const;
import xin.aliyang.mmall.common.ResponseCode;
import xin.aliyang.mmall.common.ServerResponse;
import xin.aliyang.mmall.pojo.User;
import xin.aliyang.mmall.service.ICategoryService;
import xin.aliyang.mmall.service.IUserService;

import javax.servlet.http.HttpSession;

/**
 * Created by lhy on 2019/1/7.
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
	@Autowired
	IUserService userService;

	@Autowired
	ICategoryService categoryService;

	@RequestMapping("/add_category.do")
	@ResponseBody
	public ServerResponse addCategory(String categoryName, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId,
									  HttpSession session) {
		//用户鉴权
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
		}
		if (!userService.checkUserRole(user, Const.Role.ROLE_ADMIN)) {
			return ServerResponse.createByErrorMsg("不是admin用户，无权限");
		}

		return categoryService.addCategory(categoryName, parentId);
	}

	@RequestMapping("/set_category_name.do")
	@ResponseBody
	public ServerResponse setCategoryName(Integer categoryId, String categoryName, HttpSession session) {
		//用户鉴权
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
		}
		if (!userService.checkUserRole(user, Const.Role.ROLE_ADMIN)) {
			return ServerResponse.createByErrorMsg("不是admin用户，无权限");
		}

		return categoryService.setCategoryName(categoryId, categoryName);
	}

	@RequestMapping("/get_child_category.do")
	@ResponseBody
	public ServerResponse getDirectChildCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId, HttpSession session) {
		//用户鉴权
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
		}
		if (!userService.checkUserRole(user, Const.Role.ROLE_ADMIN)) {
			return ServerResponse.createByErrorMsg("不是admin用户，无权限");
		}

		return categoryService.getDirectChildCategory(categoryId);
	}

	@RequestMapping("/get_deep_child_category.do")
	@ResponseBody
	public ServerResponse getDeepChildCategory(Integer categoryId, HttpSession session) {
		//用户鉴权
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
		}
		if (!userService.checkUserRole(user, Const.Role.ROLE_ADMIN)) {
			return ServerResponse.createByErrorMsg("不是admin用户，无权限");
		}

		return categoryService.getDeepChildCategory(categoryId);
	}

}
