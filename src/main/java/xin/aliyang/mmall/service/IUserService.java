package xin.aliyang.mmall.service;

import xin.aliyang.mmall.common.Const;
import xin.aliyang.mmall.common.ServerResponse;
import xin.aliyang.mmall.pojo.User;

/**
 * Created by lhy on 2018/8/14.
 */
public interface IUserService {
	ServerResponse<User> login(String username, String password);

	//检查注册时用户输入的用户名和邮箱是否已存在
	ServerResponse checkValid(String str, String type);

	ServerResponse register(User user);

	ServerResponse<String> getForgetQuestion(String username);

	ServerResponse<String> checkForgetAnswer(String username, String question, String answer);

	ServerResponse resetForgetPassword(String username, String passwordNew, String token);

	ServerResponse resetPassword(String passwordOld, String passwordNew, User user);

	ServerResponse updateInformation(User user);

	Boolean checkUserRole(User user, Integer roleCode);
}

