package xin.aliyang.mmall.dao;

import org.apache.ibatis.annotations.Param;
import xin.aliyang.mmall.pojo.Product;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectAllProduct();

	List<Product> selectConditionalProduct(@Param("productName") String productName, @Param("productId") Integer productId);

    List<Product> selectByNameAndCategoryIds(@Param("keyword") String keyword,
											 @Param("categoryIds") List<Integer> categoryIds);
}