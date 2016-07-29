package com.example.jdk.aa.utility;

public final class RegexTools {
	public final static String _ChineseEnglishNum = "[a-zA-Z0-9\u4e00-\u9fa5]+";
	//public final static String _Money = "[\\d]+[.]?[1-9]{0,2}";
	//判断是否是正整数，并且不能超过小数点两位
	public final static String _Money = "[\\d]+|[\\d]+[.]{1}[1-9]{1,2}|[\\d]+[.]{1}[0-9]{1}[1-9]{1}";
	
	public static Boolean IsChineseEnglishNum(String _Value)
	{
		return _Value.matches(_ChineseEnglishNum);
	}
	
	public static Boolean IsMoney(String _Value)
	{
		return _Value.matches(_Money);
	}
	
	public static Boolean IsNull(Object p_Object)
	{
		if (p_Object == null) {
			return false;
		} else {
			return true;
		}
	}
}
