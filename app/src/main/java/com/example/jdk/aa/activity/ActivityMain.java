package com.example.jdk.aa.activity;

import java.util.Date;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.jdk.aa.R;
import com.example.jdk.aa.activity.base.ActivityFrame;
import com.example.jdk.aa.adapter.AdapterAppGrid;
import com.example.jdk.aa.business.BusinessDataBackup;
import com.example.jdk.aa.controls.SlideMenuItem;
import com.example.jdk.aa.controls.SlideMenuView;
import com.example.jdk.aa.service.ServiceDatabaseBackup;

public class ActivityMain extends ActivityFrame implements SlideMenuView.OnSlideMenuListener,OnClickListener {
	
	private GridView gvAppGrid;
	public BusinessDataBackup mBusinessDataBackup;
	private AdapterAppGrid mAdapterAppGrid;
	private AlertDialog mDatabaseBackupDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HideTitleBackButton();
        AppendMainBody(R.layout.main_body);
        InitVariable();
        InitView();
        InitListeners();
        BindData();
        CreateSlideMenu(R.array.SlideMenuActivityMain);
        StartService();
    }
    
    private void StartService() {
		Intent _Intent = new Intent(this, ServiceDatabaseBackup.class);
		startService(_Intent);
	}
    
    public void InitVariable()
    {
    	mAdapterAppGrid = new AdapterAppGrid(this);
    	mBusinessDataBackup = new BusinessDataBackup(this);
    }
    
    public void InitView()
    {
    	gvAppGrid = (GridView) findViewById(R.id.gvAppGrid);
    }
    
    public void InitListeners()
    {
    	gvAppGrid.setOnItemClickListener(new onAppGridItemClickListener());
    }
    
    private class onAppGridItemClickListener implements OnItemClickListener
    {
    	
		@Override
		public void onItemClick(AdapterView p_Adapter, View p_View, int p_Position,long arg3) {
			String _MenuName = (String)p_Adapter.getAdapter().getItem(p_Position);
			if(_MenuName.equals(getString(R.string.appGridTextUserManage)))
			{
				OpenActivity(ActivityUser.class);
				return;
			}
			if(_MenuName.equals(getString(R.string.appGridTextAccountBookManage)))
			{
				OpenActivity(ActivityAccountBook.class);
				return;
			}
			if(_MenuName.equals(getString(R.string.appGridTextCategoryManage)))
			{
				OpenActivity(ActivityCategory.class);
				return;
			}
			if(_MenuName.equals(getString(R.string.appGridTextPayoutAdd)))
			{
				OpenActivity(ActivityPayoutAddOrEdit.class);
				return;
			}
			if(_MenuName.equals(getString(R.string.appGridTextPayoutManage)))
			{
				OpenActivity(ActivityPayout.class);
				return;
			}
			if(_MenuName.equals(getString(R.string.appGridTextStatisticsManage)))
			{
				OpenActivity(ActivityStatistics.class);
				return;
			}
		}
	}
    
    public void BindData()
    {
    	gvAppGrid.setAdapter(mAdapterAppGrid);
    }
    
    @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDatabaseBackup:
			DatabaseBackup();
			break;
		case R.id.btnDatabaseRestore:
			DatabaseRestore();
			break;
		default:
			break;
		}		
	}

	@Override
	public void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem) {
		SlideMenuToggle();
		if (pSlideMenuItem.getItemID() == 0) {
			ShowDatabaseBackupDialog();
		}
	}
	
	private void ShowDatabaseBackupDialog()
	{
		LayoutInflater _LayoutInflater = LayoutInflater.from(this);
		
		View _View = _LayoutInflater.inflate(R.layout.database_backup, null);
		
		Button _btnDatabaseBackup = (Button)_View.findViewById(R.id.btnDatabaseBackup);
		Button _btnDatabaseRestore = (Button)_View.findViewById(R.id.btnDatabaseRestore);
		
		_btnDatabaseBackup.setOnClickListener(this);
		_btnDatabaseRestore.setOnClickListener(this);
		
		String _Title = getString(R.string.DialogTitleDatabaseBackup);
		
		AlertDialog.Builder _Builder = new AlertDialog.Builder(this);
		_Builder.setTitle(_Title);
		_Builder.setView(_View);
		_Builder.setIcon(R.mipmap.database_backup);
		_Builder.setNegativeButton(getString(R.string.ButtonTextBack), null);
		mDatabaseBackupDialog = _Builder.show();
	}
	
	private void DatabaseBackup()
	{
		if(mBusinessDataBackup.DatabaseBackup(new Date()))
		{
			ShowMsg(R.string.DialogMessageDatabaseBackupSucceed);
		}
		else {
			ShowMsg(R.string.DialogMessageDatabaseBackupFail);
		}
		
		mDatabaseBackupDialog.dismiss();
	}
	
	private void DatabaseRestore()
	{
		if(mBusinessDataBackup.DatabaseRestore())
		{
			ShowMsg(R.string.DialogMessageDatabaseRestoreSucceed);
		}
		else {
			ShowMsg(R.string.DialogMessageDatabaseRestoreFail);
		}
		
		mDatabaseBackupDialog.dismiss();
	}
}