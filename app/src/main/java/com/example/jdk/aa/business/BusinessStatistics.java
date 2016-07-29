package com.example.jdk.aa.business;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import android.R.anim;
import android.R.integer;
import android.R.string;
import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;

import com.example.jdk.aa.R;
import com.example.jdk.aa.business.base.BusinessBase;
import com.example.jdk.aa.model.ModelPayout;

import jxl.*;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
public class BusinessStatistics extends BusinessBase {

	private BusinessPayout m_BusinessPayout;
	private BusinessUser m_BusinessUser;
	private BusinessAccountBook m_BusinessAccountBook;

	public BusinessStatistics(Context p_Context)
	{
		super(p_Context);
		m_BusinessPayout = new BusinessPayout(p_Context);
		m_BusinessUser = new BusinessUser(p_Context);
		m_BusinessAccountBook = new BusinessAccountBook(p_Context);
	}

	public String GetPayoutUserIDByAccountBookID(int p_AccountBookID)
	{
		String _Result = "";
		List<ModelStatistics> _ListModelStatisticsTotal =  GetPayoutUserID(" And AccountBookID = " + p_AccountBookID);

		//将得到的信息进行转换，以方便观看
		for (int i = 0; i < _ListModelStatisticsTotal.size(); i++) {
			ModelStatistics _ModelStatistics = _ListModelStatisticsTotal.get(i);
			if(_ModelStatistics.getPayoutType().equals("个人")) {
				_Result += _ModelStatistics.PayerUserID + "个人消费 " + _ModelStatistics.Cost.toString() + "元\r\n";
			} else if(_ModelStatistics.getPayoutType().equals("均分")) {
				if(_ModelStatistics.PayerUserID.equals(_ModelStatistics.ConsumerUserID)) {
					_Result += _ModelStatistics.PayerUserID + "个人消费 " + _ModelStatistics.Cost.toString() + "元\r\n";
				} else {
					_Result += _ModelStatistics.ConsumerUserID + "应支付给" + _ModelStatistics.PayerUserID + _ModelStatistics.Cost + "元\r\n";
				}
			}
			//_Result += _ModelStatistics.PayerUserID + "#" + _ModelStatistics.ConsumerUserID + "#" + _ModelStatistics.Cost + "(" + _ModelStatistics.getPayoutType() + ")" + "\r\n";
		}

		return _Result;
	}

	public List<ModelStatistics> GetPayoutUserID(String p_Condition)
	{
		//得到拆分好的统计信息
		List<ModelStatistics> _ListModelStatistics = GetModelStatisticsList(p_Condition);
		//存放按付款人分类的临时统计信息
		List<ModelStatistics> _ListModelStatisticsTemp = new ArrayList<ModelStatistics>();
		//存放统计好的汇总
		List<ModelStatistics> _ListModelStatisticsTotal = new ArrayList<ModelStatistics>();
		String _Result = "";
		//遍历拆分好的统计信息
		for (int i = 0; i < _ListModelStatistics.size(); i++) {
			//得到拆分好的一条信息
			ModelStatistics _ModelStatistics = _ListModelStatistics.get(i);
			_Result += _ModelStatistics.PayerUserID + "#" + _ModelStatistics.ConsumerUserID + "#" + _ModelStatistics.Cost + "\r\n";
			//保存当前的付款人ID
			String _CurrentPayerUserID = _ModelStatistics.PayerUserID;

			//把当前信息按付款人分类的临时数组
			ModelStatistics _ModelStatisticsTemp = new ModelStatistics();
			_ModelStatisticsTemp.PayerUserID = _ModelStatistics.PayerUserID;
			_ModelStatisticsTemp.ConsumerUserID = _ModelStatistics.ConsumerUserID;
			_ModelStatisticsTemp.Cost = _ModelStatistics.Cost;
			_ModelStatisticsTemp.setPayoutType(_ModelStatistics.getPayoutType());
			_ListModelStatisticsTemp.add(_ModelStatisticsTemp);

			//计算下一行的索引
			int _NextIndex;
			//如果下一行索引小于统计信息索引，则可以加1
			if((i+1) < _ListModelStatistics.size())
			{
				_NextIndex = i+1;
			}
			else {
				//否则证明已经到尾，则索引赋值为当前行
				_NextIndex = i;
			}

			//如果当前付款人与下一位付款人不同，则证明分类统计已经到尾，或者已经循环到统计数组最后一位，则就开始进入进行统计
			if (!_CurrentPayerUserID.equals(_ListModelStatistics.get(_NextIndex).PayerUserID) || _NextIndex == i) {

				//开始进行遍历进行当前分类统计数组的统计
				for (int j = 0; j < _ListModelStatisticsTemp.size(); j++) {
					//取出来一个
					ModelStatistics _ModelStatisticsTotal = _ListModelStatisticsTemp.get(j);
					//判断在总统计数组当中是否已经存在该付款人和消费人的信息
					int _Index = GetPostionByConsumerUserID(_ListModelStatisticsTotal,_ModelStatisticsTotal.PayerUserID, _ModelStatisticsTotal.ConsumerUserID);
					//如果已经存在，则开始在原来的数据上进行累加
					if(_Index != -1)
					{
						_ListModelStatisticsTotal.get(_Index).Cost = _ListModelStatisticsTotal.get(_Index).Cost.add(_ModelStatisticsTotal.Cost);
					}
					else {
						//否则就是一条新信息，添加到统计数组当中
						_ListModelStatisticsTotal.add(_ModelStatisticsTotal);
					}
				}
				//全部遍历完后清空当前分类统计数组，进入下一个分类统计数组的计算，直到尾
				_ListModelStatisticsTemp.clear();
			}

		}

		return _ListModelStatisticsTotal;
	}

