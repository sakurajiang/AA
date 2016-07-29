package com.example.jdk.aa.business.base;

import android.content.Context;

public class BusinessBase {
	private Context mContext;
	
	protected BusinessBase(Context pContext) {
		mContext = pContext;
	}
	
	protected Context GetContext() {
		return mContext;
	}
	
	protected String GetString(int pResID) {
		return mContext.getString(pResID);
	}
	
	protected String GetString(int pResID,Object[] pObject) {
		return mContext.getString(pResID, pObject);
	}
}
