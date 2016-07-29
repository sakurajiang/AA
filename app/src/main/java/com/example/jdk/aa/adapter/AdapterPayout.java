package com.example.jdk.aa.adapter;

import java.util.List;


import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jdk.aa.R;
import com.example.jdk.aa.adapter.base.AdapterBase;
import com.example.jdk.aa.business.BusinessPayout;
import com.example.jdk.aa.business.BusinessUser;
import com.example.jdk.aa.model.ModelPayout;
import com.example.jdk.aa.utility.DateTools;

public class AdapterPayout extends AdapterBase {

	private class Holder
	{
		ImageView Icon;
		TextView Name;
		TextView Total;
		TextView PayoutUserAndPayoutType;
		View RelativeLayoutDate;
	}
	
	private BusinessPayout m_BusinessPayout;
	private int mAccountBookID;
	
	public AdapterPayout(Context p_Context,int p_AccountBookID)
	{
		this(p_Context,null);
		m_BusinessPayout = new BusinessPayout(p_Context);
		mAccountBookID = p_AccountBookID;
		List _List = m_BusinessPayout.GetPayoutByAccountBookID(p_AccountBookID);
		SetList(_List);
	}
	
	public AdapterPayout(Context p_Context, List p_List) {
		super(p_Context, p_List);
	}

	@Override
	public View getView(int p_Position, View p_ConvertView, ViewGroup p_Parent) {
		Holder _Holder;
		
		if (p_ConvertView == null) {
			p_ConvertView = GetLayoutInflater().inflate(R.layout.payout_list_item, null);
			_Holder = new Holder();
			_Holder.Icon = (ImageView)p_ConvertView.findViewById(R.id.PayoutIcon);
			_Holder.Name = (TextView)p_ConvertView.findViewById(R.id.PayoutName);
			_Holder.Total = (TextView)p_ConvertView.findViewById(R.id.Total);
			_Holder.PayoutUserAndPayoutType = (TextView)p_ConvertView.findViewById(R.id.PayoutUserAndPayoutType);
			_Holder.RelativeLayoutDate = (View)p_ConvertView.findViewById(R.id.RelativeLayoutDate);
			p_ConvertView.setTag(_Holder);
		}
		else {
			_Holder = (Holder) p_ConvertView.getTag();
		}

		_Holder.RelativeLayoutDate.setVisibility(View.GONE);
		ModelPayout _ModelPayout = (ModelPayout)getItem(p_Position);
		String _PayoutDate = DateTools.getFormatShortTime(_ModelPayout.GetPayoutDate());
		Boolean _IsShow = false;
		if(p_Position > 0)
		{
			ModelPayout _ModelPayoutLast = (ModelPayout)getItem(p_Position - 1);
			String _PayoutDateLast = DateTools.getFormatShortTime(_ModelPayoutLast.GetPayoutDate());
			_IsShow = !_PayoutDate.equals(_PayoutDateLast);
		}
		if(_IsShow || p_Position == 0)
		{
			_Holder.RelativeLayoutDate.setVisibility(View.VISIBLE);
			String _Message = m_BusinessPayout.GetPayoutTotalMessage(_PayoutDate,mAccountBookID); 
			((TextView)_Holder.RelativeLayoutDate.findViewById(R.id.tvPayoutDate)).setText(_PayoutDate);
			((TextView)_Holder.RelativeLayoutDate.findViewById(R.id.tvTotal)).setText(_Message);
		}
		
		_Holder.Icon.setImageResource(R.mipmap.payout_small_icon);
		_Holder.Total.setText(_ModelPayout.GetAmount().toString());
		_Holder.Name.setText(_ModelPayout.GetCategoryName());
		
		BusinessUser _BusinessUser = new BusinessUser(GetContext());
		String _UserNameString = _BusinessUser.GetUserNameByUserID(_ModelPayout.GetPayoutUserID());
		_Holder.PayoutUserAndPayoutType.setText(_UserNameString + " " + _ModelPayout.GetPayoutType());
		
		return p_ConvertView;
	}
	
}
