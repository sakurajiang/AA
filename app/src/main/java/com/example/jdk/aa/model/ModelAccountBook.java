package com.example.jdk.aa.model;

import java.util.Date;

public class ModelAccountBook {
	//账本表主键ID
	private int mAccountBookID;
	//账本名称
	private String mAccountBookName;
	//添加日期
	private Date mCreateDate = new Date();
	//状态 0失效 1启用
	private int mState = 1;
	//是否默账本 0否1是
	private int mIsDefault;
	/**
	 * 账本表主键ID
	 */
	public int GetAccountBookID() {
		return mAccountBookID;
	}
	/**
	 * 账本表主键ID
	 */
	public void SetAccountBookID(int pAccountBookID) {
		this.mAccountBookID = pAccountBookID;
	}
	/**
	 * 账本名称
	 */
	public String GetAccountBookName() {
		return mAccountBookName;
	}
	/**
	 * 账本名称
	 */
	public void SetAccountBookName(String pAccountBookName) {
		this.mAccountBookName = pAccountBookName;
	}
	/**
	 * 添加日期
	 */
	public Date GetCreateDate() {
		return mCreateDate;
	}
	/**
	 * 添加日期
	 */
	public void SetCreateDate(Date pCreateDate) {
		this.mCreateDate = pCreateDate;
	}
	/**
	 * 状态 0失效 1启用
	 */
	public int GetState() {
		return mState;
	}
	/**
	 * 状态 0失效 1启用
	 */
	public void SetState(int pState) {
		this.mState = pState;
	}
	/**
	 * 是否默账本 0否1是
	 */
	public int GetIsDefault() {
		return mIsDefault;
	}
	/**
	 * 是否默账本 0否1是
	 */
	public void SetIsDefault(int pIsDefault) {
		this.mIsDefault = pIsDefault;
	}
}
