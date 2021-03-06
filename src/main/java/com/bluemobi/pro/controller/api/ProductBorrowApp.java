package com.bluemobi.pro.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluemobi.constant.ErrorCode;
import com.bluemobi.pro.entity.ProductBorrow;
import com.bluemobi.pro.entity.ProductBorrowRepayRecord;
import com.bluemobi.pro.service.impl.ProductBorrowService;
import com.bluemobi.utils.Result;

/**
 * 
 * @ClassName: ProductBorrowController
 * @Description: 商品贷款controller
 * @author yesong
 * @date 2015年12月11日
 *
 */
@RequestMapping("/app/pb/")
@Controller
public class ProductBorrowApp {

	@Autowired
	private ProductBorrowService service;
	
	/**
	 * 
     * @Title: findByUserId
     * @Description: 查询自己商品贷款记录
     * @param @param pb
     * @param @return    参数
     * @return Result    返回类型
    * @throws
	 */
	@RequestMapping(value = "list", method = RequestMethod.POST)
	@ResponseBody
	public Result findByUserId(ProductBorrow pb) {
		
		List<ProductBorrow> pbList = null;
		try {
			pbList = service.findBorrowByUserId(pb);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.failure();
		}
		return Result.success(pbList);
	}
	
	/**
	 * 
     * @Title: findById
     * @Description: 购物贷款详情
     * @param @param pb
     * @param @return    参数
     * @return Result    返回类型
     * @throws
	 */
	@RequestMapping(value = "detail", method = RequestMethod.POST)
	@ResponseBody
	public Result findById(ProductBorrow pb) {
		
		ProductBorrow borrow = null;
		try {
			borrow = service.findBorrowById(pb);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.failure();
		}
		return Result.success(borrow);
	}
	/**
	 * 
     * @Title: repay
     * @Description: 还款
     * @param @param pbrr
     * @param @return    参数
     * @return Result    返回类型
     * @throws
	 */
	@RequestMapping(value = "repay", method = RequestMethod.POST)
	@ResponseBody
	public Result repay(ProductBorrowRepayRecord pbrr) {
		
		try {
			int flag = service.repay(pbrr);
			if(flag == -1) {
				return Result.failure(ErrorCode.ERROR_20);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.failure();
		}
		return Result.success();
	}
}
