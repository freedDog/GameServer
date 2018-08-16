/**
* 宸ュ叿鑷姩鐢熸垚,鏆傛椂涓嶆敮鎸佹硾鍨嬪拰瀵硅薄
* @author JiangBangMing
* @date 
*/
package com.jbm.game.model.mongo.bydr.entity;

import java.util.List;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import com.jbm.game.model.mongo.IConfigChecker;

/**
 *
 * @date 
 */
@Entity(value = "config_notice", noClassnameStored = true)
public class ConfigNotice implements IConfigChecker{

	@Id
	@Indexed
	/**编号*/
	private int id;
	/**枚举类型字段*/
	private String name;
	/**中文*/
	private String cn;
	/**英文*/
	private String en;

	/**编号*/
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id=id;
	}
	/**枚举类型字段*/
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name=name;
	}
	/**中文*/
	public String getCn(){
		return this.cn;
	}
	
	public void setCn(String cn){
		this.cn=cn;
	}
	/**英文*/
	public String getEn(){
		return this.en;
	}
	
	public void setEn(String en){
		this.en=en;
	}

}
