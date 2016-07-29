package com.example.jdk.aa.business;

import java.util.List;


import android.content.Context;
import android.database.Cursor;

import com.example.jdk.aa.R;
import com.example.jdk.aa.business.base.BusinessBase;
import com.example.jdk.aa.database.sqlitedal.SQLiteDALPayout;
import com.example.jdk.aa.model.ModelPayout;

public class BusinessPayout extends BusinessBase {
	
	private SQLiteDALPayout m_SqLiteDALPayout;
	
	public BusinessPayout(Context p_Context)
	{
		super(p_Context);
		m_SqLiteDALPayout = new SQLiteDALPayout(p_Context);
	}
	
	public Boolean InsertPayout(ModelPayout p_Info)
	{
		Boolean _Result = m_SqLiteDALPayout.InsertPayout(p_Info);
		
		if(_Result)
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	public Boolean DeletePayoutByPayoutID(int p_PayoutID)
	{
		String _Condition = " And PayoutID = " + p_PayoutID;
		Boolean _Result = m_SqLiteDALPayout.DeletePayout(_Condition);
		return _Result;
	}
	
	public Boolean DeletePayoutByAccountBookID(int p_AccountBookID)
	{
		String _Condition = " And AccountBookID = " + p_AccountBookID;
		Boolean _Result = m_SqLiteDALPayout.DeletePayout(_Condition);
		return _Result;
	}
	
	public Boolean UpdatePayoutByPayoutID(ModelPayout p_Info)
	{
		String _Condition = " PayoutID = " + p_Info.GetPayoutID();
		Boolean _Result = m_SqLiteDALPayout.UpdatePayout(_Condition, p_Info);
		
		if(_Result)
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	public List<ModelPayout> GetPayout(String p_Condition)
	{
		return m_SqLiteDALPayout.GetPayout(p_Condition);
	}
	
	public int GetCount()
	{
		return m_SqLiteDALPayout.GetCount("");
	}

	public List<ModelPayout> GetPayoutByAccountBookID(int p_AccountBookID)
	{
		String _Condition = " And AccountBookID = " + p_AccountBookID + " Order By PayoutDate DESC,PayoutID DESC";
		return m_SqLiteDALPayout.GetPayout(_Condition);
	}
	
	public String GetPayoutTotalMessage(String p_PayoutDate,int p_AccountBookID)
	{
		String _Total[] = GetPayoutTotalByPayoutDate(p_PayoutDate,p_AccountBookID);
		return GetContext().getString(R.string.TextViewTextPayoutTotal, new Object[]{_Total[0],_Total[1]});
	}
	
	private String[] GetPayoutTotalByPayoutDate(String p_PayoutDate,int p_AccountBookID)
	{
		String _Condition = " And PayoutDate = '" + p_PayoutDate + "' And AccountBookID = " + p_AccountBookID;
		return GetPayoutTotal(_Condition);
	}
	
	public String[] GetPayoutTotalByAccountBookID(int p_AccountBookID)
	{
		String _Condition = " And AccountBookID = " + p_AccountBookID;
		return GetPayoutTotal(_Condition);
	}
	
	private String[] GetPayoutTotal(String p_Condition)
	{
		String _SqlText = "Select ifnull(Sum(Amount),0) As SumAmount,Count(Amount) As Count From Payout Where 1=1 " + p_Condition;
		String _Total[] = new String[2];
		Cursor _Cursor = m_SqLiteDALPayout.ExecSql(_SqlText);
		if(_Cursor.getCount() == 1)
		{
			while(_Cursor.moveToNext())
			{
				_Total[0] = _Cursor.getString(_Cursor.getColumnIndex("Count"));
				_Total[1] = _Cursor.getString(_Cursor.getColumnIndex("SumAmount"));
			}
		}
		return _Total;
	}
	
	public List<ModelPayout> GetPayoutOrderByPayoutUserID(String p_Condition)
	{
		p_Condition += " Order By PayoutUserID";
		List<ModelPayout> _List = GetPayout(p_Condition);
		if(_List.size() > 0)
		{
			return _List;
		}
		
		return null;
	}
	
	public String[] GetPayoutDateAndAmountTotal(String p_Condition)
	{
		String _SqlText = "Select Min(PayoutDate) As MinPayoutDate,Max(PayoutDate) As MaxPayoutDate,Sum(Amount) As Amount From Payout Where 1=1 " + p_Condition;
		String _PayoutTotal[] = new String[3];
		Cursor _Cursor = m_SqLiteDALPayout.ExecSql(_SqlText);
		if(_Cursor.getCount() == 1)
		{
			while(_Cursor.moveToNext())
			{
				_PayoutTotal[0] = _Cursor.getString(_Cursor.getColumnIndex("MinPayoutDate"));
				_PayoutTotal[1] = _Cursor.getString(_Cursor.getColumnIndex("MaxPayoutDate"));
				_PayoutTotal[2] = _Cursor.getString(_Cursor.getColumnIndex("Amount"));
			}
		}
		return _PayoutTotal;
	}
}
