package com.example.jdk.aa.business;

import java.text.SimpleDateFormat;
import java.util.List;


import android.R.integer;
import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.jdk.aa.business.base.BusinessBase;
import com.example.jdk.aa.database.sqlitedal.SQLiteDALAccountBook;
import com.example.jdk.aa.model.ModelAccountBook;

public class BusinessAccountBook extends BusinessBase {

	private SQLiteDALAccountBook m_SqLiteDALAccountBook;

	public BusinessAccountBook(Context p_Context) {
		super(p_Context);
		m_SqLiteDALAccountBook = new SQLiteDALAccountBook(p_Context);
	}

	public Boolean InsertAccountBook(ModelAccountBook p_Info) {
		m_SqLiteDALAccountBook.BeginTransaction();
		try {
			Boolean _Result = m_SqLiteDALAccountBook.InsertAccountBook(p_Info);
			Boolean _Result2 = true;
			if (p_Info.GetIsDefault() == 1 && _Result) {
				_Result2 = SetIsDefault(p_Info.GetAccountBookID());
			}

			if (_Result && _Result2) {
				m_SqLiteDALAccountBook.SetTransactionSuccessful();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			m_SqLiteDALAccountBook.EndTransaction();
		}
	}

	public Boolean DeleteAccountBookByAccountBookID(int p_AccountBookID) {
		m_SqLiteDALAccountBook.BeginTransaction();
		try {
			String _Condition = " And AccountBookID = " + p_AccountBookID;
			Boolean _Result = m_SqLiteDALAccountBook
					.DeleteAccountBook(_Condition);
			Boolean _Result2 = true;
			if (_Result) {
				BusinessPayout _BusinessPayout = new BusinessPayout(
						GetContext());
				_Result2 = _BusinessPayout
						.DeletePayoutByAccountBookID(p_AccountBookID);
			}

			if (_Result && _Result2) {
				m_SqLiteDALAccountBook.SetTransactionSuccessful();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			m_SqLiteDALAccountBook.EndTransaction();
		}
	}

	public Boolean UpdateAccountBookByAccountBookID(ModelAccountBook p_Info) {
		m_SqLiteDALAccountBook.BeginTransaction();
		try {
			String _Condition = " AccountBookID = " + p_Info.GetAccountBookID();
			Boolean _Result = m_SqLiteDALAccountBook.UpdateAccountBook(
					_Condition, p_Info);
			Boolean _Result2 = true;
			if (p_Info.GetIsDefault() == 1 && _Result) {
				_Result2 = SetIsDefault(p_Info.GetAccountBookID());
			}

			if (_Result && _Result2) {
				m_SqLiteDALAccountBook.SetTransactionSuccessful();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			m_SqLiteDALAccountBook.EndTransaction();
		}
	}

	public List<ModelAccountBook> GetAccountBook(String p_Condition) {
		return m_SqLiteDALAccountBook.GetAccountBook(p_Condition);
	}

	public boolean IsExistAccountBookByAccountBookName(
			String p_AccountBookName, Integer p_AccountBookID) {
		String _Condition = " And AccountBookName = '" + p_AccountBookName
				+ "'";
		if (p_AccountBookID != null) {
			_Condition += " And AccountBookID <> " + p_AccountBookID;
		}
		List _List = m_SqLiteDALAccountBook.GetAccountBook(_Condition);
		if (_List.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public ModelAccountBook GetDefaultModelAccountBook() {
		List _List = m_SqLiteDALAccountBook
				.GetAccountBook(" And IsDefault = 1");
		if (_List.size() == 1) {
			return (ModelAccountBook) _List.get(0);
		} else {
			return null;
		}
	}

	public int GetCount() {
		return m_SqLiteDALAccountBook.GetCount("");
	}

	public Boolean SetIsDefault(int p_ID) {
		String _Condition = " IsDefault = 1";
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put("IsDefault", 0);
		Boolean _Result = m_SqLiteDALAccountBook.UpdateAccountBook(_Condition,
				_ContentValues);

		_Condition = " AccountBookID = " + p_ID;
		_ContentValues.clear();
		_ContentValues.put("IsDefault", 1);
		Boolean _Result2 = m_SqLiteDALAccountBook.UpdateAccountBook(_Condition,
				_ContentValues);

		if (_Result && _Result2) {
			return true;
		} else {
			return false;
		}
	}
	
	public String GetAccountBookNameByAccountId(int p_BookId) {
		String _ConditionString = "And AccountBookID = " + String.valueOf(p_BookId);
		List<ModelAccountBook> _AccountBooks = m_SqLiteDALAccountBook.GetAccountBook(_ConditionString);
		return _AccountBooks.get(0).GetAccountBookName();
	}
}
