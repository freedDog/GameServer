package com.jbm.game.manage.core.vo;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年7月17日 下午12:39:09
 */
public class ResultVO<T> {

	private Integer status;
	private String message;
	private T result;
	
	
	
	public ResultVO(Integer status,String message,T result) {
		this.setStatus(status);
		this.setMessage(message);
		this.setResult(result);
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
	
	
	
}
