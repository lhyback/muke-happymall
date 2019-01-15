package xin.aliyang.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.aliyang.mmall.common.ResponseCode;
import xin.aliyang.mmall.common.ServerResponse;
import xin.aliyang.mmall.dao.ProductMapper;
import xin.aliyang.mmall.pojo.Product;
import xin.aliyang.mmall.service.IProductService;

import java.util.List;

/**
 * Created by lhy on 2019/1/13.
 */
@Service("/productService")
public class ProductService implements IProductService {
	@Autowired
	ProductMapper productMapper;

	@Override
	public ServerResponse saveProduct(Product product) {
		if (product.getId() == null) {
			//新增产品
			if (product.getCategoryId() == null || product.getName() == null
					|| product.getPrice() == null || product.getStock() == null) {
				return ServerResponse.createByErrorMsg(ResponseCode.ILLEGAL_ARGUMENT.getDesc());
			}
			int row = productMapper.insertSelective(product);
			if (row > 0) {
				return ServerResponse.createByErrorMsg("新增产品成功");
			}

		} else {
			//更新产品
			int row = productMapper.updateByPrimaryKeySelective(product);
			if (row > 0) {
				return ServerResponse.createBySuccessMsg("更新产品成功");
			}
		}
		return ServerResponse.createByErrorMsg("保存产品信息失败");
	}

	@Override
	public ServerResponse setSaleStatus(Integer productId, Integer status) {
		Product product = productMapper.selectByPrimaryKey(productId);
		if (product == null) {
			return ServerResponse.createByErrorMsg("修改失败，无该产品");
		}
		product.setStatus(status);
		int row = productMapper.updateByPrimaryKeySelective(product);
		if (row > 0) {
			return ServerResponse.createBySuccessMsg("修改产品状态成功");
		} else {
			return ServerResponse.createByErrorMsg("修改产品状态失败");
		}
	}

	@Override
	public ServerResponse<Product> getProductDetail(Integer productId) {
		Product product = productMapper.selectByPrimaryKey(productId);
		if (product == null) {
			return ServerResponse.createByErrorMsg("查询失败，无该产品");
		}
		return ServerResponse.createBySuccessData(product);
	}

	@Override
	public ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize) {
		//1. startPage
		PageHelper.startPage(pageNum, pageSize);
		//2. sql operations
		List<Product> list = productMapper.selectAllProduct();
		//3. 通过new PageInfo(data)自动填入分页信息
		return ServerResponse.createBySuccessData(new PageInfo(list));
	}

	@Override
	public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Product> productList = productMapper.selectConditionalProduct(productName, productId);
		PageInfo pageInfo = new PageInfo(productList);
		return ServerResponse.createBySuccessData(pageInfo);
	}
}
