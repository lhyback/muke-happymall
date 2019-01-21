package xin.aliyang.mmall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.aliyang.mmall.common.ResponseCode;
import xin.aliyang.mmall.common.ServerResponse;
import xin.aliyang.mmall.dao.ShippingMapper;
import xin.aliyang.mmall.pojo.Shipping;
import xin.aliyang.mmall.service.IShippingService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lhy on 2019/1/21.
 */
@Service("shippingService")
public class ShippingService implements IShippingService {
	@Autowired
	ShippingMapper shippingMapper;

	@Override
	public ServerResponse addShipping(Integer userId, Shipping shipping) {
		if (userId == null) {
			return ServerResponse.createByErrorCodeMsg(
					ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		shipping.setUserId(userId);
		int row = shippingMapper.insert(shipping);
		if (row > 0) {
			Map<String, Integer> resultMap = new HashMap<>();
			resultMap.put("shippingId", shipping.getId());  //insert后自动填充到参数对象里
			return ServerResponse.createBySuccess("新建地址成功", resultMap);
		}
		return ServerResponse.createByErrorMsg("新建地址失败");
	}
}
