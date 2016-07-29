package com.example.jdk.aa.database.sqlitedal;

import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jdk.aa.R;
import com.example.jdk.aa.database.base.SQLiteDALBase;
import com.example.jdk.aa.model.ModelUser;
import com.example.jdk.aa.utility.DateTools;


public class SQLiteDALUser extends SQLiteDALBase {

	public SQLiteDALUser(Context pContext) {
		super(pContext);
	}

	public boolean InsertUser(ModelUser pModelUser) {
		ContentValues _ContentValues = CreateParms(pModelUser);
		long _NewID =  GetDataBase().insert(GetTableNameAndPK()[0], null, _ContentValues);
		pModelUser.setUserID((int)_NewID);
		
		return _NewID > 0;
	}
	
	public boolean DeleteUser(String pCondition) {
		return Delete(GetTableNameAndPK()[0],pCondition);
	}
	
	public boolean UpdateUser(String pCondition,ModelUser pModelUser) {
		ContentValues _ContentValues = CreateParms(pModelUser);
		return GetDataBase().update(GetTableNameAndPK()[0], _ContentValues, pCondition, null) > 0;
	}
	
	public Boolean UpdateUser(String p_Condition,ContentValues pContentValues)
	{
		return GetDataBase().update("User", pContentValues, p_Condition, null) > 0;
	}
	
	public List<ModelUser> GetUser(String pCondition)
	{
		String _SqlText = "Select * From User Where 1=1 " + pCondition;
		return GetList(_SqlText);
	}
	
	@Override
	protected String[] GetTableNameAndPK() {
		return new String[]{"User","UserID"};
	}

	@Override
	protected Object FindModel(Cursor pCursor) {
		ModelUser _ModelUser = new ModelUser();
		_ModelUser.setUserID(pCursor.getInt(pCursor.getColumnIndex("UserID")));
		_ModelUser.setUserName(pCursor.getString(pCursor.getColumnIndex("UserName")));
		Date _CreateDate = DateTools.getDate(pCursor.getString(pCursor.getColumnIndex("CreateDate")), "yyyy-MM-dd HH:mm:ss");
		_ModelUser.setCreateDate(_CreateDate);
		_ModelUser.setState((pCursor.getInt(pCursor.getColumnIndex("State"))));
		
		return _ModelUser;
	}

	private void InitDefaultData(SQLiteDatabase pDatabase)
	{
		ModelUser _ModelUser = new ModelUser();
		String _UserName[] = GetContext().getResources().getStringArray(R.array.InitDefaultUserName);
		
		for (int i = 0; i < _UserName.length; i++) {
			_ModelUser.setUserName(_UserName[i]);
			ContentValues _ContentValues = CreateParms(_ModelUser);
			
			pDatabase.insert(GetTableNameAndPK()[0], null, _ContentValues);
		}
	}

	@Override
	public void OnCreate(SQLiteDatabase p_DataBase) {
		StringBuilder s_CreateTableScript = new StringBuilder();
		
		s_CreateTableScript.append("		Create  TABLE User(");
		s_CreateTableScript.append("				[UserID] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		s_CreateTableScript.append("				,[UserName] varchar(10) NOT NULL");
		s_CreateTableScript.append("				,[CreateDate] datetime NOT NULL");
		s_CreateTableScript.append("				,[State] integer NOT NULL");
		s_CreateTableScript.append("				)");
		
		p_DataBase.execSQL(s_CreateTableScript.toString());
		
		InitDefaultData(p_DataBase);
	}

	@Override
	public void OnUpgrade(SQLiteDatabase p_DataBase) {
		
	}

	public ContentValues CreateParms(ModelUser p_Info)
	{
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put("UserName", p_Info.getUserName());
		_ContentValues.put("CreateDate",DateTools.getFormatDateTime(p_Info.getCreateDate(),"yyyy-MM-dd HH:mm:ss"));
		_ContentValues.put("State",p_Info.getState());
		return _ContentValues;
	}
}
