package com.gzh.springboot.utils;


import java.util.Date;
import java.util.regex.Pattern;

import com.google.common.base.Strings;


/**
 * 数据验证工具类<br>
 * 
 * @author xuyc
 * @date 2015-04-01
 */
public abstract class ValidationUtils {


	/**
	 * 手机号码校验<br>
	 * 
	 * <pre>
	 * 1. 手机号码（0|86|17951）11位数字，包含13[0-9],14[57],15[012356789],17[0678],18[0-9]号码段
	 * 
	 * \d 等效于 [0-9]
	 * ? 等效于 {0,1}
	 * </pre>
	 * 
	 * @param phone
	 *            手机号码
	 * @return (true: 有效手机号码；false： 无效手机号码)
	 */
	public static boolean isMobilePhone(String phone) {

		if (Strings.isNullOrEmpty(phone)) {
			return false;
		}
		return regxMatch("^(0|86|17951)?(13\\d|15[012356789]|17[0135678]|18\\d|14[57])\\d{8}$", phone.trim());
	}

	/**
	 * 座机号码校验<br>
	 * 
	 * <pre>
	 * 1. 手机号码（0|86|17951）11位数字，包含13[0-9],14[57],15[012356789],17[0678],18[0-9]号码段
	 * 2. 座机号码：区号（3位或者4位）+ 电话（7位或者8位）
	 * </pre>
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isMobile(String phone) {

		if (Strings.isNullOrEmpty(phone)) {
			return false;
		}
		return regxMatch("^(((\\d{3,4}-)?\\d{7,8})|(0|86|17951)?(13\\d|15[012356789]|17[013678]|18\\d|14[57])\\d{8})$",
				phone.trim());
	}

	
	
	
	
	 
	/**
	 * 门禁卡卡号校验<br>
	 * 
	 * <pre>
	 * 1. 不能为空
	 * 2. 有效位数6-12位
	 * 3. 只能为0~9或啊a-f
	 * </pre>
	 * 
	 * @param cardNo
	 *            IC卡号
	 * @return 校验结果信息
	 */
	public static boolean isICCard(String cardNo) {

		// 判断IC卡是否空或者为空字符串
		if (Strings.isNullOrEmpty(cardNo)) {
			return false;
		}
		// 判断IC卡是否为8位
		// if (cardNo.trim().length() != 8) {
		// return false;
		// }
		// 判断是否字符为数组或者字母
		return regxMatch("^[A-Fa-f0-9]{6,12}$", cardNo);
	}

	 
	/**
	 * 验证应用程序版本号规则<br>
	 * 
	 * <pre>
	 * 版本号规则: 数字或者英文点，并且英文点不能出现在首尾.
	 * 【^(?![.])(?!.*?[.]$)([0-9]|[.])+$】
	 * 修改为：
	 * 版本号规则: 数字.数字.数字
	 * 【[0-9]+[\\u002E][0-9]+[\\u002E][0-9]+】
	 * 
	 * . --> \\u002E(转移字符)
	 * </pre>
	 * 
	 * @param version
	 *            版本号
	 * @return true/false
	 */
	public static boolean isVersionNumber(String version) {

		if (Strings.isNullOrEmpty(version)) {
			return false;
		}
		return regxMatch("^[0-9]+[\\u002E][0-9]+[\\u002E][0-9]+$", version);
	}

	/**
	 * 纯数字验证<br>
	 * 
	 * @param no
	 * @return
	 */
	public static boolean isNumber(int no) {
		return regxMatch("^\\+?[1-9][0-9]*$", String.valueOf(no));
	}

  
	public static boolean regxMatch(String regx, String value) {
		return Pattern.compile(regx).matcher(value).matches();
	}

 
  
}
