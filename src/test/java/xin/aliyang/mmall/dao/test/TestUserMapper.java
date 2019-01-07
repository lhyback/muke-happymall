package xin.aliyang.mmall.dao.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xin.aliyang.mmall.dao.UserMapper;
import xin.aliyang.mmall.pojo.User;
import xin.aliyang.mmall.util.MD5Util;

/**
 * Created by lhy on 2018/8/15.
 */

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:applicationContext-datasource.xml")
public class TestUserMapper {
	@Autowired
	UserMapper userMapper;
	//@Test
	public void testSelectUserByNameAndPwd() {
		User user = userMapper.selectUserByUsernameAndPwd("lhy", MD5Util.MD5EncodeUtf8("199556"));
		System.out.println(user);
	}

	//@Test
	public void testCheckEmail() {
		int count = userMapper.checkEmail("601114527@qq.com");
		System.out.println(count);
	}

	//@Test
	public void testUpdateUserByUserName() {
		User user = userMapper.selectByPrimaryKey(22);
		System.out.println(user);
		user.setUsername("lhy");
		user.setPassword(MD5Util.MD5EncodeUtf8("19950506"));
		userMapper.updateByPrimaryKeySelective(user);
		//userMapper.updateByUserName(user);
		System.out.println(userMapper.selectByPrimaryKey(22));
	}


}
