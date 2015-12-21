package com.example.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.news.newsreaderclient.R;


/*
* 自定义控件实现及监听
* */
public class MyNewsTitle extends RelativeLayout implements View.OnClickListener{
    private LayoutInflater mInflater;
    private OnClickListener mClickListener;
    private Drawable firstImg, twoImg, threeImg;
    public void setMyViewOnClickListener(OnClickListener l){   //activity中注册所调用的方法，接口采用系统定义接口（也可自定义一个接口）
        mClickListener = l;
    }

    public MyNewsTitle(Context context) {
        super(context);
    }

    public MyNewsTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public MyNewsTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

   public void init(Context context, AttributeSet attrs){
       mInflater = LayoutInflater.from(context);
       View v = mInflater.inflate(R.layout.news_title_layout,this,true);    //联系上下文解析原始布局
       TypedArray a = null;
       try {
           a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.MyNewsTitle,0,0);
           //获取新布局中引用自定义控件设置的值
           firstImg = a.getDrawable(R.styleable.MyNewsTitle_title_first);
          twoImg = a.getDrawable(R.styleable.MyNewsTitle_title_two);
          threeImg = a.getDrawable(R.styleable.MyNewsTitle_title_three);
       }finally {
           a.recycle();   //回收
       }


       if (firstImg != null){
           ImageView first = (ImageView) v.findViewById(R.id.title_first_txt);   //设置值在原始布局中
           first.setImageDrawable(firstImg);
           first.setOnClickListener(this);     //此处采用系统接口注册
       }
       if (twoImg != null){
           ImageView two = (ImageView) v.findViewById(R.id.title_two_txt);   //设置值在原始布局中
           two.setImageDrawable(twoImg);
           two.setOnClickListener(this);     //此处采用系统接口注册

       }

       if (threeImg != null){
           ImageView three = (ImageView) v.findViewById(R.id.title_three_txt);   //设置值在原始布局中
           three.setImageDrawable(threeImg);
           three.setOnClickListener(this);     //此处采用系统接口注册
       }
   }

    @Override
    public void onClick(View v) {
        if (mClickListener != null){
            mClickListener.onClick(v);
        }
    }
}
