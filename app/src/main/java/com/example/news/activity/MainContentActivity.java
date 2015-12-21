package com.example.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news.newsreaderclient.R;
import com.example.util.MyNewsTitle;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainContentActivity extends Activity  {
    private TextView mTitleTxt,mContentTxt,mSource;
    private ImageView mImgae;
    private String mUrl;
    int len;
    private MyNewsTitle mTitle;
    private ProgressBar mContentProgress,mCenterProgress;
    private TextView mNumTxt;
    ArrayList<String> picture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_content_layout);
        mTitleTxt = (TextView) findViewById(R.id.news_content_title);
        mContentTxt = (TextView) findViewById(R.id.news_content_content);
        mSource = (TextView) findViewById(R.id.news_content_come);
        mContentProgress = (ProgressBar) findViewById(R.id.layout_progress);
        mNumTxt = (TextView) findViewById(R.id.layout_image_num);
        mCenterProgress = (ProgressBar) findViewById(R.id.content_progress);
        mImgae = (ImageView) findViewById(R.id.news_content_image);
        mTitle = (MyNewsTitle) findViewById(R.id.news_my_back);

        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");

        String httpUrl = "http://c.m.163.com/nc/article/"+mUrl+"/full.html";
        connetHttpGetStr(httpUrl);

        mTitle.setMyViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mImgae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainContentActivity.this,ThirdActivity.class);
                intent.putExtra("image",picture);
                intent.putExtra("num",len);
                 startActivity(intent);
            }
        });

    }

    public void connetHttpGetStr(String url) {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(this, url, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                Toast.makeText(MainContentActivity.this,"链接超时",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int i, String s) {
                super.onSuccess(i, s);

//                String str = parseData(s);

                String image;
               picture  = new ArrayList<String>();

                String body = null;
                try {
                    JSONObject object = new JSONObject(s);
                    JSONObject content = object.getJSONObject(mUrl);
                    body = content.getString("body");
                    mContentTxt.setText(Html.fromHtml(body));    //去掉HTML格式标签
                    JSONArray img = content.getJSONArray("img");
                    String title = content.getString("title");
                    String ptime = content.getString("ptime");
                    String source = content.getString("source");

                    mSource.setText("来源："+source+" "+ptime);
                    mTitleTxt.setText(title);
                    mCenterProgress.setVisibility(View.GONE);    //加载到数据时隐藏
                    len = img.length();

                    if (len != 0){
                        mContentProgress.setVisibility(View.VISIBLE);    //显示缓冲图
                        mNumTxt.setVisibility(View.VISIBLE);
                        mNumTxt.setText("共"+len+"张");
                        for (int j=0 ;j<len ;j++){
                            JSONObject src = img.getJSONObject(j);
                            image = src.getString("src");
                            picture.add(image);    //添加所有图片到集合

                        }
                        Picasso.with(MainContentActivity.this).load(picture.get(0)).into(mImgae, new Callback() {
                            @Override
                            public void onSuccess() {
                                mContentProgress.setVisibility(View.GONE);   //隐藏缓冲图
                            }

                            @Override
                            public void onError() {

                            }
                        });  //设置第一张图显示
                    }else {
                        mImgae.setVisibility(View.GONE);      //没有则隐藏

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
    }





}
