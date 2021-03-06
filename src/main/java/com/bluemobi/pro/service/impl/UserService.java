package com.bluemobi.pro.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.cli.CliParser.exitStatement_return;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.bluemobi.pro.entity.Image;
import com.bluemobi.pro.entity.Oauth;
import com.bluemobi.pro.entity.RegisterUser;
import com.bluemobi.pro.entity.UserBank;
import com.bluemobi.pro.entity.UserInfo;
import com.bluemobi.pro.entity.UserLogin;
import com.bluemobi.sys.service.BaseService;
import com.bluemobi.utils.DateUtils;
import com.bluemobi.utils.ImageUtils;

@Service
public class UserService extends BaseService{

	public static final String PRIFIX_USER_LOGIN = UserLogin.class.getName();
	public static final String PRIFIX_USER_INFO = UserInfo.class.getName();
	
	/**
	 * 新增用户记录
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public int addUser(RegisterUser user) throws Exception {
		
		// 新增用户登录信息
		this.getBaseDao().save(PRIFIX_USER_LOGIN + ".insert", user);
		
		// 新增用户基本信息
		UserInfo userInfo = new UserInfo();
		if(user.getGender() != null) userInfo.setGender(user.getGender());
		if(user.getHeadPic() != null) userInfo.setHeadPic(user.getHeadPic());
		userInfo.setUserId(user.getId());
		userInfo.setMobile(user.getMobile());
		userInfo.setNickname(user.getNickname());
		this.getBaseDao().save(PRIFIX_USER_INFO + ".insert", userInfo);
		return user.getId();
	}
	
	/**
	 * 
     * @Title: findUserInfoById
     * @Description: 根据用户ID查询用户信息
     * @param @param userInfo
     * @param @return
     * @param @throws Exception    参数
     * @return UserInfo    返回类型
     * @throws
	 */
	public UserInfo findUserInfoById(UserInfo userInfo) throws Exception {
		return this.getBaseDao().get(PRIFIX_USER_INFO + ".findOne", userInfo);
	}
	
	/**
	 * 判断用户是否存在
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public Boolean isExist(UserLogin user) throws Exception  {
		
		UserLogin userLogin = findUserByMobile(user);
		if(userLogin != null && userLogin.getUsername() != null && userLogin.getId() != null) {
			return Boolean.valueOf(true);
		}
		return Boolean.valueOf(false);
	}
	
	/**
	 * 根据手机号查询用户信息
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public UserLogin findUserByMobile(UserLogin user) throws Exception {
		UserLogin userLogin = this.getBaseDao().getObject(PRIFIX_USER_LOGIN + ".findOne", user);
		return userLogin;
	} 
	
	/**
	 * 查询第三方openID是否有对应userId
	 * @param openId
	 * @return
	 * @throws Exception
	 */
	public Oauth findOauthByOpenId(String openId) throws Exception {
		return this.getBaseDao().get(PRIFIX_USER_LOGIN + ".findOauthByOpenId", openId);
	}
	
	/**
	 * 修改用户信息
	 * @param user
	 * @throws Exception
	 */
	public UserInfo modifyUser(UserInfo userInfo,MultipartFile file) throws Exception {
		Image image = ImageUtils.saveImage(file, false);
		userInfo.setHeadPic(image.getImage());
		this.getBaseDao().update(PRIFIX_USER_INFO + ".update", userInfo);
		return userInfo;
	}
	
	/**
	 * 
	 * @Title: modifyUserLoginPassword 
	 * @Description: 修改用户登录信息密码 
	 * @param 
	 * @param user
	 * @param 
	 * @throws Exception 参数 
	 * @return void 返回类型 
	 * @throws
	 */
	public void modifyUserLoginPassword(UserLogin user) throws Exception {
		this.getBaseDao().update(PRIFIX_USER_LOGIN + ".update", user);
	}
	
	/**
	 * 
     * @Title: bingBank
     * @Description: 绑定银行卡
     * @param @param userBank
     * @param @throws Exception    参数
     * @return void    返回类型
     * @throws
	 */
	public int bingBank(final UserBank userBank) throws Exception {
		UserBank _userBank = this.getBaseDao().getObject(PRIFIX_USER_INFO + ".findUserBankByUserId", userBank);
		if( _userBank != null) {
			return -1; // 返回-1表示已绑定过改银行卡
		}
		return this.getBaseDao().save(PRIFIX_USER_INFO + ".bingBankCard", userBank);
	}
	
	/**
	 * 
     * @Title: findUserBank
     * @Description: 查询用户银行卡
     * @param @param userBank
     * @param @return
     * @param @throws Exception    参数
     * @return List<UserBank>    返回类型
     * @throws
	 */
	public List<UserBank> findUserBank(UserBank userBank) throws Exception {
		return this.getBaseDao().getList(PRIFIX_USER_INFO + ".findUserBankByUserId", userBank);
	}
	
