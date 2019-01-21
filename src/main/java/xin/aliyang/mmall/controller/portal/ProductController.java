package xin.aliyang.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import net.sf.jsqlparser.schema.Server;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xin.aliyang.mmall.common.Const;
import xin.aliyang.mmall.common.ResponseCode;
import xin.aliyang.mmall.common.ServerResponse;
import xin.aliyang.mmall.pojo.Product;
import xin.aliyang.mmall.pojo.User;
import xin.aliyang.mmall.service.IProductService;
import xin.aliyang.mmall.service.IUserService;
import xin.aliyang.mmall.vo.ProductDetailVO;
import xin.aliyang.mmall.vo.ProductListVO;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by lhy on 2019/1/16.
 */
@Controller
@RequestMapping("/product")
public class ProductController {
	@Autowired
	IUserService userService;

	@Autowired
	IProductService productService;

	private static Logger logger = LoggerFactory.getLogger(ProductController.class);

	@RequestMapping("/detail.do")
	@ResponseBody
	public ServerResponse getProductDetail(Integer productId, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return  ServerResponse.createByErrorCodeMsg(
					ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}

		ServerResponse<Product> response = productService.getProductDetail(productId);
		if (response.isSuccessful()) {
			Product product = response.getData();
			logger.info("product : {} ", product);
			//在售状态，则返回给前台(注意比较时别用成Object.equals)
			if (Const.ProductSaleStatus.ON_SALE.getCode().equals(product.getStatus())) {
				return ServerResponse.createBySuccessData(product);
			}
		}

		return ServerResponse.createByErrorMsg("产品已下架或删除");
	}


	@RequestMapping("/list.do")
	@ResponseBody
	public ServerResponse searchProductByCategory(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
										@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
										@RequestParam(value = "orderBy", defaultValue = "") String orderBy,
										Integer categoryId,
										String keyword,
										HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return  ServerResponse.createByErrorCodeMsg(
					ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}

		return productService.searchProductByCategory(categoryId, keyword, orderBy, pageNum, pageSize);
	}
}