	private int GetPostionByConsumerUserID(List<ModelStatistics> p_ListModelStatisticsTotal,String p_PayerUserID,String p_ConsumerUserID)
	{
		int _Index = -1;
		for (int i = 0; i < p_ListModelStatisticsTotal.size(); i++) {
			if (p_ListModelStatisticsTotal.get(i).PayerUserID.equals(p_PayerUserID) && p_ListModelStatisticsTotal.get(i).ConsumerUserID.equals(p_ConsumerUserID)) {
				_Index = i;
			}
		}

		return _Index;
	}

	private List<ModelStatistics> GetModelStatisticsList(String p_Condition) {
		//按支付人ID排序取出消费记录
		List<ModelPayout> _ListPayout = m_BusinessPayout.GetPayoutOrderByPayoutUserID(p_Condition);

		//获取计算方式数组
		String _PayoutTypeArr[] = GetContext().getResources().getStringArray(R.array.PayoutType);

		List<ModelStatistics> _ListModelStatistics = new ArrayList<ModelStatistics>();

		if(_ListPayout != null)
		{
			//遍历消费记录列表
			for (int i = 0; i < _ListPayout.size(); i++) {
				//取出一条消费记录
				ModelPayout _ModelPayout = _ListPayout.get(i);
				//将消费人ID转换为真实名称
				String _PayoutUserName[] = m_BusinessUser.GetUserNameByUserID(_ModelPayout.GetPayoutUserID()).split(",");
				String _PayoutUserID[] = _ModelPayout.GetPayoutUserID().split(",");

				//取出计算方式
				String _PayoutType = _ModelPayout.GetPayoutType();

				//存放计算后的消费金额
				BigDecimal _Cost;

				//判断本次消费记录的消费类型，如果是均分，则除以本次消费人的个数，算出平均消费金额
				if(_PayoutType.equals(_PayoutTypeArr[0]))
				{
					//得到消费人数
					int _PayoutTotal = _PayoutUserName.length;

					/*金额的数据类型是BigDecimal
					通过BigDecimal的divide方法进行除法时当不整除，出现无限循环小数时，就会抛异常的，异常如下：java.lang.ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result. at java.math.BigDecimal.divide(Unknown Source)

					应用场景：一批中供客户的单价是1000元/年，如果按月计算的话1000/12=83.3333333333....

					解决之道：就是给divide设置精确的小数点divide(xxxxx,2, BigDecimal.ROUND_HALF_EVEN) */
					//得到计算后的平均消费金额
					_Cost = _ModelPayout.GetAmount().divide(new BigDecimal(_PayoutTotal),2, BigDecimal.ROUND_HALF_EVEN);
				}
				//如果是借贷或是个人消费，则直接取出消费金额
				else {
					_Cost = _ModelPayout.GetAmount();
				}

				//遍历消费人数组
				for (int j = 0; j < _PayoutUserID.length; j++) {

					//如果是借贷则跳过第一个索引，因为第一个人是借贷人自己
					if (_PayoutType.equals(_PayoutTypeArr[1]) && j == 0) {
						continue;
					}

					//声明一个统计类
					ModelStatistics _ModelStatistics = new ModelStatistics();
					//将统计类的支付人设置为消费人数组的第一个人
					_ModelStatistics.PayerUserID = _PayoutUserName[0];
					//设置消费人
					_ModelStatistics.ConsumerUserID = _PayoutUserName[j];
					//设置消费类型
					_ModelStatistics.setPayoutType(_PayoutType);
					//设置算好的消费金额
					_ModelStatistics.Cost = _Cost;

					_ListModelStatistics.add(_ModelStatistics);
				}
			}
		}

		return _ListModelStatistics;
	}

