package com.jbm.game.hall.manager;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.script.ScriptManager;
import com.jbm.game.hall.script.IUserScript;
import com.jbm.game.model.struct.User;

/**
 * 用户管理
 * @author JiangBangMing
 *
 * 2018年7月23日 下午3:48:19
 */
public class UserManager {

	
	private static final Logger logger=LoggerFactory.getLogger(UserManager.class);
	private static volatile UserManager userManager;
	private UserManager() {
		
	}
	public static UserManager getInstance() {
		if(userManager==null) {
			synchronized (UserManager.class) {
				if(userManager==null) {
					userManager=new UserManager();
				}
			}
		}
		return userManager;
	}
	
	/**
	 * 创建角色
	 * @param userConsumer
	 * @return
	 */
	public User createUser(Consumer<User> userConsumer) {
		Collection<IUserScript> evts=ScriptManager.getInstance().getBaseScriptEntry().getEvts(IUserScript.class);
		Iterator<IUserScript> iterator=evts.iterator();
		while(iterator.hasNext()) {
			IUserScript userScript=iterator.next();
			User user=userScript.createUser(userConsumer);
			if(user!=null) {
				return user;
			}
		}
		return null;
	}
	
}
