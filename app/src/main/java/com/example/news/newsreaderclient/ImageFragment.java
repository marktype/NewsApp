package com.example.news.newsreaderclient;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.util.Logs;
import com.example.util.imagecache.AsyncMemoryFileCacheImageLoader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment implements AdapterView.OnItemClickListener{
    private ImageAdapter adapter;
//    private String url = "http://api.sina.cn/sinago/list.json?channel=hdpic_toutiao&adid=4ad30dabe134695c3b7c3a65977d7e72&wm=b207&from=6042095012&chwm=12050_0001&oldchwm=&imei=867064013906290&uid=802909da86d9f5fc&p=";
    private ListView mList;
    private String mUrl;
    private ProgressBar mProgress;
    private OnImageListener mListener;
    private AsyncMemoryFileCacheImageLoader mCacheImageLoader;
    public static ImageFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString("url",url);
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCacheImageLoader = AsyncMemoryFileCacheImageLoader.getInstance(getActivity());
        if (getArguments() != null){
            mUrl = getArguments().getString("url");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_item_layout, container, false);
        adapter = new ImageAdapter(getActivity());
        mList = (ListView) v.findViewById(R.id.news_image_listview);
        mList.setOnItemClickListener(this);
        mList.setAdapter(adapter);
        mProgress = (ProgressBar) v.findViewById(R.id.image_progress);
        mProgress.setVisibility(View.VISIBLE);
        connetImageGetStr(mUrl);
        return v;
    }


    public void connetImageGetStr(String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        //此处网址要填写正确
//        String url = "http://c.m.163.com/nc/article/headline/T1348647909107/" + page + "-" + pageSize + ".html";

        client.get(getActivity(), url, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                Toast.makeText(getActivity(), "链接失败，请检查你的网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int i, String s) {
                super.onSuccess(i, s);
                ArrayList<HashMap<String, Object>> list = parseImageData(s);
                    adapter.setList(list);
                mProgress.setVisibility(View.GONE);


            }

        });

    }

    public void onButtonPressed(String url) {
        if (mListener != null) {
            mListener.onFragmentInteraction(url);
        }
    }
    public interface OnImageListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String url);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnImageListener) {
            mListener = (OnImageListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String ,Object> item = (HashMap<String, Object>) adapter.getItem(position);
        String imageId = (String) item.get("id");
        Logs.e("0000000000----------id"+imageId);

        onButtonPressed(imageId);


    }

    public class ImageAdapter extends BaseAdapter{
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

        private LayoutInflater inflater;

        public ImageAdapter(Context context) {

            inflater = LayoutInflater.from(context);
        }

        public void setList(ArrayList<HashMap<String, Object>> list) {
            this.list = list;
            notifyDataSetChanged();// 更新数据
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
          if (convertView == null){
              viewHolder = new ViewHolder();
              convertView = inflater.inflate(R.layout.image_item_layout,null);
              viewHolder.Imageitem = (ImageView) convertView.findViewById(R.id.image_item_image);
              viewHolder.contentTxt = (TextView) convertView.findViewById(R.id.image_item_title);
              convertView.setTag(viewHolder);
          }else{
              viewHolder = (ViewHolder) convertView.getTag();
          }
          HashMap<String ,Object> item = (HashMap<String, Object>) getItem(position);

           mCacheImageLoader.loadBitmap(getResources(),(String)item.get("pic"),viewHolder.Imageitem,R.mipmap.base_list_default_icon,0,250);

//            Picasso.with(getActivity()).load((String) item.get("pic")).into(viewHolder.Imageitem);
            viewHolder.contentTxt.setText((CharSequence) item.get("title"));


            return convertView;
        }
        class ViewHolder {
            ImageView Imageitem;
            TextView contentTxt;
        }
    }

    /*
     * 解析数据信息,获取数据源
	 */
    public ArrayList<HashMap<String, Object>> parseImageData(String str) {

        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        try {
            JSONObject object = new JSONObject(str);
            JSONObject data = object.getJSONObject("data");
            JSONArray array = data.getJSONArray("list");
            int len = array.length();
            for (int i = 0;i<len;i++){
                HashMap<String, Object> item = new HashMap<String, Object>();
                JSONObject listData = array.getJSONObject(i);
                String id = listData.getString("id");
                String title = listData.getString("title");
                String pic = listData.getString("kpic");

                item.put("id",id);
                item.put("title",title);
                item.put("pic",pic);
                list.add(item);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}
