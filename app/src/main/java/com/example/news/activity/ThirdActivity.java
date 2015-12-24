package com.example.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

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


        mViewPager.setPageTransformer(true, new DepthPageTransformer());    //设置动画
    }
    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
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
