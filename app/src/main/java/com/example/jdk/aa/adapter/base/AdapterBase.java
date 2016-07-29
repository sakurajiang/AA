package com.example.jdk.aa.adapter.base;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class AdapterBase extends BaseAdapter {

	private List mList;
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	
	public AdapterBase(Context pContext,List pList)
	{
		mContext = pContext;
		mList = pList;
		mLayoutInflater = LayoutInflater.from(mContext);
	}
	
	public LayoutInflater GetLayoutInflater()
	{
		return mLayoutInflater;
	}
	
	public Context GetContext()
	{
		return mContext;
	}
	
	public List GetList()
	{
		return mList;
	}
	
	public void SetList(List pList) {
		mList = pList;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int pPosition) {
		return mList.get(pPosition);
	}

	@Override
	public long getItemId(int pPosition) {
		return pPosition;
	}

}
