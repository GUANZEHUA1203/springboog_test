package com.gzh.springboot.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**@Copyright CHJ
 * @Author HUANGP
 * @Date 2018年4月25日
 * @Desc Property 工具类
 */
public class PropertyUtils {
	
	private static Properties props;

	synchronized static private void loadProps(String file) {
		props = new Properties();
		InputStream in = null;
		try {
			in = PropertyUtils.class.getClassLoader().getResourceAsStream(file);
			props.load(in);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
			}
		}
	}

	public static String getProperty(String key,String fileName) {
		if (null == props) {
			loadProps(fileName);
		}
		return props.getProperty(key);
	}
	
	public static void setProperty(String key,String  value,String fileName) {
		if (null == props) {
			loadProps(fileName);
		}
		props.setProperty(key,value);
	}

	public static String getProperty(String key, String defaultValue,String fileName) {
		if (null == props) {
			loadProps(fileName);
		}
		return props.getProperty(key, defaultValue);
	}

}