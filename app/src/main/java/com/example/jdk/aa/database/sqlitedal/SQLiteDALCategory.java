package com.example.jdk.aa.database.sqlitedal;

import java.util.Date;
import java.util.List;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jdk.aa.R;
import com.example.jdk.aa.database.base.SQLiteDALBase;
import com.example.jdk.aa.model.ModelCategory;
import com.example.jdk.aa.utility.DateTools;

public class SQLiteDALCategory extends SQLiteDALBase {

	public SQLiteDALCategory(Context p_Context) {
		super(p_Context);
	}

	public Boolean InsertCategory(ModelCategory p_Info) {
		ContentValues _ContentValues = CreatParms(p_Info);
		Long p_NewID = GetDataBase().insert("Category", null, _ContentValues);
		p_Info.SetCategoryID(p_NewID.intValue());
		return p_NewID > 0;
	}
	
	public Boolean DeleteCategory(String p_Condition)
	{
		return Delete(GetTableNameAndPK()[0], p_Condition);
	}
	
	public Boolean UpdateCategory(String p_Condition, ModelCategory p_Info)
	{
		ContentValues _ContentValues = CreatParms(p_Info);
		return GetDataBase().update("Category", _ContentValues, p_Condition, null) > 0;
	}
	
	public Boolean UpdateCategory(String p_Condition,ContentValues p_ContentValues)
	{
		return GetDataBase().update("Category", p_ContentValues, p_Condition, null) > 0;
	}
	
	public List<ModelCategory> GetCategory(String p_Condition)
	{
		String _SqlText = "Select * From Category Where  1=1 " + p_Condition;
		return GetList(_SqlText);
	}
	
	protected ModelCategory FindModel(Cursor p_Cursor)
	{
		ModelCategory _ModelCategory = new ModelCategory();
		_ModelCategory.SetCategoryID(p_Cursor.getInt(p_Cursor.getColumnIndex("CategoryID")));
		_ModelCategory.SetCategoryName(p_Cursor.getString(p_Cursor.getColumnIndex("CategoryName")));
		_ModelCategory.SetTypeFlag(p_Cursor.getString(p_Cursor.getColumnIndex("TypeFlag")));
		_ModelCategory.SetParentID(p_Cursor.getInt(p_Cursor.getColumnIndex("ParentID")));
		_ModelCategory.SetPath(p_Cursor.getString(p_Cursor.getColumnIndex("Path")));
		Date _CreateDate = DateTools.getDate(p_Cursor.getString(p_Cursor.getColumnIndex("CreateDate")), "yyyy-MM-dd HH:mm:ss");
		_ModelCategory.SetCreateDate(_CreateDate);
		_ModelCategory.SetState((p_Cursor.getInt(p_Cursor.getColumnIndex("State"))));
		
		return _ModelCategory;
	}
	
	private void InitDefaultData(SQLiteDatabase p_DataBase)
	{
		ModelCategory _ModelCategory = new ModelCategory();
		
		_ModelCategory.SetTypeFlag(GetContext().getString((R.string.PayoutTypeFlag)));
		_ModelCategory.SetPath("");
		_ModelCategory.SetParentID(0);
		String _InitDefaultCategoryNameArr[] = GetContext().getResources().getStringArray(R.array.InitDefaultCategoryName);
		for(int i=0;i<_InitDefaultCategoryNameArr.length;i++)
		{
			_ModelCategory.SetCategoryName(_InitDefaultCategoryNameArr[i]);
			
			ContentValues _ContentValues = CreatParms(_ModelCategory);
			Long _NewID = p_DataBase.insert("Category", null, _ContentValues);
			
			_ModelCategory.SetPath(_NewID.intValue() + ".");
			_ContentValues = CreatParms(_ModelCategory);
			p_DataBase.update("Category", _ContentValues, " CategoryID = " + _NewID.intValue(), null);
		}
	}

	@Override
	public void OnCreate(SQLiteDatabase p_DataBase) {
		StringBuilder s_CreateTableScript = new StringBuilder();
		
		s_CreateTableScript.append("		Create  TABLE Category(");
		s_CreateTableScript.append("				[CategoryID] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		s_CreateTableScript.append("				,[CategoryName] varchar(20) NOT NULL");
		s_CreateTableScript.append("				,[TypeFlag] varchar(20) NOT NULL");
		s_CreateTableScript.append("				,[ParentID] integer NOT NULL");
		s_CreateTableScript.append("				,[Path] text NOT NULL");
		s_CreateTableScript.append("				,[CreateDate] datetime NOT NULL");
		s_CreateTableScript.append("				,[State] integer NOT NULL");
		s_CreateTableScript.append("				)");
		
		p_DataBase.execSQL(s_CreateTableScript.toString());
		InitDefaultData(p_DataBase);
	}

	@Override
	public void OnUpgrade(SQLiteDatabase p_DataBase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String[] GetTableNameAndPK() {
		return new String[]{"Category","CategoryID"};
	}
	
	public ContentValues CreatParms(ModelCategory p_Info) {
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put("CategoryName", p_Info.GetCategoryName());
		_ContentValues.put("TypeFlag", p_Info.GetTypeFlag());
		_ContentValues.put("ParentID", p_Info.GetParentID());
		_ContentValues.put("Path", p_Info.GetPath());
		_ContentValues.put("CreateDate",DateTools.getFormatDateTime(p_Info.GetCreateDate(), "yyyy-MM-dd HH:mm:ss"));
		_ContentValues.put("State",p_Info.GetState());
		return _ContentValues;
	}

}
