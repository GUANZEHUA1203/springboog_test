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
	
	SYSTEM_PARAMETER_EXCEPTION(-2,"缺少请求参数"),
	
	SYSTEM_PARAMETER_PARSE_FAIL_EXCEPTION(-3,"参数解析失败"),
	
	SYSTEM_METHOD_NOT_EXCEPTION(-4,"不支持当前请求方法"),
	
	//文件上传
	SYSTEM_FILE_UPLOAD_EXCEPTION(10020,"文件上传异常"),
	
	
	//设备添加异常
	DEVICE_ADD_EXCEPTION(10001,"设备添加异常"),
	//设备查询异常
	DEVICE_QUERY_EXCEPTION(10002,"设备查询异常"),
	//设备修改异常
	DEVICE_UPDATE_EXCEPTION(10003,"操作更新异常"),
	//设备删除（逻辑删除）异常
	DEVICE_DEL_EXCEPTION(10004,"操作删除异常"),
	//流量开门异常
	FLOW_OPEN_DOOR_EXCEPTION(10006,"流量开门异常"),
	//门禁设备心跳异常
	DEVICE_HEART_BEAT_EXCEPTION(10007,"门禁设备心跳异常"),
	//必填参数
	PARAMETERS_REQUIRED(10008,"缺失必填参数"),
	//查询记录不存在
	NO_DATA(10009,"记录不存在"),
	
	
	


	
	FACE_BIND_EXCEPTION(10010,"人脸信息绑定异常"),
	CARD_BIND_EXCEPTION(10011,"门禁卡绑定异常"),
	MANAGER_CARD_BIND_EXCEPTION(10012,"管理卡绑定异常"),
	MANAGER_DELETE_EXCEPTION(10013,"管理卡信息删除异常"),
	FACE_CARD_QUERY_EXCEPTION(10014,"人脸/门禁卡信息查询异常"),
	MANAGER_CARD_QUERY_EXCEPTION(10015,"管理卡信息查询异常"),
	
	
	PARAM_EXCEPTION(10016,"参数有误"),
	
	
	NOTICE_EXCEPTION(10017,"消息发送异常"),
	;
	
	
	
	

	private int code;
	private String msg;
	
	private  ErrorCode(int code,String msg){
		this.code=code;
		this.msg=msg;
	}

}
