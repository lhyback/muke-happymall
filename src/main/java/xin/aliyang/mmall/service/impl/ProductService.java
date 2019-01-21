package xin.aliyang.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.aliyang.mmall.common.Const;
import xin.aliyang.mmall.common.ResponseCode;
import xin.aliyang.mmall.common.ServerResponse;
import xin.aliyang.mmall.dao.CategoryMapper;
import xin.aliyang.mmall.dao.ProductMapper;
import xin.aliyang.mmall.pojo.Category;
import xin.aliyang.mmall.pojo.Product;
import xin.aliyang.mmall.service.ICategoryService;
import xin.aliyang.mmall.service.IProductService;
import xin.aliyang.mmall.vo.ProductListVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhy on 2019/1/13.
 */
@Service("/productService")
public class ProductService implements IProductService {

	@Autowired
	ProductMapper productMapper;

	@Autowired
	CategoryMapper categoryMapper;

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
		List<ProductListVO> productListVOList = new ArrayList<>();
		for (Product product : list) {
			productListVOList.add(assembleProductListVO(product));
		}
		return ServerResponse.createBySuccessData(new PageInfo(productListVOList));
	}

	@Override
	public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Product> productList = productMapper.selectConditionalProduct(productName, productId);
		PageInfo pageInfo = new PageInfo(productList);
		return ServerResponse.createBySuccessData(pageInfo);
	}

	@Override
	public ServerResponse<PageInfo> searchProductByCategory(Integer categoryId, String keyword, String orderBy, Integer pageNum, Integer pageSize) {
		//参数处理
		if (categoryId == null) {
			return ServerResponse.createByErrorCodeMsg(
					ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}

		if (Const.SORT_SET.contains(orderBy)) {
			String[] order = orderBy.split("_");
			orderBy = order[0] + " " + order[1];
		} else {
			orderBy = null;
		}

		if (StringUtils.isNotBlank(keyword)) {
			keyword = keyword.trim();
		} else {
			keyword = null;
		}

		//递归查询所有子分类
		List<Integer> categoryIds = new ArrayList<>();
		List<Category> categoryList = categoryMapper.selectCategoryByParentId(categoryId);
		for (Category category : categoryList) {
			categoryIds.add(category.getId());
		}
		categoryIds.add(categoryId);

		PageHelper.startPage(pageNum, pageSize);
		PageHelper.orderBy(orderBy); //设置排序
		List<Product> productList = productMapper.selectByNameAndCategoryIds(keyword, categoryIds);
		List<ProductListVO> productListVOList = new ArrayList<>();
		for (Product product : productList) {
			if (!product.getStatus().equals(Const.ProductSaleStatus.ON_SALE.getCode())) {
				continue;
			}
			productListVOList.add(assembleProductListVO(product));
		}
		PageInfo pageInfo = new PageInfo(productListVOList);
		return ServerResponse.createBySuccessData(pageInfo);
	}


	private ProductListVO assembleProductListVO(Product product) {
		ProductListVO productListVO = new ProductListVO();
		//commom field
		productListVO.setId(product.getId());
		productListVO.setCategoryId(product.getCategoryId());
		productListVO.setName(product.getName());
		productListVO.setSubtitle(product.getSubtitle());
		productListVO.setMainImage(product.getMainImage());
		productListVO.setPrice(product.getPrice());
		productListVO.setStatus(product.getStatus());

		return productListVO;
	}
}
