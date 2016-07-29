package com.example.jdk.aa.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ListAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.example.jdk.aa.R;
import com.example.jdk.aa.activity.base.ActivityFrame;
import com.example.jdk.aa.adapter.AdapterCategory;
import com.example.jdk.aa.business.BusinessCategory;
import com.example.jdk.aa.controls.SlideMenuItem;
import com.example.jdk.aa.controls.SlideMenuView;
import com.example.jdk.aa.model.ModelCategory;
import com.example.jdk.aa.model.ModelCategoryTotal;

public class ActivityCategory extends ActivityFrame implements SlideMenuView.OnSlideMenuListener {
	private ExpandableListView elvCategoryList;
	private ModelCategory mSelectModelCategory;
	private BusinessCategory mBusinessCategory;
	private AdapterCategory mAdapterCategory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AppendMainBody(R.layout.category);
		InitVariable();
		InitView();
		InitListeners();
		BindData();
		CreateSlideMenu(R.array.SlideMenuCategory);
	}
	
	private void SetTitle() {
		int _Count = mBusinessCategory.GetNotHideCount();
		String _Titel = getString(R.string.ActivityTitleCategory, new Object[]{_Count});
		SetTopBarTitle(_Titel);
	}

	protected void InitView() {
		elvCategoryList = (ExpandableListView) findViewById(R.id.ExpandableListViewCategory);
	}

	protected void InitListeners() {
		registerForContextMenu(elvCategoryList);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu p_ContextMenu, View p_View, ContextMenuInfo p_MenuInfo) {
		ExpandableListContextMenuInfo _ExpandableListContextMenuInfo = (ExpandableListContextMenuInfo)p_MenuInfo;
		long _Position = _ExpandableListContextMenuInfo.packedPosition;
		int _Type = ExpandableListView.getPackedPositionType(_Position);
		int _GroupPosition = ExpandableListView.getPackedPositionGroup(_Position);

		switch (_Type) {
		case ExpandableListView.PACKED_POSITION_TYPE_GROUP:
			mSelectModelCategory = (ModelCategory)mAdapterCategory.getGroup(_GroupPosition);
			break;
		case ExpandableListView.PACKED_POSITION_TYPE_CHILD:
			int _ChildPosition = ExpandableListView.getPackedPositionChild(_Position);
			mSelectModelCategory = (ModelCategory)mAdapterCategory.getChild(_GroupPosition, _ChildPosition);
			break;
		default:
			break;
		}
		
		p_ContextMenu.setHeaderIcon(R.mipmap.category_small_icon);
		if(mSelectModelCategory != null)
		{
			p_ContextMenu.setHeaderTitle(mSelectModelCategory.GetCategoryName());
		}
		CreateMenu(p_ContextMenu);
		p_ContextMenu.add(0, 3, 3, R.string.ActivityCategoryTotal);
		if(mAdapterCategory.GetChildCountOfGroup(_GroupPosition) != 0 && mSelectModelCategory.GetParentID() == 0)
		{
			p_ContextMenu.findItem(2).setEnabled(false);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem p_Item) {
		Intent _Intent;
		switch (p_Item.getItemId()) {
		case 1:
			_Intent = new Intent(this, ActivityCategoryAddOrEdit.class);
			_Intent.putExtra("ModelCategory", mSelectModelCategory);
			this.startActivityForResult(_Intent, 1);
			break;
		case 2:
			Delete(mSelectModelCategory);
			break;
		case 3:
			List<ModelCategoryTotal> _List = mBusinessCategory.CategoryTotalByParentID(mSelectModelCategory.GetCategoryID());
			_Intent = new Intent();
			_Intent.putExtra("Total", (Serializable)_List);
			_Intent.setClass(this, ActivityCategoryChart.class);
			startActivity(_Intent);
			break;
		default:
			break;
		}
		
		return super.onContextItemSelected(p_Item);
	}

	protected void InitVariable() {
		mBusinessCategory = new BusinessCategory(ActivityCategory.this);
	}

	protected void BindData()
	{
		mAdapterCategory = new AdapterCategory(this);
		elvCategoryList.setAdapter(mAdapterCategory);
		SetTitle();
	}

	@Override
	public void onSlideMenuItemClick(View p_View, SlideMenuItem p_SlideMenuItem) {
		SlideMenuToggle();
		if (p_SlideMenuItem.getItemID() == 0) {
			Intent _Intent = new Intent(this, ActivityCategoryAddOrEdit.class);
			this.startActivityForResult(_Intent, 1);
			
			return;
		}
		
		if (p_SlideMenuItem.getItemID() == 1) {
			List<ModelCategoryTotal> _List = mBusinessCategory.CategoryTotalByRootCategory();
			Intent _Intent = new Intent();
			_Intent.putExtra("Total", (Serializable)_List);
			_Intent.setClass(this, ActivityCategoryChart.class);
			startActivity(_Intent);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		BindData();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void Delete(ModelCategory p_ModelCategory)
	{
		Object _Object[] = {p_ModelCategory.GetCategoryName()}; 	
		String _Message = getString(R.string.DialogMessageCategoryDelete,_Object);
		ShowAlertDialog(R.string.DialogTitleDelete,_Message,new OnDeleteClickListener(p_ModelCategory));
	}
	
	private class OnDeleteClickListener implements DialogInterface.OnClickListener {
		private ModelCategory m_ModelCategory;
		public OnDeleteClickListener(ModelCategory p_ModelCategory)
		{
			m_ModelCategory = p_ModelCategory;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			Boolean _Result;
				_Result = mBusinessCategory.HideCategoryByByPath(m_ModelCategory.GetPath());
				if(_Result == true)
				{
					BindData();
				}
				else {
					Toast.makeText(getApplicationContext(), R.string.TipsDeleteFail, Toast.LENGTH_SHORT).show();
				}
		}
		
	}
}
