package com.jbm.game.model.mongo;

import java.util.Map;

/**
 * 配置实体检测接口
 * <br>
 * 配置文件之间存在依赖关系，在加载检测策划配置数据是否正确
 * @author JiangBangMing
 *
 * 2018年7月23日 下午3:10:12
 */
public interface IConfigChecker {

	
	public default <V> boolean check(Map<Integer, V> source) throws Exception{
		return true;
	}
	
	public default <V,W> boolean check(Map<Integer, V> source,Map<Integer, W> source2) throws Exception{
		return true;
	}
	
	public default<V,W,X> boolean check(Map<String, V> source,Map<Integer, W> source2,Map<Integer, X> source3) {
		return true;
	}
	
	/**
	 * 数据检测
	 */
	public default boolean check() throws Exception{
		return true;
	}
}
