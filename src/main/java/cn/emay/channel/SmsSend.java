package cn.emay.channel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.emay.channel.httpclient.SDKHttpClient;

public class SmsSend {

	public static String sn = "6SDK-EMY-6688-JDTTQ";// 软件序列号,请通过亿美销售人员获取
	public static String key = "506980";// 序列号首次激活时自己设定
	public static String password = "506980";// 密码,请通过亿美销售人员获取
	public static String baseUrl = "http://sdk4rptws.eucp.b2m.cn:8080/sdkproxy/";
	
	/**
	 * 
     * @Title: send
     * @Description: 发送验证码
     * @param @param mobile
     * @param @param code
     * @param @return    参数
     * @return int    返回类型
     * @throws
	 */
	public static boolean send(String mobile,String code) {
		String mdn = "13476107753";
		String message = "【验证码】你的验证码为";
		try {
			message = URLEncoder.encode(message, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		long seqId = System.currentTimeMillis();
		String param = "cdkey=" + sn + "&password=" + key + "&phone=" + mdn + "&message=" + (message + code) + "&addserial=&seqid=" + seqId;
		String url = baseUrl + "sendsms.action";
		String ret = SDKHttpClient.sendSMS(url, param);
		
		int respcode = Integer.parseInt(ret);
		if(respcode == 0) {
			return true;
		}
		return false;
	}
}
