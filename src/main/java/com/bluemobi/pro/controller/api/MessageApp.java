package com.bluemobi.pro.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluemobi.pro.entity.Message;
import com.bluemobi.pro.entity.MsgCount;
import com.bluemobi.pro.service.impl.MessageService;
import com.bluemobi.utils.Result;

/**
 * 
 * @ClassName: MessageApp
 * @Description: 消息controller
 * @author Administrator
 * @date 2015年12月11日
 *
 */
@RequestMapping("/app/msg/")
@Controller
public class MessageApp {
	
	@Autowired
	private MessageService service;

	/**
	 * 
     * @Title: findMessageByUserId
     * @Description: 查询用户消息
     * @param @param message
     * @param @return    参数
     * @return Result    返回类型
     * @throws
	 */
	@RequestMapping(value = "list", method = RequestMethod.POST)
	@ResponseBody
	public Result findMessageByUserId(Message message) {
		
		List<Message> messageList = null;
		try {
			messageList = service.findMessageByUserId(message);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.failure();
		}
		return Result.success(messageList);
	}
	
	/**
	 * 
     * @Title: deleteMessage
     * @Description: 删除消息
     * @param @param message
     * @param @return    参数
     * @return Result    返回类型
	 * @throws
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteMessage(Message message) {
		
		try {
			service.deleteMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.failure();
		}
		return Result.success();
	}
	
	@RequestMapping(value="sysMsgCount", method = RequestMethod.POST)
	@ResponseBody
	public Result sysMsgCount(Message msg) {
		
		int count = 0;
		MsgCount mc = new MsgCount();
		try {
			count = service.sysMsgcount(msg);
			mc.setNum(count);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.failure();
		}
		return Result.success(mc);
	}
	
	@RequestMapping(value="readMsg", method = RequestMethod.POST)
	@ResponseBody
	public Result readMessage(@RequestParam("ids") String ids,@RequestParam("userId") Integer userId) {
		
		try {
			service.readMsg(ids, userId);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.failure();
		}
		return Result.success();
	}
}
