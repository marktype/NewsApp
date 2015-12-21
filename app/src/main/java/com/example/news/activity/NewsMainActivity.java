package com.example.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.news.newsreaderclient.ArgumentListContentFragment;
import com.example.news.newsreaderclient.NewsListContentFragment;
import com.example.news.newsreaderclient.R;

import java.util.ArrayList;

public class NewsMainActivity extends FragmentActivity implements
        ViewPager.OnPageChangeListener,NewsListContentFragment.OnFragmentItemListener,
        RadioGroup.OnCheckedChangeListener, ArgumentListContentFragment.OnArgumentItemListener {
    private long mExitTime;
    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_main_layout);
        ArrayList<Fragment> dataFragmentList = new ArrayList<Fragment>();    //定义fragment窗口数据
        mViewPager = (ViewPager) findViewById(R.id.news_viewpager);
        mRadioGroup = (RadioGroup) findViewById(R.id.news_radioGroup);
        MyNewsFragmentAdatper adatper = new MyNewsFragmentAdatper(getSupportFragmentManager());  //实例化适配器
        ininFragmentData(dataFragmentList);    //初始化数据
        adatper.setDataFragmentList(dataFragmentList);   //设置数据源
        mViewPager.setAdapter(adatper);    //设置适配器
        mViewPager.setOnPageChangeListener(this);    //注册页面转换监听
        mRadioGroup.setOnCheckedChangeListener(this);    //注册点击监听

    }





    //初始化viewpager数据
    public void ininFragmentData(ArrayList<Fragment> data) {
        data.add(NewsListContentFragment.newInstance());    //头条
        data.add(ArgumentListContentFragment.newInstance("T1348648517839"));   //娱乐
        data.add(ArgumentListContentFragment.newInstance("T1348649079062"));    //体育
        data.add(ArgumentListContentFragment.newInstance("T1348648756099"));    //财经
        data.add(ArgumentListContentFragment.newInstance("T1348649580692"));    //科技
    }
    



    /*
    * viewpager
    * */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
//                mViewPager.setCurrentItem(0);
                ((RadioButton) mRadioGroup.getChildAt(0)).toggle();
                break;
            case 1:
                ((RadioButton) mRadioGroup.getChildAt(1)).toggle();
                break;
            case 2:
                ((RadioButton) mRadioGroup.getChildAt(2)).toggle();
                break;
            case 3:
                ((RadioButton) mRadioGroup.getChildAt(3)).toggle();
                break;
            case 4:
                ((RadioButton) mRadioGroup.getChildAt(4)).toggle();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /*
    * 头条监听接口方法
    * */
    @Override
    public void onFragmentInteraction(String url) {
        Intent intent = new Intent(this,MainContentActivity.class);
        intent.putExtra("url",url);
//        intent.putExtra("title",title);
//        intent.putExtra("image",image);
//        intent.putExtra("ptime",ptime);
//        intent.putExtra("source",source);
        startActivity(intent);
    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.raidoTab1:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.raidoTab2:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.raidoTab3:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.raidoTab4:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.raidoTab5:
                mViewPager.setCurrentItem(4);
                break;

        }
    }
     /*
    * 娱乐监听接口方法
    * */
    @Override
    public void onFragmentArgument(String url) {
        Intent intent = new Intent(this,MainContentActivity.class);
        intent.putExtra("url",url);
//        intent.putExtra("title",title);
//        intent.putExtra("image",image);
        startActivity(intent);
    }

    /*
    * 自定义fragment的viewpager适配器
    * */
    public class MyNewsFragmentAdatper extends FragmentPagerAdapter {
        ArrayList<Fragment> dataFragmentList = new ArrayList<Fragment>();

        public void setDataFragmentList(ArrayList<Fragment> data) {
            dataFragmentList = data;
            notifyDataSetChanged();
        }

        public MyNewsFragmentAdatper(FragmentManager fm) {
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出新闻浏览", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
