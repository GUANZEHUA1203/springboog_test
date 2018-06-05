/**
 * Project Name:ice-equi
 * File Name:CIResult.java
 * Package Name:com.ice.domain
 * Date:2018年4月23日下午12:03:16
 * Copyright (c) 2018, 彩慧居 All Rights Reserved.
 *
*/

package com.gzh.springboot.domain;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gzh.springboot.common.ErrorCode;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:CIResult <br/>
 * Date: 2018年4月23日 下午12:03:16 <br/>
 * 
 * @author R.ZENG
 * @version
 * @since JDK 1.8
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter @Getter
public class CIResult {
	
	
	private int code; // 结果编号
	private String message = ""; // 结果信息

	private Object content;

	public CIResult() {
	}

	public CIResult(int code,String msg,Object data) {
		this.code = code;
		this.message = msg == null ? "" : msg;
		this.content = data==null ? null:data;
	}
	
	public CIResult(ErrorCode error,Object data) {
		this.code = error.getCode();
		this.message = error.getMsg();
		this.content = data==null ? null:data;
	}
	
	public CIResult(ErrorCode error,String msg,Object data) {
		this.code = error.getCode();
		this.message = StringUtils.isNotEmpty(msg)?msg:error.getMsg();
		this.content = data==null ? null:data;
	}
	
	public static CIResult getInstince(ErrorCode error,Object data){
		return new CIResult(error,data);
	}
	
	public static CIResult getInstince(ErrorCode error,String msg){
		return new CIResult(error,msg,null);
	}
	
	public static CIResult getInstince(ErrorCode error){
		return new CIResult(error,"",null);
	}


}
