package com.example.jdk.aa.model;

import java.io.Serializable;
import java.util.Date;

public class ModelCategory implements Serializable {
	//类别表主键ID
	private int m_CategoryID;
	//类别名称
	private String m_CategoryName;
	//类型标记名称
	private String m_TypeFlag;
	//父类型ID
	private int m_ParentID = 0;
	//路径
	private String m_Path;
	//添加日期
	private Date m_CreateDate = new Date();
	//状态 0失效 1启用
	private int m_State = 1;
	/**
	 * 账本表主键ID
	 */
	public int GetCategoryID() {
		return m_CategoryID;
	}
	/**
	 * 账本表主键ID
	 */
	public void SetCategoryID(int p_CategoryID) {
		this.m_CategoryID = p_CategoryID;
	}
	/**
	 * 账本名称
	 */
	public String GetCategoryName() {
		return m_CategoryName;
	}
	/**
	 * 账本名称
	 */
	public void SetCategoryName(String p_CategoryName) {
		this.m_CategoryName = p_CategoryName;
	}
	/**
	 * 类型标记名称
	 */
	public String GetTypeFlag() {
		return m_TypeFlag;
	}
	/**
	 * 类型标记名称
	 */
	public void SetTypeFlag(String p_TypeFlag) {
		this.m_TypeFlag = p_TypeFlag;
	}
	/**
	 * 父类型ID
	 */
	public int GetParentID() {
		return m_ParentID;
	}
	/**
	 * 父类型ID
	 */
	public void SetParentID(int p_ParentID) {
		this.m_ParentID = p_ParentID;
	}
	/**
	 * 路径
	 */
	public String GetPath() {
		return m_Path;
	}
	/**
	 * 路径
	 */
	public void SetPath(String p_Path) {
		this.m_Path = p_Path;
	}
	/**
	 * 添加日期
	 */
	public Date GetCreateDate() {
		return m_CreateDate;
	}
	/**
	 * 添加日期
	 */
	public void SetCreateDate(Date p_CreateDate) {
		this.m_CreateDate = p_CreateDate;
	}
	/**
	 * 状态 0失效 1启用
	 */
	public int GetState() {
		return m_State;
	}
	/**
	 * 状态 0失效 1启用
	 */
	public void SetState(int p_State) {
		this.m_State = p_State;
	}

	@Override
	public String toString() {
		return m_CategoryName;
	}
}
