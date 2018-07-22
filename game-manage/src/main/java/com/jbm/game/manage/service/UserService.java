package com.jbm.game.manage.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.jbm.game.manage.constant.SessionKey;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年7月17日 下午1:26:57
 */

@Service
public class UserService {


	/**
	 * 存储cookie
	 * @param session
	 * @param userName
	 * @param response
	 */
	public void saveCookie(HttpSession session,String userName,HttpServletResponse response) {
		Cookie sessionCookie=new Cookie(SessionKey.HTTP_SESSION,session.getId());
		sessionCookie.setMaxAge(3600);
		sessionCookie.setPath("/");
		response.addCookie(sessionCookie);
		
		Cookie loginidCookie=new Cookie(SessionKey.USER_NAME, userName);
		loginidCookie.setMaxAge(25920000);
		loginidCookie.setPath("/");
		response.addCookie(loginidCookie);
	}
}
