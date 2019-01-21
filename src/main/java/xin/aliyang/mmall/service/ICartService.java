package xin.aliyang.mmall.service;

import xin.aliyang.mmall.common.ServerResponse;
import xin.aliyang.mmall.vo.CartVO;

/**
 * Created by lhy on 2019/1/21.
 */
public interface ICartService {
	ServerResponse<CartVO> addProduct(Integer userId, Integer productId, Integer count);

	ServerResponse<CartVO> updateCartItem(Integer userId, Integer productId, Integer count);

	ServerResponse<CartVO> deleteCartItem(Integer userId, String productIds);

	ServerResponse<CartVO> listCartItem(Integer userId);

	ServerResponse<CartVO> selectOrUnselectCartItem(Integer userId, Integer productId, Integer checked);

	ServerResponse<Integer> getCartProductCount(Integer userId);
}
