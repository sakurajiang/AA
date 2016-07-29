package com.example.jdk.aa.business;


import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;

import com.example.jdk.aa.R;
import com.example.jdk.aa.business.base.BusinessBase;
import com.example.jdk.aa.database.sqlitedal.SQLiteDALCategory;
import com.example.jdk.aa.model.ModelCategory;
import com.example.jdk.aa.model.ModelCategoryTotal;

public class BusinessCategory extends BusinessBase {
	
	private SQLiteDALCategory m_SqLiteDALCategory;
	private final String  TYPE_FLAG = " And TypeFlag= '" + GetString(R.string.PayoutTypeFlag) + "'";
	
	public BusinessCategory(Context p_Context)
	{
		super(p_Context);
		m_SqLiteDALCategory = new SQLiteDALCategory(p_Context);
	}
	
	public Boolean InsertCategory(ModelCategory p_Info)
	{
		m_SqLiteDALCategory.BeginTransaction();
		try {
			Boolean _Result = m_SqLiteDALCategory.InsertCategory(p_Info);
			Boolean _Result2 = true;

			ModelCategory _ParentModelCategory = GetModelCategoryByCategoryID(p_Info.GetParentID());
			String _Path;
			if(_ParentModelCategory != null)
			{
				_Path = _ParentModelCategory.GetPath() + p_Info.GetCategoryID() + ".";
			}
			else {
				_Path = p_Info.GetCategoryID() + ".";
			}
			
			p_Info.SetPath(_Path);
			_Result2 = UpdateCategoryInsertTypeByCategoryID(p_Info);
			
			if(_Result && _Result2)
			{
				m_SqLiteDALCategory.SetTransactionSuccessful();
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			m_SqLiteDALCategory.EndTransaction();
		}
	}
	
	public Boolean DeleteCategoryByCategoryID(int p_CategoryID)
	{
		String _Condition = " CategoryID = " + p_CategoryID;
		Boolean _Result = m_SqLiteDALCategory.DeleteCategory(_Condition);
		return _Result;
	}
	
	public Boolean DeleteCategoryByPath(String p_Path) throws Exception
	{
		/*int _Count = m_SqLiteDALCategory.GetCount("PayoutID", "v_Payout", " And Path Like '" + p_Path + "%'");
		
		if(_Count != 0)
		{
			throw new Exception(GetString(R.string.ErrorMessageExistPayout));
		}
		
		String _Condition = " And Path Like '" + p_Path + "%'";
		Boolean _Result = m_SqLiteDALCategory.DeleteCategory(_Condition);
		return _Result;*/
		
		return true;
	}
	
	public Boolean UpdateCategoryInsertTypeByCategoryID(ModelCategory p_Info)
	{
		String _Condition = " CategoryID = " + p_Info.GetCategoryID();
		Boolean _Result = m_SqLiteDALCategory.UpdateCategory(_Condition, p_Info);		
		
		if(_Result)
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	public Boolean UpdateCategoryByCategoryID(ModelCategory p_Info)
	{
		m_SqLiteDALCategory.BeginTransaction();
		try {
			String _Condition = " CategoryID = " + p_Info.GetCategoryID();
			Boolean _Result = m_SqLiteDALCategory.UpdateCategory(_Condition, p_Info);
			Boolean _Result2 = true;
			

			ModelCategory _ParentModelCategory = GetModelCategoryByCategoryID(p_Info.GetParentID());
			String _Path;
			if(_ParentModelCategory != null)
			{
				_Path = _ParentModelCategory.GetPath() + p_Info.GetCategoryID() + ".";
			}
			else {
				_Path = p_Info.GetCategoryID() + ".";
			}
			
			p_Info.SetPath(_Path);
			_Result2 = UpdateCategoryInsertTypeByCategoryID(p_Info);
			
			if(_Result && _Result2)
			{
				m_SqLiteDALCategory.SetTransactionSuccessful();
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			m_SqLiteDALCategory.EndTransaction();
		}
	}
	
	public Boolean HideCategoryByByPath(String p_Path)
	{
		String _Condition = " Path Like '" + p_Path + "%'";
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put("State",0);
		Boolean _Result = m_SqLiteDALCategory.UpdateCategory(_Condition, _ContentValues);

		if(_Result)
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	public List<ModelCategory> GetCategory(String p_Condition)
	{
		return m_SqLiteDALCategory.GetCategory(p_Condition);
	}
	
	public List<ModelCategory> GetNotHideCategory()
	{
		return m_SqLiteDALCategory.GetCategory(TYPE_FLAG + " And State = 1");
	}
	
	public int GetNotHideCount()
	{
		return m_SqLiteDALCategory.GetCount(TYPE_FLAG + " And State = 1");
	}
	
	public int GetNotHideCountByParentID(int p_CategoryID)
	{
		return m_SqLiteDALCategory.GetCount(TYPE_FLAG + " And ParentID = " + p_CategoryID + " And State = 1");
	}
	
	public List<ModelCategory> GetNotHideRootCategory()
	{
		return m_SqLiteDALCategory.GetCategory(TYPE_FLAG + " And ParentID = 0 And State = 1");
	}
	
	public List<ModelCategory> GetNotHideCategoryListByParentID(int p_ParentID)
	{
		return m_SqLiteDALCategory.GetCategory(TYPE_FLAG + " And ParentID = " + p_ParentID + " And State = 1");
	}
	
	public ModelCategory GetModelCategoryByParentID(int p_ParentID)
	{
		List _List = m_SqLiteDALCategory.GetCategory(TYPE_FLAG + " And ParentID = " + p_ParentID);
		if(_List.size() == 1)
		{
			return (ModelCategory)_List.get(0);
		}
		else
		{
			return null;
		}
	}
	
	public ModelCategory GetModelCategoryByCategoryID(int p_CategoryID)
	{
		List _List = m_SqLiteDALCategory.GetCategory(TYPE_FLAG + " And CategoryID = " + p_CategoryID);
		if(_List.size() == 1)
		{
			return (ModelCategory)_List.get(0);
		}
		else
		{
			return null;
		}
	}
	
	public ArrayAdapter GetRootCategoryArrayAdapter()
	{
		List _List = GetNotHideRootCategory();
		_List.add(0,"--请选择--");
		ArrayAdapter _ArrayAdapter = new ArrayAdapter(GetContext(), android.R.layout.simple_spinner_item, _List);
		_ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return _ArrayAdapter;
	}
	
	public ArrayAdapter GetAllCategoryArrayAdapter()
	{
		/*List _List = GetNotHideCategory();
		String _Name[] = new String[_List.size()];
		for (int i = 0; i < _List.size(); i++) {
			_Name[i] = ((ModelCategory)_List.get(i)).GetCategoryName();
		}
		ArrayAdapter _ArrayAdapter = new ArrayAdapter(GetContext(), R.layout.common_auto_complete, _List);
		return _ArrayAdapter;*/
		
		return null;
	}
	
	public List<ModelCategoryTotal> CategoryTotalByRootCategory()
	{
		String _Condition = TYPE_FLAG + " And ParentID = 0 And State = 1";
		List<ModelCategoryTotal> _ModelCategoryTotalList = CategoryTotal(_Condition);
		
		return _ModelCategoryTotalList;
	}
	
	public List<ModelCategoryTotal> CategoryTotalByParentID(int p_ParentID)
	{
		String _Condition = TYPE_FLAG + " And ParentID = " + p_ParentID;
		List<ModelCategoryTotal> _ModelCategoryTotalList = CategoryTotal(_Condition);
		
		return _ModelCategoryTotalList;
	}
	
	public List<ModelCategoryTotal> CategoryTotal(String p_Condition)
	{
		String _Condition = p_Condition;
		Cursor _Cursor = m_SqLiteDALCategory.ExecSql("Select Count(PayoutID) As Count, Sum(Amount) As SumAmount, CategoryName From v_Payout Where 1=1 " + _Condition + " Group By CategoryName");
		List<ModelCategoryTotal> _ModelCategoryTotalList = new ArrayList<ModelCategoryTotal>();
		while (_Cursor.moveToNext()) {
			ModelCategoryTotal _ModelCategoryTotal = new ModelCategoryTotal();
			_ModelCategoryTotal.Count = _Cursor.getString(_Cursor.getColumnIndex("Count"));
			_ModelCategoryTotal.SumAmount = _Cursor.getString(_Cursor.getColumnIndex("SumAmount"));
			_ModelCategoryTotal.CategoryName = _Cursor.getString(_Cursor.getColumnIndex("CategoryName"));
			_ModelCategoryTotalList.add(_ModelCategoryTotal);
		}
		
		return _ModelCategoryTotalList;
	}
}
