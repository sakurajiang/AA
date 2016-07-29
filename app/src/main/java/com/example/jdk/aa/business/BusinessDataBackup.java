package com.example.jdk.aa.business;

import java.io.File;
import java.io.IOException;
import java.util.Date;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.jdk.aa.business.base.BusinessBase;
import com.example.jdk.aa.utility.FileUtil;

public class BusinessDataBackup extends BusinessBase {

	public BusinessDataBackup(Context p_Context) {
		super(p_Context);
	}

	public boolean DatabaseBackup(Date p_Backup) {
		boolean _Result = false;
		try {
			File _SourceFile = new File("/data/data/" + GetContext().getPackageName() + "/databases/GoDutchDataBase");
			
			if(_SourceFile.exists())
			{
				File _FileDir = new File("/sdcard/GoDutch/DataBaseBak/");
				if (!_FileDir.exists()) {
					_FileDir.mkdirs();
				}
				
				FileUtil.cp("/data/data/" + GetContext().getPackageName() + "/databases/GoDutchDataBase", "/sdcard/GoDutch/DataBaseBak/GoDutchDataBase");
				
			}
			
			SaveDatabaseBackupDate(p_Backup.getTime());
			
			_Result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return _Result;
	}
	
	public boolean DatabaseRestore() {
		boolean _Result = false;
		try {
			long _DatabaseBackupDate = LoadDatabaseBackupDate();
			
			if(_DatabaseBackupDate != 0)
			{
				FileUtil.cp("/sdcard/GoDutch/DataBaseBak/GoDutchDataBase", "/data/data/Mobidever.GoDutch/databases/GoDutchDataBase");
			}
			
			_Result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return _Result;
	}
	
	public void SaveDatabaseBackupDate(long _Millise)
	{		
//		MODE_PRIVATE��ΪĬ�ϲ���ģʽ��������ļ���˽�����ݣ�ֻ�ܱ�Ӧ�ñ�����ʣ��ڸ�ģʽ�£�д������ݻḲ��ԭ�ļ������ݣ���������д�������׷�ӵ�ԭ�ļ��У�����ʹ��MODE_APPEND
//		MODE_APPEND��ģʽ�����ļ��Ƿ���ڣ����ھ����ļ�׷�����ݣ�����ʹ������ļ�
//		MODE_WORLD_READABLE��MODE_WORLD_WRITEABLE������������Ӧ���Ƿ���Ȩ�޶�д���ļ�
//		MODE_WORLD_READABLE����ʾ��ǰ�ļ����Ա�����Ӧ�ö�ȡ��MODE_WORLD_WRITEABLE����ʾ��ǰ�ļ����Ա�����Ӧ��д��
		
		//��ȡָ��Key��SharedPreferences����
		SharedPreferences _SP = GetContext().getSharedPreferences("DatabaseBackupDate",Context.MODE_PRIVATE);
		//��ȡ�༭
		SharedPreferences.Editor _Editor = _SP.edit();
		//����ָ��Key��������
		_Editor.putLong("DatabaseBackupDate", _Millise);
		//�ύ��������
		_Editor.commit();
	}
	
	public long LoadDatabaseBackupDate()
	{
		long _DatabaseBackupDate = 0;
		//��ȡָ��Key��SharedPreferences����
		SharedPreferences _SP = GetContext().getSharedPreferences("DatabaseBackupDate",Context.MODE_PRIVATE);
		//����Ϊ��֤����������
		if (_SP != null) {
			//����ͻ�ȡָ��Key������
			_DatabaseBackupDate = _SP.getLong("DatabaseBackupDate", 0);
		}
		
		return _DatabaseBackupDate;
	}
}
