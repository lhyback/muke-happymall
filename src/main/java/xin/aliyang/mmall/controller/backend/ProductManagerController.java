package xin.aliyang.mmall.controller.backend;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import net.sf.jsqlparser.schema.Server;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import xin.aliyang.mmall.common.Const;
import xin.aliyang.mmall.common.ResponseCode;
import xin.aliyang.mmall.common.ServerResponse;
import xin.aliyang.mmall.pojo.Category;
import xin.aliyang.mmall.pojo.Product;
import xin.aliyang.mmall.pojo.User;
import xin.aliyang.mmall.service.ICategoryService;
import xin.aliyang.mmall.service.IFileService;
import xin.aliyang.mmall.service.IProductService;
import xin.aliyang.mmall.service.IUserService;
import xin.aliyang.mmall.util.DateUtil;
import xin.aliyang.mmall.util.PropertiesUtil;
import xin.aliyang.mmall.vo.ProductDetailVO;
import xin.aliyang.mmall.vo.ProductListVO;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * Created by lhy on 2019/1/13.
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManagerController {

	private static Logger logger = LoggerFactory.getLogger(ProductManagerController.class);
	@Autowired
	private IUserService userService;

	@Autowired
	private IProductService productService;

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private IFileService fileService;

	@RequestMapping("/save.do")
	@ResponseBody
	public ServerResponse saveProduct(Product product, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
		}
		if (!userService.checkUserRole(user, Const.Role.ROLE_ADMIN)) {
			return ServerResponse.createByErrorMsg("不是admin用户，无权限");
		}

		if (product == null) {
			return ServerResponse.createByErrorCodeMsg(
					ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}

		return productService.saveProduct(product);
	}

	@RequestMapping("/set_sale_status.do")
	@ResponseBody
	public ServerResponse setSaleStatus(Integer productId, Integer status, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
		}
		if (!userService.checkUserRole(user, Const.Role.ROLE_ADMIN)) {
			return ServerResponse.createByErrorMsg("不是admin用户，无权限");
		}

		if (productId == null || status == null) {
			return ServerResponse.createByErrorCodeMsg(
					ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}

		return productService.setSaleStatus(productId, status);
	}

	@RequestMapping("/detail.do")
	@ResponseBody
	public ServerResponse<ProductDetailVO> getProductDetail(Integer productId, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
		}
		if (!userService.checkUserRole(user, Const.Role.ROLE_ADMIN)) {
			return ServerResponse.createByErrorMsg("不是admin用户，无权限");
		}

		if (productId == null) {
			return ServerResponse.createByErrorCodeMsg(
					ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}

		ServerResponse<Product> response = productService.getProductDetail(productId);
		if (!response.isSuccessful()) {
			return ServerResponse.createByErrorMsg("查询产品详情失败");
		}
		Product product = response.getData();
		return ServerResponse.createBySuccessData(assembleProductDetailVO(product));

	}

	@RequestMapping("/list.do")
	@ResponseBody
	public ServerResponse<PageInfo> getProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
												   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
												   HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
		}
		if (!userService.checkUserRole(user, Const.Role.ROLE_ADMIN)) {
			return ServerResponse.createByErrorMsg("不是admin用户，无权限");
		}

		ServerResponse response = productService.getProductList(pageNum, pageSize);
		//PageInfo<T> 表示内部含有一个list<T>
		PageInfo pageInfo = (PageInfo) response.getData();
		List<Product> productList = pageInfo.getList();
		List<ProductListVO> list = new ArrayList<>();
		for (Product product : productList) {
			ProductListVO productListVO = assembleProductListVO(product);
			list.add(productListVO);
		}
		//这里不能重新new一个pageInfo，因为分页相关信息在service层自动填充了
		pageInfo.setList(list);
		return ServerResponse.createBySuccessData(pageInfo);
	}

	@RequestMapping("/search.do")
	@ResponseBody
	public ServerResponse searchProduct(String productName,
										Integer productId,
										@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
										@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
										HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
		}
		if (!userService.checkUserRole(user, Const.Role.ROLE_ADMIN)) {
			return ServerResponse.createByErrorMsg("不是admin用户，无权限");
		}

		ServerResponse response = productService.searchProduct(productName, productId, pageNum, pageSize);
		PageInfo pageInfo = (PageInfo) response.getData();
		List<Product> productList = pageInfo.getList();
		List<ProductListVO> list = new ArrayList<>();
		for (Product product : productList) {
			ProductListVO productListVO = assembleProductListVO(product);
			list.add(productListVO);
		}

		pageInfo.setList(list);
		return ServerResponse.createBySuccessData(list);
	}


	/**
	 * 上传文件到ftp server
	 * @param file
	 * @param session
	 * @return
	 */
	@RequestMapping("/upload.do")
	@ResponseBody
	public ServerResponse uploadFile(@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
		}
		if (!userService.checkUserRole(user, Const.Role.ROLE_ADMIN)) {
			return ServerResponse.createByErrorMsg("不是admin用户，无权限");
		}

		//webapp : /Users/lhy/project/java-project/muke-geelyssm/mmall/target/mmall
		//tmpPath : ${webapp}/upload
		//remotePah : ftp工作子目录
		String tmpPath = session.getServletContext().getRealPath("upload");
		String remotePath = "img";
		String uploadFileName = null;
		try {
			uploadFileName = fileService.uploadToFtpServer(file, tmpPath, remotePath);
			String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + remotePath + "/" + uploadFileName;
			Map<String, String> map = new HashMap<>();
			map.put("uri", uploadFileName);
			map.put("url", url);

			return ServerResponse.createBySuccessData(map);
		} catch (IOException e) {
			logger.error("上传文件失败", e);
		}
		return ServerResponse.createByErrorMsg("上传文件失败");
	}


	/**
	 * 上传富文本到ftp server
	 * @param file
	 * @param session
	 * @return 返回值由simditor（浏览器端的富文本编辑器）标准确定, 这里返回一个map
	 */
	@RequestMapping("/richtext_img_upload.do")
	@ResponseBody
	public Map uploadRichText(@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			//return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
		}
		if (!userService.checkUserRole(user, Const.Role.ROLE_ADMIN)) {
			//return ServerResponse.createByErrorMsg("不是admin用户，无权限");
		}
		//TODO
		return null;
	}



/*
	private tool method
 */

	private ProductDetailVO assembleProductDetailVO(Product product) {
		ProductDetailVO productDetailVO = new ProductDetailVO();
		//commom field
		productDetailVO.setId(product.getId());
		productDetailVO.setName(product.getName());
		productDetailVO.setSubtitle(product.getSubtitle());
		productDetailVO.setMainImage(product.getMainImage());
		productDetailVO.setSubImages(product.getSubImages());
		productDetailVO.setDetail(product.getDetail());
		productDetailVO.setPrice(product.getPrice());
		productDetailVO.setStock(product.getStock());
		productDetailVO.setStatus(product.getStatus());

		//particular field
		Integer categoryId = product.getCategoryId();
		Category parentCategory = categoryService.getParentCategory(categoryId);
		String imageHost = PropertiesUtil.getProperty("ftp.server.http.prefix", "http://image.aliyang.xin/");
		if (parentCategory != null) {
			productDetailVO.setParentCategoryId(parentCategory.getId());
		}
		productDetailVO.setImageHost(imageHost);

		//Date format
		productDetailVO.setCreateTime(DateUtil.dateToStr(product.getCreateTime()));
		productDetailVO.setUpdateTime(DateUtil.dateToStr(product.getUpdateTime()));

		return productDetailVO;
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
