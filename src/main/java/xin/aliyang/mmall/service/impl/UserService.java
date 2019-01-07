package xin.aliyang.mmall.service.impl;

import net.sf.jsqlparser.schema.Server;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.aliyang.mmall.common.Const;
import xin.aliyang.mmall.common.ServerResponse;
import xin.aliyang.mmall.common.TokenCache;
import xin.aliyang.mmall.dao.UserMapper;
import xin.aliyang.mmall.pojo.User;
import xin.aliyang.mmall.service.IUserService;
import xin.aliyang.mmall.util.MD5Util;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * Created by lhy on 2018/8/14.
 */

@Service("userService")
public class UserService implements IUserService {
	@Autowired
	UserMapper userMapper;

	@Override
	public ServerResponse<User> login(String username, String password) {
		//检查有无该用户名
		int count = userMapper.checkUsername(username);
		if (count == 0) {
			return ServerResponse.createByErrorMsg("用户名不存在");
		}
		User user = userMapper.selectUserByUsernameAndPwd(username, MD5Util.MD5EncodeUtf8(password));
		if (user == null) {
			return ServerResponse.createByErrorMsg("密码错误");
		}
		//password不能返回给前端
		user.setPassword(StringUtils.EMPTY);
		return ServerResponse.createBySuccessData(user); //user -> data
	}

	@Override
	public ServerResponse checkValid(String str, String type) {
		int count;
		//开始校验
		if (Const.EMAIL.equals(type)) {
			count = userMapper.checkEmail(str);
			if (count > 0) {
				return ServerResponse.createByErrorMsg("校验失败,该用户名已存在");
			} else {
				return ServerResponse.createBySuccessMsg("校验成功,该用户名可用");
			}
		} else if (Const.USERNAME.equals(type)) {
			count = userMapper.checkUsername(str);
			if (count > 0) {
				return ServerResponse.createByErrorMsg("校验失败,该邮箱已存在");
			} else {
				return ServerResponse.createBySuccessMsg("校验成功,该邮箱可用");
			}
		}

		return ServerResponse.createByErrorMsg("待校验参数类型错误");
	}

	@Override
	public ServerResponse register(User user) {
		String username = user.getUsername();
		String email = user.getEmail();

		//注册时需要校验
		ServerResponse validResponse = checkValid(username, Const.USERNAME);
		if (!validResponse.isSuccessful()) {
			return validResponse;
		} else {
			validResponse = checkValid(email, Const.EMAIL);
			if (!validResponse.isSuccessful()) {
				return validResponse;
			}
		}

		//校验成功，开始注册
		user.setRole(Const.Role.ROLE_CUSTOMER);
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
		int rowCount = userMapper.insert(user);
		return rowCount == 1 ? ServerResponse.createBySuccessMsg("注册成功")
								: ServerResponse.createByErrorMsg("注册失败");
	}

	@Override
	public ServerResponse<String> getForgetQuestion(String username) {
		ServerResponse validResponse = checkValid(username, Const.USERNAME);
		//说明数据库中没有该用户名
		if (validResponse.isSuccessful()) {
			return ServerResponse.createByErrorMsg("用户不存在");
		}
		String question = userMapper.selectQuestionByUsername(username);
		return question != null ? ServerResponse.createBySuccessData(question)
								: ServerResponse.createByErrorMsg("该用户未设置找回密码问题");
	}

	@Override
	public ServerResponse<String> checkForgetAnswer(String username, String question, String answer) {
		int rowCount = userMapper.checkForgetAnswer(username, question, answer);
		if (rowCount == 1) {
			String key = TokenCache.TOKEN_PREFIX + username;
			String token = UUID.randomUUID().toString();
			//以TOKEN_PREFIX + username作为缓存的key，token作为value
			TokenCache.setKey(key, token);
			return ServerResponse.createBySuccessData(token);
		} else {
			return ServerResponse.createByErrorMsg("问题答案错误");
		}
	}

	@Override
	public ServerResponse resetForgetPassword(String username, String passwordNew, String token) {
		if (checkValid(username, Const.USERNAME).isSuccessful()) {
			//用户名不存在
			return ServerResponse.createByErrorMsg("用户名不存在");
		}
		if (StringUtils.isBlank(token)) {
			return ServerResponse.createByErrorMsg("该请求缺少token参数");
		}
		if (StringUtils.isBlank(passwordNew)) {
			return ServerResponse.createByErrorMsg("密码不能为空");
		}

		String key = TokenCache.TOKEN_PREFIX + username;
		String realToken = TokenCache.getKey(key);
		ServerResponse response;
		if (!StringUtils.equals(token, realToken)) {
			response =  ServerResponse.createByErrorMsg("token已失效");
		} else {
			User user = new User();
			user.setUsername(username);
			user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
			int rowResult = userMapper.updateByUserName(user);
			if (rowResult == 1) {
				response = ServerResponse.createBySuccessMsg("修改密码成功");
			} else {
				response = ServerResponse.createByErrorMsg("修该密码失败");
			}
		}
		return response;
	}

	@Override
	public ServerResponse resetPassword(String passwordOld, String passwordNew, User user) {
		//重置密码时，为防止横向越权，需要先校验旧密码是否正确。
		if (userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId()) == 0) {
			return ServerResponse.createByErrorMsg("旧密码输入错误");
		}

		user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
		if (userMapper.updateByPrimaryKeySelective(user) == 1) {
			return ServerResponse.createBySuccessMsg("修改密码成功");
		}
		return ServerResponse.createByErrorMsg("修改密码失败");
	}

	@Override
	public ServerResponse<User> updateInformation(User user) {
		//检查更新的email是否已存在
		int rowCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
		if (rowCount > 0) {
			return ServerResponse.createByErrorMsg("该邮箱已被注册");
		}
		rowCount = userMapper.updateByPrimaryKeySelective(user);
		if (rowCount == 1) {
			return ServerResponse.createBySuccess("更新个人信息成功", user);
		}
		return ServerResponse.createByErrorMsg("更新个人信息失败");
	}

	@Override
	public Boolean checkUserRole(User user, Integer roleCode) {
		return user.getRole() == roleCode;
	}
}
