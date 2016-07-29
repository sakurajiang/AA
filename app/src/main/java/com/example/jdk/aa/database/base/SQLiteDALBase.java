package com.example.jdk.aa.database.base;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class SQLiteDALBase implements SQLiteHelper.SQLiteDataTable {
	
	private Context m_Context;
	private SQLiteDatabase m_DataBase;
	
	public SQLiteDALBase(Context p_Context)
	{
		m_Context = p_Context;
	}
	
	protected Context GetContext()
	{
		return m_Context;
	}
	
	public SQLiteDatabase GetDataBase()
	{
		if(m_DataBase == null)
		{
			m_DataBase = SQLiteHelper.GetInstance(m_Context).getWritableDatabase();
		}
		
		return m_DataBase;
	}
	
	public void BeginTransaction()
	{
		m_DataBase.beginTransaction();
	}
	
	public void SetTransactionSuccessful()
	{
		m_DataBase.setTransactionSuccessful();
	}
	
	public void EndTransaction()
	{
		m_DataBase.endTransaction();
	}
	
	public int GetCount(String p_Condition)
	{
		String _String[] = GetTableNameAndPK();
		Cursor _Cursor = ExecSql("Select " + _String[1] + " From " + _String[0] + " Where 1=1 " + p_Condition);
		int _Count = _Cursor.getCount();
		_Cursor.close();
		return _Count;
	}
	
	public int GetCount(String p_PK,String p_TableName, String p_Condition)
	{
		Cursor _Cursor = ExecSql("Select " + p_PK + " From " + p_TableName + " Where 1=1 " + p_Condition);
		int _Count = _Cursor.getCount();
		_Cursor.close();
		return _Count;
	}
	
	protected Boolean Delete(String p_TableName, String p_Condition)
	{
		return GetDataBase().delete(p_TableName, " 1=1 " + p_Condition, null) >= 0;
	}
	
	protected abstract String[] GetTableNameAndPK();
	
	protected List GetList(String p_SqlText)
	{
		Cursor _Cursor = ExecSql(p_SqlText);
		return CursorToList(_Cursor);
	}
	
	protected abstract Object FindModel(Cursor p_Cursor);
	
	protected List CursorToList(Cursor p_Cursor)
	{
		List _List = new ArrayList();
		while(p_Cursor.moveToNext())
		{
			Object _Object = FindModel(p_Cursor);
			_List.add(_Object);
		}
		p_Cursor.close();
		return _List;
	}
	
	public Cursor ExecSql(String p_SqlText)
	{
		return GetDataBase().rawQuery(p_SqlText, null);
	}
}
