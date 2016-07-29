package com.example.jdk.aa.database.sqlitedal;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jdk.aa.database.base.SQLiteDALBase;
import com.example.jdk.aa.model.ModelPayout;
import com.example.jdk.aa.utility.DateTools;

public class SQLiteDALPayout extends SQLiteDALBase {

	public SQLiteDALPayout(Context p_Context) {
		super(p_Context);
	}

	public Boolean InsertPayout(ModelPayout p_Info) {
		ContentValues _ContentValues = CreatParms(p_Info);
		Long p_NewID = GetDataBase().insert("Payout", null, _ContentValues);
		p_Info.SetPayoutID(p_NewID.intValue());
		return p_NewID > 0;
	}
	
	public Boolean DeletePayout(String p_Condition)
	{
		return Delete(GetTableNameAndPK()[0], p_Condition);
	}
	
	public Boolean UpdatePayout(String p_Condition, ModelPayout p_Info)
	{
		ContentValues _ContentValues = CreatParms(p_Info);
		return GetDataBase().update("Payout", _ContentValues, p_Condition, null) > 0;
	}
	
	public Boolean UpdatePayout(String p_Condition,ContentValues p_ContentValues)
	{
		return GetDataBase().update("Payout", p_ContentValues, p_Condition, null) > 0;
	}
	
	public List<ModelPayout> GetPayout(String p_Condition)
	{
		String _SqlText = "Select * From v_Payout Where  1=1 " + p_Condition;
		return GetList(_SqlText);
	}
	
	protected ModelPayout FindModel(Cursor p_Cursor)
	{
		ModelPayout _ModelPayout = new ModelPayout();
		_ModelPayout.SetPayoutID(p_Cursor.getInt(p_Cursor.getColumnIndex("PayoutID")));
		_ModelPayout.SetAccountBookID(p_Cursor.getInt((p_Cursor.getColumnIndex("AccountBookID"))));
		_ModelPayout.SetAccountBookName((p_Cursor.getString(p_Cursor.getColumnIndex("AccountBookName"))));
		_ModelPayout.SetCategoryID(p_Cursor.getInt((p_Cursor.getColumnIndex("CategoryID"))));
		_ModelPayout.SetCategoryName((p_Cursor.getString(p_Cursor.getColumnIndex("CategoryName"))));
		_ModelPayout.SetPath((p_Cursor.getString(p_Cursor.getColumnIndex("Path"))));
		_ModelPayout.SetPayWayID(p_Cursor.getInt((p_Cursor.getColumnIndex("PayWayID"))));
		_ModelPayout.SetPlaceID(p_Cursor.getInt((p_Cursor.getColumnIndex("PlaceID"))));
		_ModelPayout.SetAmount(new BigDecimal(p_Cursor.getString(((p_Cursor.getColumnIndex("Amount"))))));
		Date _PayoutDate = DateTools.getDate(p_Cursor.getString(p_Cursor.getColumnIndex("PayoutDate")), "yyyy-MM-dd");
		_ModelPayout.SetPayoutDate(_PayoutDate);
		_ModelPayout.SetPayoutType((p_Cursor.getString(p_Cursor.getColumnIndex("PayoutType"))));
		_ModelPayout.SetPayoutUserID((p_Cursor.getString(p_Cursor.getColumnIndex("PayoutUserID"))));
		_ModelPayout.SetComment((p_Cursor.getString(p_Cursor.getColumnIndex("Comment"))));
		Date _CreateDate = DateTools.getDate(p_Cursor.getString(p_Cursor.getColumnIndex("CreateDate")), "yyyy-MM-dd HH:mm:ss");	
		_ModelPayout.SetCreateDate(_CreateDate);
		_ModelPayout.SetState((p_Cursor.getInt(p_Cursor.getColumnIndex("State"))));
		
		return _ModelPayout;
	}

	@Override
	public void OnCreate(SQLiteDatabase p_DataBase) {
		StringBuilder s_CreateTableScript = new StringBuilder();
		
		s_CreateTableScript.append("		Create  TABLE Payout(");
		s_CreateTableScript.append("				[PayoutID] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		s_CreateTableScript.append("				,[AccountBookID] integer NOT NULL");
		s_CreateTableScript.append("				,[CategoryID] integer NOT NULL");
		s_CreateTableScript.append("				,[PayWayID] integer");
		s_CreateTableScript.append("				,[PlaceID] integer");
		s_CreateTableScript.append("				,[Amount] decimal NOT NULL");
		s_CreateTableScript.append("				,[PayoutDate] datetime NOT NULL");
		s_CreateTableScript.append("				,[PayoutType] varchar(20) NOT NULL");
		s_CreateTableScript.append("				,[PayoutUserID] text NOT NULL");
		s_CreateTableScript.append("				,[Comment] text");
		s_CreateTableScript.append("				,[CreateDate] datetime NOT NULL");
		s_CreateTableScript.append("				,[State] integer NOT NULL");
		s_CreateTableScript.append("				)");
		
		p_DataBase.execSQL(s_CreateTableScript.toString());
	}

	@Override
	public void OnUpgrade(SQLiteDatabase p_DataBase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String[] GetTableNameAndPK() {
		return new String[]{"Payout","PayoutID"};
	}

	public ContentValues CreatParms(ModelPayout p_Info) {
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put("AccountBookID",p_Info.GetAccountBookID());
		_ContentValues.put("CategoryID",p_Info.GetCategoryID());
		_ContentValues.put("PayWayID",p_Info.GetPayWayID());
		_ContentValues.put("PlaceID",p_Info.GetPlaceID());
		_ContentValues.put("Amount",p_Info.GetAmount().toString());
		_ContentValues.put("PayoutDate",DateTools.getFormatDateTime(p_Info.GetPayoutDate(),"yyyy-MM-dd"));
		_ContentValues.put("PayoutType",p_Info.GetPayoutType());
		_ContentValues.put("PayoutUserID",p_Info.GetPayoutUserID());
		_ContentValues.put("Comment",p_Info.GetComment());
		_ContentValues.put("CreateDate",DateTools.getFormatDateTime(p_Info.GetCreateDate(),"yyyy-MM-dd HH:mm:ss"));
		_ContentValues.put("State",p_Info.GetState());
		
		return _ContentValues;
	}

}
