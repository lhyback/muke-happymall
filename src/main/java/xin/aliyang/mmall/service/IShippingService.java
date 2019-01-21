package xin.aliyang.mmall.service;

import xin.aliyang.mmall.common.ServerResponse;
import xin.aliyang.mmall.pojo.Shipping;

/**
 * Created by lhy on 2019/1/21.
 */
public interface IShippingService {
	ServerResponse addShipping(Integer userId, Shipping shipping);
}
