package xin.aliyang.mmall.service;

import org.springframework.stereotype.Service;
import xin.aliyang.mmall.common.ServerResponse;

/**
 * Created by lhy on 2019/1/7.
 */
public interface ICategoryService {
	ServerResponse addCategory(String categoryName, Integer partnerId);
}
