package com.starting.config;

import com.starting.exception.ResException;

public enum ResponseState {
	/**
	 * 空实例
	 */
	INSTANTIATION(),
	/**
	 * 200:请求成功
	 */
	SUSSESS("200", "SUSSESS"),
	/**
	 * 111:警告
	 */
	WARNING("111", "未查到相关数据"),
	/**
	 * 999:请求失败
	 */
	ERROR("999", "ERROR"),
	/**
	 * 500:服务器异常
	 */
	E500("500", "服务器异常"),
	/**
	 * 404:路径找不到
	 */
	E404("404", "路径找不到");

	private String code;
	private String msg;

	ResponseState() {
	}

	ResponseState(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/**
	 * 抓取异常
	 * 
	 * @param e
	 * @return
	 */
	public ResponseState catchException(ResException e) {
		this.code = e.getCode();
		this.msg = e.getMessage();
		return this;
	}

	public String getCode() {
		return code;
	}

	public ResponseState setCode(String code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public ResponseState setMsg(String msg) {
		this.msg = msg;
		return this;
	}
}
