/**
 * 
 */
package com.gzh.springboot.services;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

/**
 * @author zehua
 *
 */
public interface RoleServices {
	
	public List<Map<String, Object>> getAll();
}

