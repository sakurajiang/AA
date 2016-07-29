package com.example.jdk.aa.controls;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.example.jdk.aa.R;
import com.example.jdk.aa.adapter.AdapterSlideMenu;

public class SlideMenuView {

	private Activity mActivity;
	private List mMenuList;
	private boolean mIsClosed;
	private RelativeLayout layBottomBox;
	private OnSlideMenuListener mSlideMenuListener;
	
	public interface OnSlideMenuListener
	{
		public abstract void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem);
	}

	/*public SlideMenuView(Activity pActivity,boolean pIsRemove) {
		mActivity = pActivity;
		InitView();
	}*/
	
	public SlideMenuView(Activity pActivity) {
		mActivity = pActivity;
		InitView();
		if (pActivity instanceof OnSlideMenuListener) {
			mSlideMenuListener = (OnSlideMenuListener) pActivity;
			InitVariable();
			InitListeners();
		}
	}

	public void InitVariable() {
		mMenuList = new ArrayList();
		mIsClosed = true;
	}

	public void InitView() {
		layBottomBox = (RelativeLayout) mActivity.findViewById(R.id.IncludeBottom);
	}

	public void InitListeners() {
		layBottomBox.setOnClickListener(new OnSlideMenuClick());
		layBottomBox.setFocusableInTouchMode(true);
		layBottomBox.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_UP) {
					Toggle();
				}
				
				return false;
			}
		});
	}

	private void Open() {
		LayoutParams _LayoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		_LayoutParams.addRule(RelativeLayout.BELOW, R.id.IncludeTitle);
		
		layBottomBox.setLayoutParams(_LayoutParams);
		
		mIsClosed = false;
	}

	private void Close() {
		LayoutParams _LayoutParams = new LayoutParams(LayoutParams.FILL_PARENT, 200);
		_LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		layBottomBox.setLayoutParams(_LayoutParams);
		
		mIsClosed = true;
	}

	public void Toggle() {
		if(mIsClosed)
		{
			Open();
		}
		else {
			Close();
		}
	}
	
	public void RemoveBottomBox()
	{
		RelativeLayout _MainLayout = (RelativeLayout)mActivity.findViewById(R.id.layMainLayout);
		_MainLayout.removeView(layBottomBox);
		layBottomBox = null;
	}

	public void Add(SlideMenuItem pSliderMenuItem) {
		mMenuList.add(pSliderMenuItem);
	}

	public void BindList() {
		AdapterSlideMenu _AdapterSlideMenu = new AdapterSlideMenu(mActivity, mMenuList);
		
		ListView _ListView = (ListView) mActivity.findViewById(R.id.lvSlideList);
		_ListView.setAdapter(_AdapterSlideMenu);
		_ListView.setOnItemClickListener(new OnSlideMenuItemClick());
	}

	private class OnSlideMenuClick implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			Toggle();
		}
		
	}
	
	private class OnSlideMenuItemClick implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> pAdapterView, View pView, int pPosition,
				long arg3) {
			SlideMenuItem _SlideMenuItem = (SlideMenuItem) pAdapterView.getItemAtPosition(pPosition);
			mSlideMenuListener.onSlideMenuItemClick(pView, _SlideMenuItem);
		}
		
	}
}
