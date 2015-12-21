package com.example.news.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news.newsreaderclient.R;
import com.example.util.Logs;

public class TabMainActivity extends TabActivity implements TabHost.OnTabChangeListener{
        private long mExitTime;
    private TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab_my_own_layout);

        tabHost = getTabHost();
        TabHost.TabSpec first = tabHost.newTabSpec("tab1");
        first.setIndicator(setTabMenu("新闻", R.drawable.news_checked_selector));
        first.setContent(new Intent(this, NewsMainActivity.class));


        TabHost.TabSpec second = tabHost.newTabSpec("tab2");
        second.setIndicator(setTabMenu("图片", R.drawable.news_image_selector));
        second.setContent(new Intent(this, ImageActivity.class));

        TabHost.TabSpec three = tabHost.newTabSpec("tab3");
        three.setIndicator(setTabMenu("视频", R.drawable.news_vedio_selector));
        three.setContent(new Intent(this, VedioActivity.class));

        TabHost.TabSpec four = tabHost.newTabSpec("tab4");
        four.setIndicator(setTabMenu("天气", R.drawable.news_weather_selector));
        four.setContent(new Intent(this, WeatherActivity.class));

        TabHost.TabSpec five = tabHost.newTabSpec("tab5");
        five.setIndicator(setTabMenu("地图", R.drawable.news_map_selector));
        five.setContent(new Intent(this, MapActivity.class));

        tabHost.setOnTabChangedListener(this);
        tabHost.addTab(first);
        tabHost.addTab(second);
        tabHost.addTab(three);
        tabHost.addTab(four);
        tabHost.addTab(five);


    }
    public View setTabMenu(String name, int image){
        View v = LayoutInflater.from(this).inflate(R.layout.tab_own_item_layout, null);
        TextView menuText = (TextView) v.findViewById(R.id.tab_menu_txt);
        ImageView menuImg = (ImageView) v.findViewById(R.id.tab_image);
        menuText.setText(name);
        menuImg.setImageResource(image);
        return v;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTabChanged(String tabId) {
        Logs.e("tab-----------"+tabId);
        switch (tabId){
            case "tab1":
                tabHost.setCurrentTab(0);
                break;
            case "tab2":
                tabHost.setCurrentTab(1);
                break;
            case "tab3":
                tabHost.setCurrentTab(2);
                break;
            case "tab4":
                tabHost.setCurrentTab(3);
                break;
            case "tab5":
                tabHost.setCurrentTab(4);
                break;
        }
    }
}
