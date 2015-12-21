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


public class ImageContentFragment extends Fragment {
    private String mMessage,mContent,mImageNum;
    private ImageView mImage;
    private TextView mText,mImageNumTxt;
    private ProgressBar mProgress;
    public static ImageContentFragment newInstance(String image,String content,String imageNUm) {
        
        Bundle args = new Bundle();
        args.putString("imageUrl",image);
        args.putString("content",content);
        args.putString("num",imageNUm);
        ImageContentFragment fragment = new ImageContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    if (getArguments() != null){
        mMessage = getArguments().getString("imageUrl");
        mContent = getArguments().getString("content");
        mImageNum = getArguments().getString("num");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_content_layout, container, false);
        mImage = (ImageView) v.findViewById(R.id.image_item_item_image);
        mText = (TextView) v.findViewById(R.id.image_item_content);
        mProgress = (ProgressBar) v.findViewById(R.id.Image_progress_img);
        mImageNumTxt = (TextView) v.findViewById(R.id.image_content_num);

        mImageNumTxt.setText(mImageNum);

        Picasso.with(getActivity()).load(mMessage).into(mImage, new Callback() {
            @Override
            public void onSuccess() {
                mProgress.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
        mText.setText(mContent);

        return v;
    }



}
