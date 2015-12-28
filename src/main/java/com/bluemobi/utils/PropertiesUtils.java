package com.bluemobi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.stereotype.Component;

/**
 * Properties工具类
 * 
 * @author maew
 * @version 2012-9-11 下午01:10:22
 */
@Component
public class PropertiesUtils {
	@Autowired
	private PropertyPlaceholderConfigurer propertyResourceConfigurer;
	private static PropertiesUtils piu;
	private Properties properties;

	/**
	 * 初始化工具类默认属性
	 * 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@PostConstruct
	public void init() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		piu = this;
		properties = new Properties();
		Method mergeProperties = PropertiesLoaderSupport.class.getDeclaredMethod("mergeProperties");
		mergeProperties.setAccessible(true);
		Properties props = (Properties) mergeProperties.invoke(propertyResourceConfigurer);
		Method convertProperties = PropertyResourceConfigurer.class.getDeclaredMethod("convertProperties", Properties.class);
		convertProperties.setAccessible(true);
		convertProperties.invoke(propertyResourceConfigurer, props);
		properties.putAll(props);
	}

	public static String getValue(String key) {
		return piu.properties.getProperty(key, null);
	}

	/**
	 * yesong 2014-12-30
	 * @param key
	 * @param path
	 * @return
	 */
	public static String getPropertiesValues(String key,String path) {
		if(StringUtils.isBlank(key) || StringUtils.isBlank(path)){
			throw new IllegalAccessError();
		}
		path = PropertiesUtils.class.getResource("/").getPath() + "resource" + File.separator + path;
		Properties prop = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(new File(path));
			prop.load(in);
			return prop.get(key) != null ? prop.get(key).toString() : ""; 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}
}
