package com.example.jdk.aa.activity;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.jdk.aa.R;
import com.example.jdk.aa.activity.base.ActivityFrame;
import com.example.jdk.aa.model.ModelCategoryTotal;

public class ActivityCategoryChart extends ActivityFrame {
	private List<ModelCategoryTotal> mModelCategoryTotal;
	private TextView tvContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InitVariable();
		View _PieView = CategoryStatistics();
		AppendMainBody(_PieView);
		RemoveBottomBox();
	}

	private View CategoryStatistics() {
		int[] _Color = new int[] { Color.parseColor("#FF5552"), Color.parseColor("#2A94F1"), Color.parseColor("#F12792"), Color.parseColor("#FFFF52"), Color.parseColor("#84D911"),Color.parseColor("#5255FF") };
		DefaultRenderer _DefaultRenderer = BuildCategoryRenderer(_Color);
		CategorySeries _CategorySeries = _BuildCategoryDataset("消费类别统计", mModelCategoryTotal);
		View _PieView = ChartFactory.getPieChartView(this, _CategorySeries, _DefaultRenderer);
		return _PieView;
	}
	
	protected DefaultRenderer BuildCategoryRenderer(int[] colors) {
        DefaultRenderer _Renderer = new DefaultRenderer();
        _Renderer.setZoomButtonsVisible(true);
        _Renderer.setLabelsTextSize(15);
        _Renderer.setLegendTextSize(15);
        _Renderer.setLabelsColor(Color.BLUE);
        _Renderer.setMargins(new int[] { 20, 30, 15, 10 });
        int _Color = 0;
        for (int i = 0;i<mModelCategoryTotal.size();i++) {
          SimpleSeriesRenderer _R = new SimpleSeriesRenderer();
          _R.setColor(colors[_Color]);
          _Renderer.addSeriesRenderer(_R);
          _Color++;
          if (_Color > colors.length) {
        	  _Color = 0;
          }
        }
        return _Renderer;
      }
    
    protected CategorySeries _BuildCategoryDataset(String title, List<ModelCategoryTotal> values) {
        CategorySeries series = new CategorySeries(title);
        for (ModelCategoryTotal value : values) {
          series.add("数量： " + value.Count, Double.parseDouble(value.Count));
        }

        return series;
      }
	
	private void SetTitle() {
		/*int _Count = mBusinessCategory.GetNotHideCount();*/
		String _Titel = getString(R.string.ActivityCategoryTotal);
		SetTopBarTitle(_Titel);
	}

	protected void InitView() {
//		elvCategoryList = (ExpandableListView) findViewById(R.id.ExpandableListViewCategory);
//		tvContent = (TextView) findViewById(R.id.tvContent);
	}

	protected void InitListeners() {
		
	}
	

	protected void InitVariable() {
		mModelCategoryTotal = (List<ModelCategoryTotal>) getIntent().getSerializableExtra("Total");
	}

	protected void BindData()
	{
		SetTitle();
		String _Content = "";
		for (int i = 0; i < mModelCategoryTotal.size(); i++) {
			ModelCategoryTotal _ModelCategoryTotal = mModelCategoryTotal.get(i);
			_Content += _ModelCategoryTotal.CategoryName + "--" + _ModelCategoryTotal.Count + "--" + _ModelCategoryTotal.SumAmount + "\r\n";
		}
		
		tvContent.setText(_Content);
	}
	
}