	/**
	 * 
     * @Title: deleteUserBank
     * @Description: 解除绑定银行卡
     * @param @param userBank
     * @param @return
     * @param @throws Exception    参数
     * @return int    返回类型
     * @throws
	 */
	public int deleteUserBank(UserBank userBank) throws Exception {
		return this.getBaseDao().delete(PRIFIX_USER_INFO + ".deleteUserBank", userBank);
	}
	
	/**
	 * 
     * @Title: countMgsNum
     * @Description: 计算评论消息数
     * @param @param userInfo
     * @param @return
     * @param @throws Exception    参数
     * @return int    返回类型
     * @throws
	 */
	public int countMgsNum(UserInfo userInfo)  throws Exception {
		return this.getBaseDao().get(PRIFIX_USER_INFO + ".countMgsNum", userInfo);
	}
	
	public void readMsg(Integer userId) throws Exception {
		this.getBaseDao().update(PRIFIX_USER_INFO + ".readMsg", userId);
	}
	
	/**
	 * 新增第三方信息
	 * @param auth
	 * @throws Exception
	 */
	public void insertAuth(Oauth auth) throws Exception {
		this.getBaseDao().save(PRIFIX_USER_LOGIN + ".insertAuth", auth);
	}
	
	/**
	 * 绑定用户
	 * @param auth
	 * @throws Exception
	 */
	public void updateOauth(Oauth auth) throws Exception {
		this.getBaseDao().update(PRIFIX_USER_LOGIN + ".updateOauth", auth);
	}
	
	/***********************************************************************
	 ************************************二期接口******************************
	 ***********************************************************************
	 */
	@Transactional
	public void addAddress(Map<String,Object> params) throws Exception {
		params.put("create_date", DateUtils.getCurrentTime());
		params.put("modify_date", DateUtils.getCurrentTime());
		
		Integer isDefault = Integer.parseInt(params.get("isDefault").toString());
		
		this.getBaseDao().save(PRIFIX_USER_INFO + ".addAddress", params);
		params.put("addressId", params.get("id"));
		
		if(isDefault == 1){
			this.getBaseDao().update(PRIFIX_USER_INFO + ".editAddressIsDefault", params);
//			this.getBaseDao().update(PRIFIX_USER_INFO + ".selectDefaultAddress", params);
		}
		this.getBaseDao().update(PRIFIX_USER_INFO + ".editAddress", params);
	}
	
	public void editAddress(Map<String,Object> params) throws Exception {
		
		String is_default = params.get("isDefault").toString();
		if("1".equals(is_default)){
			this.getBaseDao().update(PRIFIX_USER_INFO + ".editAddressIsDefault", params);
//			this.getBaseDao().update(PRIFIX_USER_INFO + ".selectDefaultAddress", params);
		}
		params.put("is_default", Byte.parseByte(params.get("isDefault").toString()));
		this.getBaseDao().update(PRIFIX_USER_INFO + ".editAddress", params);
	}
	
	@SuppressWarnings("rawtypes")
	public List findAllAddress(Map<String,Object> params) throws Exception {
		List<Map<String,Object>> list = this.getBaseDao().getList(PRIFIX_USER_INFO + ".selectAllAddress", params);
		return list == null ? Collections.emptyList() : list;
	}
	
	// 设置默认地址
	@Transactional
	public void selectDefaultAddres(Map<String,Object> params) throws Exception {
//		this.getBaseDao().update(PRIFIX_USER_INFO + ".selectDefaultAddress", params);
		this.getBaseDao().update(PRIFIX_USER_INFO + ".editAddressIsDefault", params);
		this.getBaseDao().update(PRIFIX_USER_INFO + ".updateReceiver", params);
	} 
	
	// 删除收货地址
	public void removeAddress(Map<String,Object> params) throws Exception {
		// 如果该地址是用户的默认地址，更新用户表address值
//		Map<String,Object> memberAddress = this.getBaseDao().get(PRIFIX + ".findMemberAddress", params);
//		if(memberAddress != null && memberAddress.get("delivery_address") != null) {
//			
//		}
		this.getBaseDao().delete(PRIFIX_USER_INFO + ".deleteAddress", params);
	}
	
	public void modifyOrder(Map<String,Object> params) throws Exception{
		
		Object addressObj = params.get("addressId");
		Object expireObj = params.get("expire");
		Map<String,Object> addressMap = new HashMap<String, Object>();
		if(addressObj != null) {
			addressMap = this.getBaseDao().get(PRIFIX_USER_INFO + ".findByAddressId", params);
		}
		if(expireObj != null) {
			addressMap.put("expire", expireObj);
		}
		addressMap.put("orderId", params.get("orderId"));
		addressMap.put("order_status", "0");
		this.getBaseDao().update("com.bluemobi.pro.service.impl.XxShopServiceImpl.motifyOrder", addressMap);
	}
	
	// 根据Id获取地址信息
	public Map<String,Object> findByAddressId(String addressId) throws Exception {
		return this.getBaseDao().get(PRIFIX_USER_INFO + ".findByAddressId", addressId);
	}
}
