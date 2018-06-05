/**
 * 
 */
package com.gzh.springboot.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

/**
 * @author zehua
 *
 */
public interface RoleMapper {
	
	@Select("select count(0) as allNum from tb_roles")
	public List<Map<String, Object>> getAll();
}

