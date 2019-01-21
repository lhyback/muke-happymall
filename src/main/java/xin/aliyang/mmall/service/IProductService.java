package xin.aliyang.mmall.service;

import com.github.pagehelper.PageInfo;
import xin.aliyang.mmall.common.ServerResponse;
import xin.aliyang.mmall.pojo.Product;

import java.util.List;

/**
 * Created by lhy on 2019/1/13.
 */
public interface IProductService {
	ServerResponse saveProduct(Product product);

	ServerResponse setSaleStatus(Integer productId, Integer status);

	ServerResponse<Product> getProductDetail(Integer productId);

	ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize);

	ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize);

	ServerResponse<PageInfo> searchProductByCategory(Integer categoryId,String keyword, String orderBy,
													 Integer pageNum,Integer pageSize);
}
