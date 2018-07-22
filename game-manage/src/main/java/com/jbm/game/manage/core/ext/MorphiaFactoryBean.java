package com.jbm.game.manage.core.ext;

import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * mongodb Morphia 工厂
 * @author JiangBangMing
 *
 * 2018年7月17日 下午12:56:49
 */
public class MorphiaFactoryBean extends AbstractFactoryBean<Morphia>{

	//要扫描并映射的包
	private String[] mapPackages;
	
	
	//要映射的类
	private String[] mapClasses;
	
	//扫描包时，是否忽略不映射的类，这里按照Morphia的原始定义，默认设置为false
	private boolean ignoreInvalidCasses;
	
	@Override
	protected Morphia createInstance() throws Exception {
		Morphia m=new Morphia();
		if(mapPackages!=null) {
			for(String packageName:mapPackages) {
				m.mapPackage(packageName,ignoreInvalidCasses);
			}
		}
		if(mapClasses!=null) {
			for(String entityClass:mapClasses) {
				m.map(Class.forName(entityClass));
			}
		}
		return m;
	}

	@Override
	public Class<?> getObjectType() {
		return Morphia.class;
	}

	public String[] getMapPackages() {
		return mapPackages;
	}

	public void setMapPackages(String[] mapPackages) {
		this.mapPackages = mapPackages;
	}

	public String[] getMapClasses() {
		return mapClasses;
	}

	public void setMapClasses(String[] mapClasses) {
		this.mapClasses = mapClasses;
	}

	public boolean isIgnoreInvalidCasses() {
		return ignoreInvalidCasses;
	}

	public void setIgnoreInvalidCasses(boolean ignoreInvalidCasses) {
		this.ignoreInvalidCasses = ignoreInvalidCasses;
	}
	
	

}
