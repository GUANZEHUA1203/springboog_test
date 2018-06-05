package com.gzh.springboot.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;


/**@Copyright CHJ
 * @Author HUANGP
 * @Date 2018年5月6日
 * @Desc 激光消息结构定义
 */
@Setter @Getter
public class MassageResult{
	
	//消息类型【1：开门通知，2：数据拉取通知，3：公告通知】
	private int msgType;
	
	/**【1:开门】
	 * isOpen [true:开门，false:不开]
	 * owenId 住户ID
	 * 
	 * 【2：数据拉取消息通知】
	 * dataType 数据类型 [1:新增，2：删除]
	 * type [1:人脸，2：门禁卡，3：所有]
	 * data     增量/差量唯一标识
	 */
	private Map<String,Object> content;
	
	public MassageResult(int msgType,Object object) throws Exception{
		if(msgType!=3){
			throw new Exception("msgType 类型错误");
		}
		this.msgType=msgType;
		this.content=new HashMap<String,Object>();
		content.put("message",object);
		content.put("timestamp", System.currentTimeMillis());
	}
	
	public MassageResult(int msgType,int type,Object objectO,Object objectT) throws Exception{
		if((objectO instanceof Boolean || objectO instanceof Integer)){
			if(objectT instanceof String || objectT instanceof List){
				this.msgType=msgType;
				this.content=new HashMap<String,Object>();
				if(msgType==1){
					content.put("isOpen", objectO);
					content.put("owenId", objectT);
				}else if(msgType==2){
					content.put("dataType", objectO);
					content.put("type", type);
					content.put("data", objectT);
				}
				content.put("timestamp", System.currentTimeMillis());
			}else{
				throw new Exception("objectT 类型错误");
			}
		}else{
			throw new Exception("objectO 类型错误");
		}
	}

}
