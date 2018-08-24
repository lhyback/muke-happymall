package xin.aliyang.mmall.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xin.aliyang.mmall.common.Const;
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
		return ServerResponse.createBySuccess();
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


}
