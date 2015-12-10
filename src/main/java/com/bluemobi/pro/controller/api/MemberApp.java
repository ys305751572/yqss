package com.bluemobi.pro.controller.api;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluemobi.cache.CacheService;
import com.bluemobi.constant.ErrorCode;
import com.bluemobi.pro.entity.RegisterUser;
import com.bluemobi.pro.entity.UserInfo;
import com.bluemobi.pro.entity.UserLogin;
import com.bluemobi.pro.service.impl.UserService;
import com.bluemobi.utils.Result;

/**
 * 
 * @ClassName: MemberApp
 * @Description: 用户controller
 * @author yesong
 * @date 2015年12月10日
 *
 */
@Controller
@RequestMapping("/app/member/manager/")
public class MemberApp {

	@Autowired
	private UserService service;

	@Resource(name = "cacheTempCodeServiceImpl")
	private CacheService<String> cacheService;

	@RequestMapping(value = "modifyInfo", method = RequestMethod.POST)
	@ResponseBody
	public Result modifyMemberInfo(UserInfo userInfo) {

		try {
			service.modifyUser(userInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.failure();
		}
		return Result.success();
	}

	@RequestMapping(value = "forget", method = RequestMethod.POST)
	@ResponseBody
	public Result forget(RegisterUser user) {

		try {
			String requestCode = user.getCode();
			String code = cacheService.get(user.getUsername());

			UserLogin userLogin = service.findUserByMobile(user);
			if (userLogin == null)
				return Result.failure(ErrorCode.ERROR_06);
			if (StringUtils.isBlank(requestCode) || !requestCode.equals(code)) {
				return Result.failure(ErrorCode.ERROR_10);
			}
			
			userLogin.setPassword(user.getPassword());
			service.modifyUserLoginPassword(user);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.failure();
		}
		return Result.success();
	}
}
