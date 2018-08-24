package xin.aliyang.mmall.dao.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xin.aliyang.mmall.dao.UserMapper;
import xin.aliyang.mmall.pojo.User;

/**
 * Created by lhy on 2018/8/15.
 */

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:applicationContext.xml")
public class TestUserMapper {
	@Autowired
	UserMapper userMapper;
	//@Test
	public void testSelectUserByNameAndPwd() {
		User user = userMapper.selectUserByUsernameAndPwd("lhy", "lhy");
		System.out.println(user);
	}

	//@Test
	public void testCheckEmail() {
		int count = userMapper.checkEmail("601114527@qq.com");
		System.out.println(count);
	}

	//@Test
	public void testUpdateUserByUserName() {
		User user = new User();
		user.setUsername("lhy");
		user.setPassword("199556");
		userMapper.updateByUserName(user);
	}


}
