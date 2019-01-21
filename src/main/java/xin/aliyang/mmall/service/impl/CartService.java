package xin.aliyang.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xin.aliyang.mmall.common.Const;
import xin.aliyang.mmall.common.ResponseCode;
import xin.aliyang.mmall.common.ServerResponse;
import xin.aliyang.mmall.dao.CartMapper;
import xin.aliyang.mmall.dao.ProductMapper;
import xin.aliyang.mmall.pojo.Cart;
import xin.aliyang.mmall.pojo.Product;
import xin.aliyang.mmall.service.ICartService;
import xin.aliyang.mmall.util.BigDecimalUtil;
import xin.aliyang.mmall.util.PropertiesUtil;
import xin.aliyang.mmall.vo.CartProductVO;
import xin.aliyang.mmall.vo.CartVO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhy on 2019/1/21.
 */
@Service
public class CartService implements ICartService {
	@Autowired
	CartMapper cartMapper;

	@Autowired
	ProductMapper productMapper;

	@Override
	public ServerResponse<CartVO> listCartItem(Integer userId) {
		CartVO cartVO = getCartVOWithStockLimit(userId);
		return ServerResponse.createBySuccessData(cartVO);
	}

	@Override
	public ServerResponse<CartVO> addProduct(Integer userId, Integer productId, Integer count) {
		if (productId == null || count == null) {
			return ServerResponse.createByErrorCodeMsg(
					ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}

		Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
		if (cart == null) {
			//新增: 这个产品不在这个购物车里，需要新增一条相关的记录
			Cart cartItem = new Cart();
			cartItem.setQuantity(count);
			cartItem.setChecked(Const.Cart.CHECKED);
			cartItem.setUserId(userId);
			cartItem.setProductId(productId);
			cartMapper.insertSelective(cartItem);
		} else {
			//更新数量
			cart.setQuantity(cart.getQuantity() + count);
			cartMapper.updateByPrimaryKeySelective(cart);
		}

		return listCartItem(userId);
	}

	@Override
	public ServerResponse<CartVO> updateCartItem(Integer userId, Integer productId, Integer count) {
		if (productId == null || count == null) {
			return ServerResponse.createByErrorCodeMsg(
					ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}

		Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
		if (cart == null) {
			return ServerResponse.createByErrorMsg("购物车中无该商品");
		}
		cart.setQuantity(count);
		cartMapper.updateByPrimaryKeySelective(cart);

		return listCartItem(userId);
	}

	@Override
	public ServerResponse<CartVO> deleteCartItem(Integer userId, String productIds) {
//		String[] productIdArray = productIds.split(",");
//		List<String> productIdList = Lists.newArrayList(productIdArray);
		List<String> productIdList = Splitter.on(",").splitToList(productIds);
		if (CollectionUtils.isEmpty(productIdList)) {
			return ServerResponse.createByErrorCodeMsg(
					ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}

		//可以考虑遍历productIdList然后逐项删除产品，也可以用mybatis的foreach标签
		cartMapper.deleteByUserIdAndProductIds(userId, productIdList);

		return listCartItem(userId);
	}

	@Override
	public ServerResponse<CartVO> selectOrUnselectCartItem(Integer userId, Integer productId, Integer checked) {
		if (userId == null) {
			return ServerResponse.createByErrorCodeMsg(
					ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}

		cartMapper.updateCheckedByUserId(userId, productId, checked);

		return listCartItem(userId);
	}

	@Override
	public ServerResponse<Integer> getCartProductCount(Integer userId) {
		if (userId == null) {
			return ServerResponse.createByErrorCodeMsg(
					ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}

		Integer count = cartMapper.selectCartProductCount(userId);
		return ServerResponse.createBySuccessData(count);
	}

	/**
	 * 获取cartVO的核心方法
	 * @param userId
	 * @return
	 */
	private CartVO getCartVOWithStockLimit(Integer userId) {
		CartVO cartVO = new CartVO();
		List<Cart> cartList = cartMapper.selectCartByUserId(userId);
		List<CartProductVO> cartProductVOList = new ArrayList<>();
		BigDecimal cartTotalPrice = new BigDecimal("0");  //String to construct

		for (Cart cartItem : cartList) {
			CartProductVO cartProductVO = new CartProductVO();
			cartProductVO.setId(cartItem.getId());
			cartProductVO.setChecked(cartItem.getChecked());
			cartProductVO.setUserId(cartItem.getUserId());
			cartProductVO.setProductId(cartItem.getProductId());
			cartProductVO.setChecked(cartItem.getChecked());

			Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
			if (product != null) {
				cartProductVO.setMainImage(product.getMainImage());
				cartProductVO.setSubtitle(product.getSubtitle());
				cartProductVO.setName(product.getName());
				cartProductVO.setPrice(product.getPrice());
				cartProductVO.setStatus(product.getStatus());
				cartProductVO.setStock(product.getStock());
			}
			//判断库存
			int buyCount = 0;
			if (product.getStock() >= cartItem.getQuantity()) {
				buyCount = cartItem.getQuantity();
				cartProductVO.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
			} else {
				buyCount = product.getStock();
				cartProductVO.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
				//库存不足时，更新cartItem的quantity（只能有product的stock那么大）
				Cart cartForQuantity = new Cart();
				cartForQuantity.setQuantity(buyCount);
				cartMapper.updateByPrimaryKeySelective(cartForQuantity);
			}

			cartProductVO.setQuantity(buyCount);
			BigDecimal productTotalPrice = BigDecimalUtil.multiply(cartProductVO.getPrice().doubleValue(), cartProductVO.getQuantity());
			cartProductVO.setTotalPrice(productTotalPrice);

			cartProductVOList.add(cartProductVO);
			//如果该cartItem已被勾选(checked == 1)，则将productTotalPrice计入购物车总价
			if (cartItem.getChecked() == Const.Cart.CHECKED) {
				cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVO.getTotalPrice().doubleValue());
			}
		}

		cartVO.setCartProductVOList(cartProductVOList);
		cartVO.setCartTotalPrice(cartTotalPrice);
		cartVO.setAllChecked(getAllCartItemChecked(userId));
		cartVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
		return cartVO;
	}


	private boolean getAllCartItemChecked(Integer userId) {
		if (userId == null) {
			return false;
		}
		Integer uncheckedCode = Const.Cart.UN_CHECKED;
		int count = cartMapper.countUnChecked(userId, uncheckedCode);
		return !(count > 0);
	}
}
