package xin.aliyang.mmall.dao;

import org.apache.ibatis.annotations.Param;
import xin.aliyang.mmall.pojo.Cart;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    //有字段非空判断
    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

	List<Cart> selectCartByUserId(Integer userId);

	int countUnChecked(@Param("userId") Integer userId, @Param("uncheckedCode") Integer uncheckedCode);

	int deleteByUserIdAndProductIds(@Param("userId") Integer userId, @Param("productIdList") List<String> productIdList);

	int updateCheckedByUserId(@Param("userId") Integer userId, @Param("productId") Integer productId, @Param("checked") Integer checked);

	int selectCartProductCount(Integer userId);
}