package com.example.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.news.newsreaderclient.R;
import com.example.news.newsreaderclient.ThirdFragment;

import java.util.ArrayList;

public class ThirdActivity extends FragmentActivity {
    private ArrayList<String> mImageUrl;
    private ViewPager mViewPager;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_layout);
        ArrayList<Fragment> list = new ArrayList<>();
        mViewPager = (ViewPager) findViewById(R.id.content_viewpager_third);
        ThirdFragmentAdatper adatper = new ThirdFragmentAdatper(getSupportFragmentManager());
        mViewPager.setAdapter(adatper);

        Intent intent = getIntent();
        mImageUrl = intent.getStringArrayListExtra("image");
        int len = intent.getIntExtra("num",0);
        for (String url:mImageUrl){
            i++;
            list.add(ThirdFragment.newInstance(url,i+"/"+len));
        }
        adatper.setDataFragmentList(list);   //设置数据源
    }


    /*
   * 自定义fragment的viewpager适配器
   * */
    public class ThirdFragmentAdatper extends FragmentPagerAdapter {
        ArrayList<Fragment> dataFragmentList = new ArrayList<Fragment>();

        public void setDataFragmentList(ArrayList<Fragment> data) {
            dataFragmentList = data;
            notifyDataSetChanged();
        }

        public ThirdFragmentAdatper(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return dataFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return dataFragmentList.size();
        }
    }
}
