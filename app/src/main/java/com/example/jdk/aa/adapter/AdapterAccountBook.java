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
import com.example.jdk.aa.business.BusinessAccountBook;
import com.example.jdk.aa.model.ModelAccountBook;

public class AdapterAccountBook extends AdapterBase {
	
	private class Holder  
	{
		ImageView ivIcon;
		TextView tvName;
		TextView tvTotal;
		TextView tvMoney;
	}
	
	public AdapterAccountBook(Context pContext) {
		super(pContext, null);
		BusinessAccountBook _BusinessAccountBook = new BusinessAccountBook(pContext);
		List _List = _BusinessAccountBook.GetAccountBook("");
		SetList(_List);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder _Holder;
		
		if (convertView == null) {
			convertView = GetLayoutInflater().inflate(R.layout.account_book_list_item, null);
			_Holder = new Holder();
			_Holder.ivIcon = (ImageView)convertView.findViewById(R.id.ivAccountBookIcon);
			_Holder.tvName = (TextView)convertView.findViewById(R.id.tvAccountBookName);
			_Holder.tvTotal = (TextView)convertView.findViewById(R.id.tvTotal);
			_Holder.tvMoney = (TextView)convertView.findViewById(R.id.tvMoney);
			convertView.setTag(_Holder);
		}
		else {
			_Holder = (Holder) convertView.getTag();
		}
		
		ModelAccountBook _ModelAccountBook = (ModelAccountBook)getItem(position);
		if(_ModelAccountBook.GetIsDefault() == 1)
		{
			_Holder.ivIcon.setImageResource(R.mipmap.account_book_default);
		}
		else {
			_Holder.ivIcon.setImageResource(R.mipmap.account_book_big_icon);
		}
		_Holder.tvName.setText(_ModelAccountBook.GetAccountBookName());
		return convertView;
	}

}
