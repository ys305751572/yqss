package com.bluemobi.pro.controller.api;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluemobi.constant.ErrorCode;
import com.bluemobi.pay.excute.PayRequest;
import com.bluemobi.pro.entity.BorrowRepayRecord;
import com.bluemobi.pro.entity.ProductBorrowRepayRecord;
import com.bluemobi.pro.service.impl.BorrowService;
import com.bluemobi.pro.service.impl.ProductBorrowService;
import com.bluemobi.pro.service.impl.XxShopServiceImpl;
import com.bluemobi.utils.ParamUtils;
import com.bluemobi.utils.ResultUtils;
import com.bluemobi.utils.YqssUtils;

/**
 * 
 * @ClassName: PayApp
 * @Description: 支付
 * @author yesong
 * @date 2016年1月5日
 *
 */
@Controller
@RequestMapping("/app/pay/")
public class PayApp {

	@Autowired
	private BorrowService borrowService;

	@Autowired
	private ProductBorrowService pbService;

	@Autowired
	private XxShopServiceImpl iShopServiceImpl;

	@RequestMapping(value = "weixin/borrow", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> wenxinBorrowPay(BorrowRepayRecord repay, HttpServletRequest request,
			HttpServletResponse resp) {

		try {
			if (!borrowService.isThisMonth(repay)) {
				int totelFee = (int) (repay.getAmount() * 100);
				String sn = YqssUtils.generateSn();
				String prepayid = null;
				repay.setAmount(0.0);
				repay.setSn(sn);
				borrowService.repay(repay);

				 request.setAttribute("fee", totelFee);
				 request.setAttribute("sn", sn);
				 request.setAttribute("prepayid", prepayid);
				 Map<String,Object> resultMap = PayRequest.pay(1,request,
				 resp);
				return ResultUtils.map2(resultMap);
			} else {
				return ResultUtils.error(ErrorCode.ERROR_20);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "alipay/borrow", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> alipayBorrowPay(BorrowRepayRecord repay) {

		String sn = YqssUtils.generateSn();
		try {
			if (!borrowService.isThisMonth(repay)) {

				repay.setAmount(0.0);
				repay.setSn(sn);
				borrowService.repay(repay);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("sn", sn);
				return ResultUtils.map2(map);
			}
			else {
				return ResultUtils.error(ErrorCode.ERROR_20);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
	}

	@RequestMapping(value = "weixin/productBorrow", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> weixinProductBorrowPay(ProductBorrowRepayRecord pbrr, HttpServletRequest request,
			HttpServletResponse resp) {

		if (pbService.isThisMonth(pbrr)) {
			return ResultUtils.error(ErrorCode.ERROR_20);
		}

		int fee = (int) (pbrr.getAmount() * 100);
		String sn = YqssUtils.generateSn();
		try {
			pbrr.setAmount(0.0);
			pbrr.setSn(sn);
			pbService.repay(pbrr);

			request.setAttribute("fee", fee);
			request.setAttribute("sn", sn);
			Map<String, Object> resultMap = PayRequest.pay(2, request, resp);
			return ResultUtils.map2(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "alipay/productBorrow", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> alipayProductBorrowPay(ProductBorrowRepayRecord pbrr) {

		if (pbService.isThisMonth(pbrr)) {
			return ResultUtils.error(ErrorCode.ERROR_20);
		}

		String sn = YqssUtils.generateSn();

		pbrr.setAmount(0.0);
		pbrr.setSn(sn);
		try {
			pbService.repay(pbrr);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sn", sn);
			return ResultUtils.map2(map);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.error();
		}
	}

	// 获取预支付ID
	@RequestMapping(value = "weixin/product", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> payWeixin(@RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) {

		if (ParamUtils.existEmpty(params, "sn"))
			return ResultUtils.error(ErrorCode.ERROR_02);
		Map<String, Object> countAndprepayidMap = null;
		Double fee = 1.0;
		String prepayid = null;
		// try {
		// countAndprepayidMap =
		// iShopServiceImpl.countPrice(params.get("sn").toString());
		// fee = countAndprepayidMap.get("price") != null ?
		// Double.parseDouble(countAndprepayidMap.get("price").toString()) :
		// 0.0;
		// prepayid = countAndprepayidMap.get("prepayid") != null ?
		// countAndprepayidMap.get("prepayid").toString() : null;
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		String sn = params.get("sn").toString();
		request.setAttribute("sn", sn);
		String feeStr = (fee != null && fee.doubleValue() != 0.0
				? String.valueOf(new DecimalFormat("0").format(fee * 100)) : "1");
		request.setAttribute("fee", Integer.parseInt(feeStr)); // 正确价格
		request.setAttribute("prepayid", prepayid);
		Map<String, Object> resultMap = PayRequest.pay(3, request, response);

		prepayid = resultMap.get("prepayid").toString();
		params.put("prepayid", prepayid);
		try {
			iShopServiceImpl.modifyPrepayid(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultUtils.map2(resultMap);
	}
}
