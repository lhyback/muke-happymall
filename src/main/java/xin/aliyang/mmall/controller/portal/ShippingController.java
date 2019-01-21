package xin.aliyang.mmall.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xin.aliyang.mmall.common.Const;
import xin.aliyang.mmall.common.ResponseCode;
import xin.aliyang.mmall.common.ServerResponse;
import xin.aliyang.mmall.pojo.Shipping;
import xin.aliyang.mmall.pojo.User;
import xin.aliyang.mmall.service.IShippingService;

import javax.servlet.http.HttpSession;

/**
 * Created by lhy on 2019/1/21.
 */
@Controller
@RequestMapping("/shipping")
public class ShippingController {
	@Autowired
	IShippingService shippingService;

	@RequestMapping("/add.do")
	@ResponseBody
	public ServerResponse addShipping(Shipping shipping, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return  ServerResponse.createByErrorCodeMsg(
					ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}

		//userId 是服务器自己获取，不需要前端传
		return shippingService.addShipping(user.getId(), shipping);
	}

}
