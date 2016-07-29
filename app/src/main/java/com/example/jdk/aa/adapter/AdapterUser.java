package com.example.jdk.aa.adapter;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jdk.aa.R;
import com.example.jdk.aa.adapter.base.AdapterBase;
import com.example.jdk.aa.business.BusinessUser;
import com.example.jdk.aa.model.ModelUser;

public class AdapterUser extends AdapterBase {
	
	private class Holder
	{
		ImageView ivUserIcon;
		TextView tvUserName;
	}
	
	public AdapterUser(Context pContext) {
		super(pContext, null);
		BusinessUser _BusinessUser = new BusinessUser(pContext);
		List _List = _BusinessUser.GetNotHideUser();
		SetList(_List);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder _Holder;
		
		if (convertView == null) {
			convertView = GetLayoutInflater().inflate(R.layout.user_list_item, null);
			_Holder = new Holder();
			_Holder.ivUserIcon = (ImageView) convertView.findViewById(R.id.ivUserIcon);
			_Holder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
			convertView.setTag(_Holder);
		}
		else {
			_Holder = (Holder) convertView.getTag();
		}
		
		ModelUser _Info = (ModelUser) GetList().get(position);
		
		_Holder.ivUserIcon.setImageResource(R.mipmap.user_big_icon);
		_Holder.tvUserName.setText(_Info.getUserName());
		
		return convertView;
	}

}
