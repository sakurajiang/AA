package com.example.jdk.aa.utility;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.jdk.aa.R;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by JDK on 2016/7/30.
 */
public class MyAuthCodeUtils extends View{
    private String mMyAuthcodeText;
    private int mMyAuthcodeTextColor;
    private int mMyAuthcodeTextSize;
    private Paint mPaint;
    private Rect mRect;
    public MyAuthCodeUtils(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyAuthcodeText = randomText();
                setmMyAuthcodeText(mMyAuthcodeText);
                postInvalidate();
            }
        });
    }

    public MyAuthCodeUtils(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyAuthCodeUtils,defStyleAttr,0);
        int indexCount=typedArray.getIndexCount();
        for (int i=0;i<indexCount;i++){
            int attr=typedArray.getIndex(i);
            switch (attr){
                case R.styleable.MyAuthCodeUtils_myAuthcodeText:
                    mMyAuthcodeText=typedArray.getString(attr);
                    break;
                case R.styleable.MyAuthCodeUtils_myAuthcodeTextColor:
                    mMyAuthcodeTextColor=typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MyAuthCodeUtils_myAuthcodeTextSize:
                    mMyAuthcodeTextSize=typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }
        typedArray.recycle();
        mPaint=new Paint();
        mPaint.setTextSize(mMyAuthcodeTextSize);
        mRect=new Rect();
        mPaint.getTextBounds(mMyAuthcodeText, 0, mMyAuthcodeText.length(), mRect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);
        mPaint.setColor(mMyAuthcodeTextColor);
        canvas.drawText(mMyAuthcodeText, getWidth() / 2 - mRect.width() / 2, getHeight() / 2 + mRect.height()/2, mPaint);

    }
    private String randomText() {
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < 4)
        {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : set)
        {
            sb.append("" + i);
        }

        return sb.toString();

    }

    public String getmMyAuthcodeText() {
        return mMyAuthcodeText;
    }

    public void setmMyAuthcodeText(String mMyAuthcodeText) {
        this.mMyAuthcodeText = mMyAuthcodeText;
    }
}
