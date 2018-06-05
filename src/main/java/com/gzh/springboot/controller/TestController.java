package com.gzh.springboot.controller;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gzh.springboot.common.ErrorCode;
import com.gzh.springboot.domain.CIResult;
import com.gzh.springboot.services.RoleServices;

@RestController
public class TestController {

	@Autowired
	private RoleServices roleServices;

	/**
	 * @Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月18日
	 * @Desc
	 *
	 * @param mName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getAllRoles",method=RequestMethod.GET)
	public @ResponseBody Object testInsert( HttpServletRequest request){
		List<Map<String, Object>> all = roleServices.getAll();
		return CIResult.getInstince(ErrorCode.SUCCESS,all);
	}

}
