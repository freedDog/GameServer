package com.jbm.game.model.script;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;

import com.jbm.game.engine.script.IScript;

/**
 *配置加载脚本
 * @author JiangBangMing
 *
 * 2018年7月23日 下午2:34:27
 */
public interface IConfigScript extends IScript{
	
	/**
	 * 加载配置
	 * @param tableName  指定的配置表 null加载所有
	 * @return
	 */
	public default String reloadConfig(List<String> tableName) {
		return "未加载任何配置";
	}
	
	/**
	 * 是否包含加载表
	 * @param tables
	 * @param clazz
	 * @return
	 */
	public default boolean containTable(List<String> tables,Class<?> clazz) {
		if(tables==null||tables.isEmpty()) {
			return true;
		}
		
		Entity entity=clazz.getAnnotation(Entity.class);
		if(entity!=null&&tables.contains(entity.value())) {
			return true;
		}
		return false;
	}
}
