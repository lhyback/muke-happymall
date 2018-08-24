package xin.aliyang.mmall.dao;

import org.apache.ibatis.annotations.Param;
import xin.aliyang.mmall.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    User selectUserByUsernameAndPwd(@Param("username") String username, @Param("password") String password);

	int checkEmail(String email);

	String selectQuestionByUsername(String username);

	int checkForgetAnswer(@Param("username") String username,@Param("question") String question,@Param("answer") String answer);

	int updateByUserName(User user);
}