	public String ExportStatistics(int p_AccountBookID) throws Exception {
		String _Result = "";
		String _AccountBookName = m_BusinessAccountBook.GetAccountBookNameByAccountId(p_AccountBookID);
		Date _Date = new Date();
//		String _FileName = _AccountBookName + String.valueOf(_Date.getYear()) + String.valueOf(_Date.getMonth()) + String.valueOf(_Date.getDay()) + ".xls";
		String _FileName = String.valueOf(_Date.getYear()) + String.valueOf(_Date.getMonth()) + String.valueOf(_Date.getDay()) + ".xls";
		File _FileDir = new File("/sdcard/GoDutch/Export/");
		if (!_FileDir.exists()) {
			_FileDir.mkdirs();
		}
		File _File = new File("/sdcard/GoDutch/Export/" + _FileName);
		if(!_File.exists()) {
			_File.createNewFile();
		}

		WritableWorkbook wBookData;
		//创建工作簿
		wBookData = Workbook.createWorkbook(_File);
		//创建工作表
		WritableSheet wsAccountBook = wBookData.createSheet(_AccountBookName, 0);

		String[] _Titles = {"编号", "姓名", "金额", "消费信息", "消费类型"};
		Label _Label;
		//添加标题行
		for (int i = 0; i < _Titles.length; i++) {
			_Label = new Label(i, 0, _Titles[i]);
			wsAccountBook.addCell(_Label);
		}

		/*
		 * 添加行
		 */
		List<ModelStatistics> _ListModelStatisticsTotal =  GetPayoutUserID(" And AccountBookID = " + p_AccountBookID);

		for(int i = 0;i < _ListModelStatisticsTotal.size(); i++) {
			ModelStatistics _ModelStatistics = _ListModelStatisticsTotal.get(i);

			//添加编号列
			jxl.write.Number _IdCell = new Number(0, i+1, i+1);
			wsAccountBook.addCell(_IdCell);

			//添加姓名
			Label _lblName = new Label(1, i+1, _ModelStatistics.PayerUserID);
			wsAccountBook.addCell(_lblName);

			//格式化金额类型显示
			NumberFormat nfMoney = new NumberFormat("#.##");
			WritableCellFormat wcfFormat = new WritableCellFormat(nfMoney);

			//添加金额
			Number _CostCell = new Number(2, i+1, _ModelStatistics.Cost.doubleValue(), wcfFormat);
			wsAccountBook.addCell(_CostCell);

			//添加消费信息
			String _Info = "";
			if(_ModelStatistics.getPayoutType().equals("个人")) {
				_Info = _ModelStatistics.PayerUserID + "个人消费 " + _ModelStatistics.Cost.toString() + "元";
			} else if(_ModelStatistics.getPayoutType().equals("均分")) {
				if(_ModelStatistics.PayerUserID.equals(_ModelStatistics.ConsumerUserID)) {
					_Info = _ModelStatistics.PayerUserID + "个人消费 " + _ModelStatistics.Cost.toString() + "元";
				} else {
					_Info = _ModelStatistics.ConsumerUserID + "应支付给" + _ModelStatistics.PayerUserID + _ModelStatistics.Cost + "元";
				}
			}
			Label _lblInfo = new Label(3, i+1, _Info);
			wsAccountBook.addCell(_lblInfo);

			//添加消费类型
			Label _lblPayoutType = new Label(4, i+1, _ModelStatistics.getPayoutType());
			wsAccountBook.addCell(_lblPayoutType);
		}

		wBookData.write();
		wBookData.close();
		_Result = "数据已经导出！位置在：/sdcard/GoDutch/Export/" + _FileName;
		return _Result;
	}

	public class ModelStatistics
	{
		public String PayerUserID;
		public String ConsumerUserID;
		private String m_PayoutType;
		public BigDecimal Cost;

		public String getPayoutType() {
			return m_PayoutType;
		}

		public void setPayoutType(String p_Value) {
			m_PayoutType = p_Value;
		}
	}
}


