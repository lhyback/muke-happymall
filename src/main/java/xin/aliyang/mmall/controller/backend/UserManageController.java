package xin.aliyang.mmall.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xin.aliyang.mmall.common.Const;
import xin.aliyang.mmall.common.ServerResponse;
import xin.aliyang.mmall.pojo.User;
import xin.aliyang.mmall.service.IUserService;

import javax.servlet.http.HttpSession;

/**
 * Created by lhy on 2018/9/12.
 */
@Controller
@RequestMapping("/manage/user")
public class UserManageController {

	@Autowired
	IUserService userService;

	@RequestMapping("/login.do")
	@ResponseBody
	public ServerResponse<User> login(String username, String password, HttpSession session) {
		ServerResponse response = userService.login(username, password);
		if (response.isSuccessful()) {
			User user = (User) response.getData();
			if (user.getRole() == Const.Role.ROLE_ADMIN) {
				session.setAttribute(Const.CURRENT_USER, user);
				return response;
			}
			return ServerResponse.createByErrorMsg("not admin");
		}
		return response;
	}


}
