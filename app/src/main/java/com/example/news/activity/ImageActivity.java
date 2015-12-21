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

import com.example.news.newsreaderclient.ImageFragment;
import com.example.news.newsreaderclient.R;

import java.util.ArrayList;

public class ImageActivity extends FragmentActivity implements ImageFragment.OnImageListener,
        ViewPager.OnPageChangeListener,RadioGroup.OnCheckedChangeListener{
    private long mExitTime;
    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;
    public static final String URL_ONE = "http://api.sina.cn/sinago/list.json?channel=hdpic_toutiao&adid=4ad30dabe134695c3b7c3a65977d7e72&wm=b207&from=6042095012&chwm=12050_0001&oldchwm=&imei=867064013906290&uid=802909da86d9f5fc&p=";
    public static final String URL_TWO = "http://api.sina.cn/sinago/list.json?channel=hdpic_funny&adid=4ad30dabe134695c3b7c3a65977d7e72&wm=b207&from=6042095012&chwm=12050_0001&oldchwm=12050_0001&imei=867064013906290&uid=802909da86d9f5fc&p=";
    public static final String URL_THREE = "http://api.sina.cn/sinago/list.json?channel=hdpic_pretty&adid=4ad30dabe134695c3b7c3a65977d7e72&wm=b207&from=6042095012&chwm=12050_0001&oldchwm=12050_0001&imei=867064013906290&uid=802909da86d9f5fc&p=";
    public static final String URL_FOUR = "http://api.sina.cn/sinago/list.json?channel=hdpic_story&adid=4ad30dabe134695c3b7c3a65977d7e72&wm=b207&from=6042095012&chwm=12050_0001&oldchwm=12050_0001&imei=867064013906290&uid=802909da86d9f5fc&p=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_layout);
        ArrayList<Fragment> dataFragmentList = new ArrayList<Fragment>();    //定义fragment窗口数据
        MyImageFragmentAdatper adatper = new MyImageFragmentAdatper(getSupportFragmentManager());
        ininFragmentData(dataFragmentList);    //初始化数据
        adatper.setDataFragmentList(dataFragmentList);   //设置数据源
        mViewPager = (ViewPager) findViewById(R.id.image_viewpager_view);
        mRadioGroup = (RadioGroup) findViewById(R.id.news_radioGroup_gru);
        mViewPager.setAdapter(adatper);    //设置适配器
        mViewPager.setOnPageChangeListener(this);    //注册页面转换监听
        mRadioGroup.setOnCheckedChangeListener(this);    //注册点击监听
    }

    //初始化viewpager数据
    public void ininFragmentData(ArrayList<Fragment> data) {
        data.add(ImageFragment.newInstance(URL_ONE));    //精选
        data.add(ImageFragment.newInstance(URL_TWO));    //趣图
        data.add(ImageFragment.newInstance(URL_THREE));   //美图
        data.add(ImageFragment.newInstance(URL_FOUR));    //故事

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.raidoTab1_one:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.raidoTab2_two:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.raidoTab3_three:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.raidoTab4_four:
                mViewPager.setCurrentItem(3);
                break;

        }
    }
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

            }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /*
   * 自定义fragment的viewpager适配器
   * */
    public  class MyImageFragmentAdatper extends FragmentPagerAdapter {
        ArrayList<Fragment> dataFragmentList = new ArrayList<Fragment>();

        public void setDataFragmentList(ArrayList<Fragment> data) {
            dataFragmentList = data;
            notifyDataSetChanged();
        }

        public MyImageFragmentAdatper(FragmentManager fm) {
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
                Toast.makeText(this, "再按一次退出图片新闻", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFragmentInteraction(String url) {
        Intent intent = new Intent(this,ImageContentActivity.class);
        intent.putExtra("urlId",url);

        startActivity(intent);


    }
}

