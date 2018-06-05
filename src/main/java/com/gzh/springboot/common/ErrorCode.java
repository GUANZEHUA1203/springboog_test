package com.gzh.springboot.common;



import lombok.Getter;

/**@Copyright CHJ
 * @Author HUANGP
 * @Date 2018年5月4日
 * @Desc 系统异常代码定义
 */
@Getter
public enum ErrorCode{
	
	//操作成功
	SUCCESS(0,"操作成功"),
	//操作失败
	FAIL(1,"操作失败"),
	//系统异常
	SYSTEM_EXCEPTION(-1,"系统异常，请联系管理员"),

	;
	
	
	
	

	private int code;
	private String msg;
	
	private  ErrorCode(int code,String msg){
		this.code=code;
		this.msg=msg;
	}

}
