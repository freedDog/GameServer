package com.jbm.game.manage.core.ext;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * mongdb
 * @author JiangBangMing
 *
 * 2018年7月17日 下午12:43:34
 */
public class DataStoreFactoryBean extends AbstractFactoryBean<Datastore>{
	
	private Morphia morphia;//morphia 实例，最好是单例
	
	private String dbName;//数据库名
	
	private String uri;//连接地址
	
	private boolean toEnsureIndexs=false;//是否确认索引存在，默认false;
	
	private boolean toEnsureCaps=false;//是否确认caps存在,默认flase
	

	@Override
	protected Datastore createInstance() throws Exception {
		//这里的userName 和password 可以为null,morphia 对象会处理
		MongoClientURI mongoClientURI=new MongoClientURI(uri);
		Datastore ds=morphia.createDatastore(new MongoClient(mongoClientURI), dbName);
		if(toEnsureIndexs) {
			ds.ensureIndexes();
		}
		if(toEnsureCaps) {
			ds.ensureCaps();
		}
		return ds;
	}

	@Override
	public Class<?> getObjectType() {
		return Datastore.class;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		if(morphia==null) {
			throw new IllegalStateException();
		}
	}

	public Morphia getMorphia() {
		return morphia;
	}

	public void setMorphia(Morphia morphia) {
		this.morphia = morphia;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public boolean isToEnsureIndexs() {
		return toEnsureIndexs;
	}

	public void setToEnsureIndexs(boolean toEnsureIndexs) {
		this.toEnsureIndexs = toEnsureIndexs;
	}

	public boolean isToEnsureCaps() {
		return toEnsureCaps;
	}

	public void setToEnsureCaps(boolean toEnsureCaps) {
		this.toEnsureCaps = toEnsureCaps;
	}
	
	

}
