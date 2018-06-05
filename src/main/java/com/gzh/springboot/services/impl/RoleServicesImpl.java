/**
 * 
 */
package com.gzh.springboot.services.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzh.springboot.dao.RoleMapper;
import com.gzh.springboot.services.RoleServices;

/**
 * @author zehua
 *
 */
@Service
public class RoleServicesImpl implements RoleServices{
	@Autowired
	private RoleMapper roleMapper;
	
	@Override
	public List<Map<String, Object>> getAll() {
		return this.roleMapper.getAll();
	}
	
}

