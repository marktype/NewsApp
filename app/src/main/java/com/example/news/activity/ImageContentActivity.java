package com.example.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news.newsreaderclient.ImageContentFragment;
import com.example.news.newsreaderclient.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageContentActivity extends FragmentActivity {
    private String url = "http://api.sina.cn/sinago/article.json?postt=hdpic_hdpic_toutiao_4&wm=b207&from=6042095012&chwm=12050_0001&oldchwm=12050_0001&imei=867064013906290&uid=802909da86d9f5fc&id=";
    private ViewPager mViewPager;
    private ImageFragmentAdatper adapter;
    private TextView mTitle;
    private ImageView mBackImage;
//    private TextView mImageNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_content_layout);
        mViewPager = (ViewPager) findViewById(R.id.Image_content_viewpager);
//        mImageNum = (TextView) findViewById(R.id.image_content_num);
        mTitle = (TextView) findViewById(R.id.image_title);
        mBackImage = (ImageView) findViewById(R.id.image_title_back);


        adapter = new ImageFragmentAdatper(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);


        Intent intent = getIntent();
        String urlId = intent.getStringExtra("urlId");
        urlId = url+urlId;
        connetImageGetStr(urlId);

        mBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());  //设置动画
    }
    /*
    * 自定义实现viewpager动画继承PageTransformer实现transformPage方法
    * */
    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }


    public void connetImageGetStr(String url) {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(this, url, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                Toast.makeText(ImageContentActivity.this, "链接失败，请检查你的网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int i, String s) {
                super.onSuccess(i, s);
                ArrayList<Fragment> list = parseImageData(s);
                adapter.setDataFragmentList(list);
//                mProgress.setVisibility(View.GONE);


            }

        });

    }

    public ArrayList<Fragment> parseImageData(String str) {

        ArrayList<Fragment> list = new ArrayList<Fragment>();
        try {
            JSONObject object = new JSONObject(str);
            JSONObject data = object.getJSONObject("data");
            String ImageCotentTitle = data.getString("title");
            mTitle.setText(ImageCotentTitle);
            JSONArray array = data.getJSONArray("pics");
            int len = array.length();
            for (int i = 0;i<len;i++){
//                HashMap<String, Object> item = new HashMap<String, Object>();
                JSONObject listData = array.getJSONObject(i);

                String title = listData.getString("alt");
                String pic = listData.getString("kpic");
                String num = (i+1)+"/"+len;

               list.add(ImageContentFragment.newInstance(pic,title,num));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /*
   * 自定义fragment的viewpager适配器
   * */
    public class ImageFragmentAdatper extends FragmentPagerAdapter {
        ArrayList<Fragment> dataFragmentList = new ArrayList<Fragment>();

        public void setDataFragmentList(ArrayList<Fragment> data) {
            dataFragmentList = data;
            notifyDataSetChanged();
        }

        public ImageFragmentAdatper(FragmentManager fm) {
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
