package com.example.jdk.aa.database.base;


import java.util.ArrayList;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.jdk.aa.utility.Reflection;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static SQLiteDateBaseConfig SQLITE_DATEBASE_CONFIG;
	private static SQLiteHelper INSTANCE;
	private Context mContext;
	private Reflection mReflection;
	
	public interface SQLiteDataTable
	{
		public void OnCreate(SQLiteDatabase p_DataBase);
		public void OnUpgrade(SQLiteDatabase p_DataBase);
	}
	
	private SQLiteHelper(Context pContext)
	{
		super(pContext, SQLITE_DATEBASE_CONFIG.GetDataBaseName(), null, SQLITE_DATEBASE_CONFIG.GetVersion());
		mContext = pContext;
	}
	
	public static SQLiteHelper GetInstance(Context pContext)
	{
		if (INSTANCE == null) {
			SQLITE_DATEBASE_CONFIG = SQLiteDateBaseConfig.GetInstance(pContext);
			INSTANCE = new SQLiteHelper(pContext);
		}
		
		return INSTANCE;
	}
	
	@Override
	public void onCreate(SQLiteDatabase pDataBase) {
		ArrayList<String> _ArrayList = SQLITE_DATEBASE_CONFIG.GetTables();
		Log.i("Logcat", "LIST"+_ArrayList);
		mReflection = new Reflection();
		
		/*for (int i = 0; i < _ArrayList.size(); i++) {
			try {
				SQLiteDataTable _SQLiteDataTable = (SQLiteDataTable) mReflection.newInstance(
						_ArrayList.get(i), 
						new Object[]{mContext},
						new Class[]{Context.class});
				_SQLiteDataTable.OnCreate(pDataBase);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}*/
		for(int i=0;i<_ArrayList.size();i++)
		{
			try {
				Log.i("Logcat","list="+_ArrayList.get(i));
				((SQLiteDataTable)mReflection.newInstance(_ArrayList.get(i), new Object[]{mContext}, new Class[]{Context.class})).OnCreate(pDataBase);
				Log.i("Logcat", "msg2");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
