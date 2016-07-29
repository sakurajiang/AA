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
import com.example.jdk.aa.controls.SlideMenuItem;

public class AdapterSlideMenu extends AdapterBase {
	
	private class Holder
	{
		TextView tvMenuName;
	}
	
	public AdapterSlideMenu(Context pContext, List pList) {
		super(pContext, pList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder _Holder;
		
		if (convertView == null) {
			convertView = GetLayoutInflater().inflate(R.layout.slidemenu_list_item, null);
			_Holder = new Holder();
			_Holder.tvMenuName = (TextView) convertView.findViewById(R.id.tvMenuName);
			convertView.setTag(_Holder);
		}
		else {
			_Holder = (Holder) convertView.getTag();
		}
		
		SlideMenuItem _Item = (SlideMenuItem) GetList().get(position);
		
		_Holder.tvMenuName.setText(_Item.getTitle());
		
		return convertView;
	}

}
