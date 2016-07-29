package com.example.jdk.aa.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



import android.R.integer;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.accessibility.AccessibilityEventSource;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Spinner;

import com.example.jdk.aa.R;
import com.example.jdk.aa.activity.base.ActivityFrame;
import com.example.jdk.aa.adapter.AdapterAccountBookSelect;
import com.example.jdk.aa.adapter.AdapterCategory;
import com.example.jdk.aa.adapter.AdapterUser;
import com.example.jdk.aa.business.BusinessAccountBook;
import com.example.jdk.aa.business.BusinessCategory;
import com.example.jdk.aa.business.BusinessPayout;
import com.example.jdk.aa.business.BusinessUser;
import com.example.jdk.aa.controls.NumberDialog;
import com.example.jdk.aa.model.ModelAccountBook;
import com.example.jdk.aa.model.ModelCategory;
import com.example.jdk.aa.model.ModelPayout;
import com.example.jdk.aa.model.ModelUser;
import com.example.jdk.aa.utility.DateTools;
import com.example.jdk.aa.utility.RegexTools;

public class ActivityPayoutAddOrEdit extends ActivityFrame
	implements View.OnClickListener,NumberDialog.OnNumberDialogListener
{
	
	private Button btnSave;
	private Button btnCancel;
	
	private ModelPayout mModelPayout;
	private BusinessPayout mBusinessPayout;
	private Integer mAccountBookID;
	private Integer mCategoryID;
	private String mPayoutUserID;
	private String mPayoutTypeArr[];
		
	private EditText etAccountBookName;
	private EditText etAmount;
	private AutoCompleteTextView actvCategoryName;
	private EditText etPayoutDate;
	private EditText etPayoutType;
	private EditText etPayoutUser;
	private EditText etComment;
	private Button btnSelectCategory;
	private Button btnSelectUser;
	private Button btnSelectAccountBook;
	private Button btnSelectAmount;
	private Button btnSelectPayoutDate;
	private Button btnSelectPayoutType;
	
	private BusinessAccountBook mBusinessAccountBook;
	private BusinessCategory mBusinessCategory;
	private ModelAccountBook mModelAccountBook;
	private BusinessUser mBusinessUser;
	private List<RelativeLayout> mItemColor;
	private List<ModelUser> mUserSelectedList;

	@Override
	public void onClick(View v) {
		int _ID = v.getId();
		switch (_ID) {
		case R.id.btnSelectAccountBook:
			ShowAccountBookSelectDialog();
			break;
		case R.id.btnSelectAmount:
			(new NumberDialog(this)).show();
			break;
		case R.id.btnSelectCategory:
			ShowCategorySelectDialog();
			break;
		case R.id.btnSelectPayoutDate:
			Calendar _Calendar = Calendar.getInstance();
			ShowDateSelectDialog(_Calendar.get(Calendar.YEAR), _Calendar.get(Calendar.MONTH), _Calendar.get(Calendar.DATE));
			break;
		case R.id.btnSelectPayoutType:
			ShowPayoutTypeSelectDialog();
			break;
		case R.id.btnSelectUser:
			ShowUserSelectDialog(etPayoutType.getText().toString());
			break;
		case R.id.btnSave:
			AddOrEditPayout();
			break;
		case R.id.btnCancel:
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AppendMainBody(R.layout.payout_add_or_edit);
		RemoveBottomBox();
		InitView();
		InitVariable();
		BindData();
		SetTitle();
		InitListeners();
	}
	
	private void SetTitle() {
		String _Titel;
		if(mModelPayout == null)
		{
			_Titel = getString(R.string.ActivityTitlePayoutAddOrEdit, new Object[]{getString(R.string.TitleAdd)});
		}
		else {
			_Titel = getString(R.string.ActivityTitlePayoutAddOrEdit, new Object[]{getString(R.string.TitleEdit)});
			InitData(mModelPayout);
		}
		
		SetTopBarTitle(_Titel);
	}

	protected void InitView() {
		btnSave = (Button)findViewById(R.id.btnSave);
		btnCancel = (Button)findViewById(R.id.btnCancel);
		btnSelectAccountBook = (Button)findViewById(R.id.btnSelectAccountBook);
		btnSelectAmount = (Button)findViewById(R.id.btnSelectAmount);
		btnSelectCategory = (Button)findViewById(R.id.btnSelectCategory);
		btnSelectPayoutDate = (Button)findViewById(R.id.btnSelectPayoutDate);
		btnSelectPayoutType = (Button)findViewById(R.id.btnSelectPayoutType);
		btnSelectUser = (Button)findViewById(R.id.btnSelectUser);
		etAccountBookName = (EditText) findViewById(R.id.etAccountBookName);
		etPayoutDate = (EditText) findViewById(R.id.etPayoutDate);
		etPayoutType = (EditText) findViewById(R.id.etPayoutType);
		etAmount = (EditText) findViewById(R.id.etAmount);
		etAccountBookName = (EditText) findViewById(R.id.etAccountBookName);
		actvCategoryName = (AutoCompleteTextView) findViewById(R.id.actvCategoryName);
		etPayoutUser = (EditText) findViewById(R.id.etPayoutUser);
		etComment = (EditText) findViewById(R.id.etComment);
	}

	protected void InitListeners() {
		btnSave.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnSelectAmount.setOnClickListener(this);
		btnSelectCategory.setOnClickListener(this);
		btnSelectPayoutDate.setOnClickListener(this);
		btnSelectPayoutType.setOnClickListener(this);
		btnSelectUser.setOnClickListener(this);
		actvCategoryName.setOnItemClickListener(new OnAutoCompleteTextViewItemClickListener());
		btnSelectAccountBook.setOnClickListener(this);
	}
	

	protected void InitVariable() {
		mBusinessPayout = new BusinessPayout(this);
		mModelPayout = (ModelPayout) getIntent().getSerializableExtra("ModelPayout");
		mBusinessAccountBook = new BusinessAccountBook(this);
		mBusinessCategory = new BusinessCategory(this);
		mModelAccountBook = mBusinessAccountBook.GetDefaultModelAccountBook();
		mBusinessUser = new BusinessUser(this);
	}

	protected void BindData()
	{
		
		mAccountBookID = mModelAccountBook.GetAccountBookID();
		etAccountBookName.setText(mModelAccountBook.GetAccountBookName());
		actvCategoryName.setAdapter(mBusinessCategory.GetAllCategoryArrayAdapter());
		etPayoutDate.setText(DateTools.getFormatDateTime(new Date(), "yyyy-MM-dd"));
		mPayoutTypeArr = getResources().getStringArray(R.array.PayoutType);
		etPayoutType.setText(mPayoutTypeArr[0]);
	}
	
	private void InitData(ModelPayout p_ModelPayout)
	{
		etAccountBookName.setText(p_ModelPayout.GetAccountBookName());
		mAccountBookID = p_ModelPayout.GetAccountBookID();
		etAmount.setText(p_ModelPayout.GetAmount().toString());
		actvCategoryName.setText(p_ModelPayout.GetCategoryName());
		mCategoryID = p_ModelPayout.GetCategoryID();
		etPayoutDate.setText(DateTools.getFormatDateTime(p_ModelPayout.GetPayoutDate(), "yyyy-MM-dd"));
		etPayoutType.setText(p_ModelPayout.GetPayoutType());
		
		BusinessUser _BusinessUser = new BusinessUser(this);
		String _UserNameString = _BusinessUser.GetUserNameByUserID(p_ModelPayout.GetPayoutUserID());
		
		etPayoutUser.setText(_UserNameString);
		mPayoutUserID = p_ModelPayout.GetPayoutUserID();
		etComment.setText(p_ModelPayout.GetComment());
	}

	private void AddOrEditPayout() {
		Boolean _CheckResult = CheckData();
		if(_CheckResult == false)
		{
			return;
		}
		
		if(mModelPayout == null)
		{
			mModelPayout = new ModelPayout();
		}
		mModelPayout.SetAccountBookID(mAccountBookID);
		mModelPayout.SetCategoryID(mCategoryID);
		mModelPayout.SetAmount(new BigDecimal(etAmount.getText().toString().trim()));
		mModelPayout.SetPayoutDate(DateTools.getDate(etPayoutDate.getText().toString().trim(), "yyyy-MM-dd"));
		mModelPayout.SetPayoutType(etPayoutType.getText().toString().trim());
		mModelPayout.SetPayoutUserID(mPayoutUserID);
		mModelPayout.SetComment(etComment.getText().toString().trim());
		
		Boolean _Result = false;
		
		if(mModelPayout.GetPayoutID() == 0)
		{
			_Result = mBusinessPayout.InsertPayout(mModelPayout);
		}
		else {
			_Result = mBusinessPayout.UpdatePayoutByPayoutID(mModelPayout);
		}

		if(_Result)
		{
			Toast.makeText(getApplicationContext(), getString(R.string.TipsAddSucceed), Toast.LENGTH_SHORT).show();
			finish();
		}
		else {
			Toast.makeText(getApplicationContext(), getString(R.string.TipsAddFail), Toast.LENGTH_SHORT).show();
		}
	}
	
	private Boolean CheckData() {
		Boolean _CheckResult = RegexTools.IsMoney(etAmount.getText().toString().trim());
		if(_CheckResult == false) 
		{
			etAmount.requestFocus();
//			etAmount.setFocusable(false);
			Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextMoney), 1).show();
			return _CheckResult;
		}
		
		_CheckResult = RegexTools.IsNull(mCategoryID);
		if(_CheckResult == false) 
		{
			btnSelectCategory.setFocusable(true);
			btnSelectCategory.setFocusableInTouchMode(true);
			btnSelectCategory.requestFocus();
			Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextCategoryIsNull), Toast.LENGTH_SHORT).show();
			return _CheckResult;
		}
		
		if(mPayoutUserID == null)
		{
			btnSelectUser.setFocusable(true);
			btnSelectUser.setFocusableInTouchMode(true);
			btnSelectUser.requestFocus();
			Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextPayoutUserIsNull), Toast.LENGTH_SHORT).show();
			return false;
		}
		
		String _PayoutType = etPayoutType.getText().toString();
		if(_PayoutType.equals(mPayoutTypeArr[0]) || _PayoutType.equals(mPayoutTypeArr[1]))
		{
			if(mPayoutUserID.split(",").length <= 1)
			{
				btnSelectUser.setFocusable(true);
				btnSelectUser.setFocusableInTouchMode(true);
				btnSelectUser.requestFocus();
				Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextPayoutUser), Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		else {
			if(mPayoutUserID.equals(""))
			{
				btnSelectUser.setFocusable(true);
				btnSelectUser.setFocusableInTouchMode(true);
				btnSelectUser.requestFocus();
				Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextPayoutUser2), Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public void SetNumberFinish(BigDecimal p_Number) {
		((EditText)findViewById(R.id.etAmount)).setText(p_Number.toString());
	}

	private class OnAutoCompleteTextViewItemClickListener
			implements AdapterView.OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView p_AdapterView, View arg1, int p_Postion,
				long arg3) {
			ModelCategory _ModelCategory = (ModelCategory)p_AdapterView.getAdapter().getItem(p_Postion);
			mCategoryID = _ModelCategory.GetCategoryID();
		}

	}
	
	private void ShowAccountBookSelectDialog()
	{
		AlertDialog.Builder _Builder = new AlertDialog.Builder(this);
		View _View = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
		ListView _ListView = (ListView)_View.findViewById(R.id.ListViewSelect);
		AdapterAccountBookSelect _AdapterAccountBookSelect = new AdapterAccountBookSelect(this);
		_ListView.setAdapter(_AdapterAccountBookSelect);

		_Builder.setTitle(R.string.ButtonTextSelectAccountBook);
		_Builder.setNegativeButton(R.string.ButtonTextBack, null);
		_Builder.setView(_View);
		AlertDialog _AlertDialog = _Builder.create();
		_AlertDialog.show();
		_ListView.setOnItemClickListener(new OnAccountBookItemClickListener(_AlertDialog));
	}
	
	private class OnAccountBookItemClickListener implements AdapterView.OnItemClickListener
	{
		private AlertDialog m_AlertDialog;
		public OnAccountBookItemClickListener(AlertDialog p_AlertDialog)
		{
			m_AlertDialog = p_AlertDialog;
		}
		@Override
		public void onItemClick(AdapterView p_AdapterView, View arg1, int p_Position,
				long arg3) {
			ModelAccountBook _ModelAccountBook = (ModelAccountBook)((Adapter)p_AdapterView.getAdapter()).getItem(p_Position);
			etAccountBookName.setText(_ModelAccountBook.GetAccountBookName());
			mAccountBookID = _ModelAccountBook.GetAccountBookID();
			m_AlertDialog.dismiss();
		}
	}
	
	private void ShowCategorySelectDialog()
	{
		AlertDialog.Builder _Builder = new AlertDialog.Builder(this);
		View _View = LayoutInflater.from(this).inflate(R.layout.category_select_list, null);
		ExpandableListView _ExpandableListView = (ExpandableListView)_View.findViewById(R.id.ExpandableListViewCategory);
		AdapterCategory _AdapterCategorySelect = new AdapterCategory(this);
		_ExpandableListView.setAdapter(_AdapterCategorySelect);

		_Builder.setIcon(R.mipmap.category_small_icon);
		_Builder.setTitle(R.string.ButtonTextSelectCategory);
		_Builder.setNegativeButton(R.string.ButtonTextBack, null);
		_Builder.setView(_View);
		AlertDialog _AlertDialog = _Builder.create();
		_AlertDialog.show();
		_ExpandableListView.setOnGroupClickListener(new OnCategoryGroupItemClickListener(_AlertDialog,_AdapterCategorySelect));
		_ExpandableListView.setOnChildClickListener(new OnCategoryChildItemClickListener(_AlertDialog,_AdapterCategorySelect));
	}
	
	private class OnCategoryGroupItemClickListener implements OnGroupClickListener
	{
		private AlertDialog m_AlertDialog;
		private AdapterCategory m_AdapterCategory;
		
		public OnCategoryGroupItemClickListener(AlertDialog p_AlertDialog,AdapterCategory p_AdapterCategory)
		{
			m_AlertDialog = p_AlertDialog;
			m_AdapterCategory = p_AdapterCategory;
		}
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			int _Count = m_AdapterCategory.getChildrenCount(groupPosition);
			if(_Count == 0)
			{
				ModelCategory _ModelCategory = (ModelCategory)m_AdapterCategory.getGroup(groupPosition);
				actvCategoryName.setText(_ModelCategory.GetCategoryName());
				mCategoryID = _ModelCategory.GetCategoryID();
				m_AlertDialog.dismiss();
			}
			return false;
		}
	
	}
	
	private class OnCategoryChildItemClickListener implements OnChildClickListener
	{
		private AlertDialog m_AlertDialog;
		private AdapterCategory m_AdapterCategory;
		
		public OnCategoryChildItemClickListener(AlertDialog p_AlertDialog,AdapterCategory p_AdapterCategory)
		{
			m_AlertDialog = p_AlertDialog;
			m_AdapterCategory = p_AdapterCategory;
		}
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			ModelCategory _ModelCategory = (ModelCategory)m_AdapterCategory.getChild(groupPosition, childPosition);
			actvCategoryName.setText(_ModelCategory.GetCategoryName());
			mCategoryID = _ModelCategory.GetCategoryID();
			m_AlertDialog.dismiss();
			return false;
		}
	
	}
	
	private void ShowDateSelectDialog(int p_Year,int p_Month,int p_Day)
	{
		(new DatePickerDialog(this, new OnDateSelectedListener(), p_Year, p_Month, p_Day)).show();
	}
	
	private class OnDateSelectedListener implements OnDateSetListener
	{
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			Date _Date = new Date(year-1900, monthOfYear, dayOfMonth);
			etPayoutDate.setText(DateTools.getFormatDateTime(_Date,"yyyy-MM-dd"));
		}
	}

	private void ShowPayoutTypeSelectDialog()
	{
		AlertDialog.Builder _Builder = new AlertDialog.Builder(this);
		View _View = LayoutInflater.from(this).inflate(R.layout.payout_type_select_list, null);
		ListView _ListView = (ListView)_View.findViewById(R.id.ListViewPayoutType);

		_Builder.setTitle(R.string.ButtonTextSelectPayoutType);
		_Builder.setNegativeButton(R.string.ButtonTextBack, null);
		_Builder.setView(_View);
		AlertDialog _AlertDialog = _Builder.create();
		_AlertDialog.show();
		_ListView.setOnItemClickListener(new OnPayoutTypeItemClickListener(_AlertDialog));
	}
	
	private class OnPayoutTypeItemClickListener implements AdapterView.OnItemClickListener
	{
		private AlertDialog m_AlertDialog;
		public OnPayoutTypeItemClickListener(AlertDialog p_AlertDialog)
		{
			m_AlertDialog = p_AlertDialog;
		}
		@Override
		public void onItemClick(AdapterView p_AdapterView, View arg1, int p_Position,
				long arg3) {
			String _PayoutType = (String)p_AdapterView.getAdapter().getItem(p_Position);
			etPayoutType.setText(_PayoutType);
			etPayoutUser.setText("");
			mPayoutUserID = "";
			m_AlertDialog.dismiss();
		}
	}
	
	private void ShowUserSelectDialog(String p_PayoutType)
	{
		AlertDialog.Builder _Builder = new AlertDialog.Builder(this);
		View _View = LayoutInflater.from(this).inflate(R.layout.user, null);
		LinearLayout _LinearLayout = (LinearLayout)_View.findViewById(R.id.LinearLayoutMain);
		_LinearLayout.setBackgroundResource(R.drawable.blue);
		ListView _ListView = (ListView)_View.findViewById(R.id.lvUserList);
		AdapterUser _AdapterUser = new AdapterUser(this);
		_ListView.setAdapter(_AdapterUser);

		_Builder.setIcon(R.mipmap.user_small_icon);
		_Builder.setTitle(R.string.ButtonTextSelectUser);
		_Builder.setNegativeButton(R.string.ButtonTextBack, new OnSelectUserBack());
		_Builder.setView(_View);
		AlertDialog _AlertDialog = _Builder.create();
		_AlertDialog.show();
		_ListView.setOnItemClickListener(new OnUserItemClickListener(_AlertDialog,p_PayoutType));
	}
	
	private class OnSelectUserBack implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface dialog, int which) {
			etPayoutUser.setText("");
			String _Name = "";
			mPayoutUserID = "";
			if(mUserSelectedList != null)
			{
				for(int i=0;i<mUserSelectedList.size();i++)
				{
					_Name += mUserSelectedList.get(i).getUserName() + ",";
					mPayoutUserID += mUserSelectedList.get(i).getUserID() + ",";
				}
				etPayoutUser.setText(_Name);
			}
			
			mItemColor = null;
			mUserSelectedList = null;
		}
	}
	
	private class OnUserItemClickListener implements AdapterView.OnItemClickListener
	{
		private AlertDialog m_AlertDialog;
		private String m_PayoutType;
		
		public OnUserItemClickListener(AlertDialog p_AlertDialog,String p_PayoutType)
		{
			m_AlertDialog = p_AlertDialog;
			m_PayoutType = p_PayoutType;
		}
		@Override
		public void onItemClick(AdapterView p_AdapterView, View arg1, int p_Position,
				long arg3) {
//			ModelUser _ModelUser = (ModelUser)((Adapter)p_AdapterView.getAdapter()).getItem(p_Position);
//			((OnListSelectListener)ActivityBase.this).OnSelected(_ModelUser,"User");
//			m_AlertDialog.dismiss();
			
			String _PayoutTypeArr[] = getResources().getStringArray(R.array.PayoutType);
			ModelUser _ModelUser = (ModelUser)((Adapter)p_AdapterView.getAdapter()).getItem(p_Position);
			if(m_PayoutType.equals(_PayoutTypeArr[0]) || m_PayoutType.equals(_PayoutTypeArr[1]))
			{
				RelativeLayout _Main = (RelativeLayout)arg1.findViewById(R.id.RelativeLayoutMain);
				
				
				if(mItemColor == null && mUserSelectedList == null)
				{
					mItemColor = new ArrayList<RelativeLayout>();
					mUserSelectedList = new ArrayList<ModelUser>();
				}
				
				if(mItemColor.contains(_Main))
				{
					_Main.setBackgroundResource(R.drawable.blue);
					mItemColor.remove(_Main);
					mUserSelectedList.remove(_ModelUser);
				}
				else {
					_Main.setBackgroundResource(R.drawable.red);
					mItemColor.add(_Main);
					mUserSelectedList.add(_ModelUser);
				}
				
//				if(m_PayoutType.equals(_PayoutTypeArr[1]))
//				{
//					if(m_UserSelectedList.size() == 2)
//					{
//						((OnListSelectListener)ActivityBase.this).OnSelected(m_UserSelectedList,"User");
//						m_AlertDialog.dismiss();
//					}
//				}
				return;
			}
			
			if(m_PayoutType.equals(_PayoutTypeArr[2]))
			{
				mUserSelectedList = new ArrayList<ModelUser>();
				mUserSelectedList.add(_ModelUser);
				etPayoutUser.setText("");
				String _Name = "";
				mPayoutUserID = "";
				for(int i=0;i<mUserSelectedList.size();i++)
				{
					_Name += mUserSelectedList.get(i).getUserName() + ",";
					mPayoutUserID += mUserSelectedList.get(i).getUserID() + ",";
				}
				etPayoutUser.setText(_Name);
				
				mItemColor = null;
				mUserSelectedList = null;
				m_AlertDialog.dismiss();
			}
		}
	}
}
