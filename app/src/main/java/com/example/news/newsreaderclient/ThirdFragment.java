package com.example.news.newsreaderclient;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdFragment extends Fragment {
    private String mUrl,mLen;
    private ImageView mImage;
    private ProgressBar mProgress;
    private TextView mNumTxt;
    public static ThirdFragment newInstance(String url,String len) {

        Bundle args = new Bundle();
        args.putString("url",url);
        args.putString("num",len);
        ThirdFragment fragment = new ThirdFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    if (getArguments() != null){
        mUrl = getArguments().getString("url");
        mLen = getArguments().getString("num");
    }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_third_layout, container, false);
        mImage = (ImageView) v.findViewById(R.id.content_image_third);
        mProgress = (ProgressBar) v.findViewById(R.id.Image_progress_img_third);
        mNumTxt = (TextView) v.findViewById(R.id.image_num_third);
        mNumTxt.setText(mLen);
        getImage();   //加载图片
        return v;
    }

    public void getImage(){

        Picasso.with(getActivity()).load(mUrl).into(mImage, new Callback() {
            @Override
            public void onSuccess() {
                mProgress.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
    }
}
