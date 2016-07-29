package com.example.jdk.aa.adapter;

import java.util.ArrayList;
import java.util.List;


import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.jdk.aa.R;
import com.example.jdk.aa.business.BusinessCategory;
import com.example.jdk.aa.model.ModelCategory;

public class AdapterCategory extends BaseExpandableListAdapter {
	
	private class GroupHolder
	{
		TextView Name;
		TextView Count;
	}
	
	private class ChildHolder
	{
		TextView Name;
	}

	private Context m_Context;
	private List m_List;
	private BusinessCategory m_BusinessCategory;
	public List _ChildCountOfGroup;
	
	public AdapterCategory(Context p_Context)
	{
		m_BusinessCategory = new BusinessCategory(p_Context);
		m_Context = p_Context;
		m_List = m_BusinessCategory.GetNotHideRootCategory();
		_ChildCountOfGroup = new ArrayList();
	}
	
	public Integer GetChildCountOfGroup(int groupPosition)
	{
		return (Integer) _ChildCountOfGroup.get(groupPosition);
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ModelCategory _ModelCategory = (ModelCategory) getGroup(groupPosition);
		List _List = m_BusinessCategory.GetNotHideCategoryListByParentID(_ModelCategory.GetCategoryID());
		return _List.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		ChildHolder _ChildHolder;
		if(convertView == null)
		{
			convertView = LayoutInflater.from(m_Context).inflate(R.layout.category_children_list_item, null);
			_ChildHolder = new ChildHolder();
			_ChildHolder.Name = (TextView)convertView.findViewById(R.id.tvCategoryName);
			convertView.setTag(_ChildHolder);
		}
		else {
			_ChildHolder = (ChildHolder)convertView.getTag();
		}
		ModelCategory _ModelCategory = (ModelCategory)getChild(groupPosition, childPosition);
		_ChildHolder.Name.setText(_ModelCategory.GetCategoryName());
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ModelCategory _ModelCategory = (ModelCategory) getGroup(groupPosition);
		List _List = m_BusinessCategory.GetNotHideCategoryListByParentID(_ModelCategory.GetCategoryID());
		return _List.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return (ModelCategory)m_List.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return m_List.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return (long)groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		GroupHolder _GroupHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(m_Context).inflate(R.layout.category_group_list_item, null);
			_GroupHolder = new GroupHolder();
			_GroupHolder.Name = (TextView)convertView.findViewById(R.id.tvCategoryName);
			_GroupHolder.Count = (TextView)convertView.findViewById(R.id.tvCount);
			convertView.setTag(_GroupHolder);
		}
		else {
			_GroupHolder = (GroupHolder)convertView.getTag();
		}
		ModelCategory _ModelCategory = (ModelCategory)getGroup(groupPosition);
		_GroupHolder.Name.setText(_ModelCategory.GetCategoryName());
		int _Count = m_BusinessCategory.GetNotHideCountByParentID(_ModelCategory.GetCategoryID());
		_GroupHolder.Count.setText(m_Context.getString(R.string.TextViewTextChildrenCategory, _Count));
		_ChildCountOfGroup.add(_Count);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		//���Ƿ����Ψһid
		//�Ƿ�ָ��������ͼ��������ͼ��ID��Ӧ�ĺ�̨���ݸı�Ҳ�ᱣ�ָ�ID
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		////���Ƿ��ѡ
		return true;
	}

}
