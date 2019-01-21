package xin.aliyang.mmall.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xin.aliyang.mmall.common.Const;
import xin.aliyang.mmall.common.ResponseCode;
import xin.aliyang.mmall.common.ServerResponse;
import xin.aliyang.mmall.pojo.User;
import xin.aliyang.mmall.service.ICartService;

import javax.servlet.http.HttpSession;

/**
 * Created by lhy on 2019/1/21.
 */
@Controller
@RequestMapping("/cart")
public class CartController {
	@Autowired
	ICartService cartService;

	@RequestMapping("/add.do")
	@ResponseBody
	public ServerResponse addProduct(Integer count, Integer productId, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return  ServerResponse.createByErrorCodeMsg(
					ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}

		return cartService.addProduct(user.getId(), productId, count);
	}

	@RequestMapping("/update.do")
	@ResponseBody
	public ServerResponse updateCartItem(Integer count, Integer productId, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMsg(
					ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}
		return cartService.updateCartItem(user.getId(), productId, count);
	}

	@RequestMapping("/delete_product.do")
	@ResponseBody
	public ServerResponse deleteCartItem(String productIds, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return  ServerResponse.createByErrorCodeMsg(
					ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}

		return cartService.deleteCartItem(user.getId(), productIds);
	}

	@RequestMapping("/list.do")
	@ResponseBody
	public ServerResponse listCartItem(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return  ServerResponse.createByErrorCodeMsg(
					ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}

		return cartService.listCartItem(user.getId());
	}


	@RequestMapping("/select_all.do")
	@ResponseBody
	public ServerResponse selectAllCartItem(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return  ServerResponse.createByErrorCodeMsg(
					ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}

		return cartService.selectOrUnselectCartItem(user.getId(), null, Const.Cart.CHECKED);
	}

	@RequestMapping("/un_select_all.do")
	@ResponseBody
	public ServerResponse unselectAllCartItem(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return  ServerResponse.createByErrorCodeMsg(
					ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}

		return cartService.selectOrUnselectCartItem(user.getId(), null, Const.Cart.UN_CHECKED);
	}

	@RequestMapping("/select.do")
	@ResponseBody
	public ServerResponse selectCartItem(Integer productId, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return  ServerResponse.createByErrorCodeMsg(
					ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}

		return cartService.selectOrUnselectCartItem(user.getId(), productId, Const.Cart.CHECKED);
	}

	@RequestMapping("/un_select.do")
	@ResponseBody
	public ServerResponse unselectCartItem(Integer productId, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return  ServerResponse.createByErrorCodeMsg(
					ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}

		return cartService.selectOrUnselectCartItem(user.getId(), productId, Const.Cart.UN_CHECKED);
	}

	@RequestMapping("/get_cart_product_count.do")
	@ResponseBody
	public ServerResponse getCartProductCount(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return  ServerResponse.createByErrorCodeMsg(
					ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}

		return cartService.getCartProductCount(user.getId());
	}
}
