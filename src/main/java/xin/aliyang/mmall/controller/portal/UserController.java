package xin.aliyang.mmall.controller.portal;

import net.sf.jsqlparser.schema.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xin.aliyang.mmall.common.Const;
import xin.aliyang.mmall.common.ResponseCode;
import xin.aliyang.mmall.common.ServerResponse;
import xin.aliyang.mmall.common.TokenCache;
import xin.aliyang.mmall.pojo.User;
import xin.aliyang.mmall.service.IUserService;
import xin.aliyang.mmall.service.impl.UserService;

import javax.servlet.http.HttpSession;

/**
 * Created by lhy on 2018/8/14.
 */
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	IUserService userService;

	@RequestMapping(value = "/login.do")
	@ResponseBody()
	//这里不需要@RequestParam
	public ServerResponse<User> login(String username, String password, HttpSession session) {
		ServerResponse response = userService.login(username, password);
		if (response.isSuccessful()) {
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		return response;
	}

	@RequestMapping(value = "/logout.do")
	@ResponseBody
	public ServerResponse loginout(HttpSession session) {
		session.removeAttribute(Const.CURRENT_USER);
		return ServerResponse.createBySuccessMsg("退出成功");
	}

	@RequestMapping(value = "/register.do")
	@ResponseBody
	public ServerResponse register(User user) {
	// 直接由springmvc从请求参数构造一个user对象
		return userService.register(user);
	}

	@RequestMapping(value = "/check_valid.do")
	@ResponseBody
	public ServerResponse checkValid(String str, String type) {
		return userService.checkValid(str, type);
	}

	@RequestMapping(value = "/get_user_info.do")
	@ResponseBody
	public ServerResponse getUserInfo(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user != null) {
			return ServerResponse.createBySuccessData(user);
		} else {
			return ServerResponse.createByErrorMsg("用户未登录，无法获取当前用户信息");
		}
	}

	@RequestMapping(value = "/forget_get_question.do")
	@ResponseBody
	public ServerResponse<String> getForgetQuestion(String username) {
		return userService.getForgetQuestion(username);
	}


	@RequestMapping(value = "/forget_check_answer.do")
	@ResponseBody
	public ServerResponse<String> checkForgetAnswer(String username, String question, String answer) {
		return userService.checkForgetAnswer(username, question, answer);
	}

	@RequestMapping(value = "/forget_reset_password.do")
	@ResponseBody
	public ServerResponse resetForgetPassword(String username, String passwordNew, String token) {
		return userService.resetForgetPassword(username, passwordNew, token);
	}

	@RequestMapping(value = "/reset_password.do")
	@ResponseBody
	public ServerResponse resetPassword(String passwordOld, String passwordNew, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorMsg("用户未登录");
		}
		return userService.resetPassword(passwordOld, passwordNew, user);
	}

	@RequestMapping("/update_information.do")
	@ResponseBody
	//SpringMVC的数据绑定 支持FORM格式参数直接转POJO
	public ServerResponse<User> updateInformation(User user, HttpSession session) {
		User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorMsg("用户未登录");
		}
		//不可修改的属性
		user.setId(currentUser.getId());
		user.setUsername(currentUser.getUsername());

		ServerResponse<User> response = userService.updateInformation(user);
		//若修改成功，需更新session中对应的current_user
		if (response.isSuccessful()) {
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		return response;
	}

	@RequestMapping("/get_information.do")
	@ResponseBody
	public ServerResponse<User> getInformation(HttpSession session) {
		User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要强制登录status=10");
		}
		return ServerResponse.createBySuccessData(currentUser);
	}

}
