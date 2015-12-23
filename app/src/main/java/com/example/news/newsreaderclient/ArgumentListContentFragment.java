package com.example.news.newsreaderclient;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.util.Logs;
import com.example.util.WriteAndReadFile;
import com.example.util.view.XListView;
import com.example.util.view.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.scxh.slider.library.SliderLayout;
import com.scxh.slider.library.SliderTypes.BaseSliderView;
import com.scxh.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArgumentListContentFragment extends Fragment implements IXListViewListener, AdapterView.OnItemClickListener {
    private String[] mImage = new String[1];
    private String[] mString = new String[1];
    private View v,mView;
    private NewsListAdapter adapter;    //适配器
    XListView mContentList;     //声明列表xlistview
    private String url_3w, docid, title, imgsrc;
    private int currentPage = 0;   //当前页
    private ProgressBar mProgress;    //没有加载时缓冲的圈
    private Handler mHandler;
    public static final int TYPE_ONE_IMAGE = 0;    //列表中0显示1张图
    public static final int TYPE_THREE_IMAGE = 1;   //1显示3张图
    private SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd-HH:mm:ss");
    private LruCache<String, Bitmap> mMemoryCache;      //声明缓存对象
    private OnArgumentItemListener mListener;     //监听回调接口
    private Boolean isUp = false;    //数据是刷新还是加载
    LayoutInflater mInflater;

    private String urlId = "";     //解析某种新闻id号
    private WriteAndReadFile mGetFile;
    public static ArgumentListContentFragment newInstance(String id) {
        Bundle args = new Bundle();

        args.putCharSequence("id",id);
        ArgumentListContentFragment fragment = new ArgumentListContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            urlId = getArguments().getString("id");
        }

        mGetFile = new WriteAndReadFile();
        mGetFile.mFilePath = getActivity().getFilesDir().getAbsolutePath();
        initLruCache();
        mHandler = new Handler();
        mInflater = LayoutInflater.from(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_news_list_content_layout, container, false);

        adapter = new NewsListAdapter(getActivity());
        mContentList = (XListView) v.findViewById(R.id.news_content_listview);
        mProgress = (ProgressBar) v.findViewById(R.id.news_item_progress);
        mContentList.setAdapter(adapter);       //设置适配器
        mContentList.setPullLoadEnable(true);     //实现上拉加载
        mContentList.setPullRefreshEnable(true);   //实现下拉刷新
        mContentList.setEmptyView(mProgress);    //listview没有加载上时启动
        mContentList.setXListViewListener(this);    //注册上下拉刷新事件
        mContentList.setOnItemClickListener(this);   //注册item点击事件

        connetHttpGetStr(currentPage);   //联网取数据
        mView = mInflater.inflate(R.layout.imageslider_layout, null);
        mContentList.addHeaderView(mView);
        return v;
    }

    /*
    * imageSlider控件加入
    * */
    public void getSliderLayoutView(String[] mImage, final String[] mString) {
//        if (mView == null){
//            mView = mInflater.inflate(R.layout.imageslider_layout, null);
//        }
        SliderLayout mSliderLayout = (SliderLayout) mView.findViewById(R.id.image_slider_layout);
        mSliderLayout.removeAllSliders();
        int length = mImage.length;
        for (int i = 0; i < length; i++) {
            TextSliderView sliderView = new TextSliderView(getActivity());   //向SliderLayout中添加控件
            sliderView.image(mImage[i]);
            sliderView.description(mString[i]);
            final int finalI = i;
            sliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    Toast.makeText(getActivity(),mString[finalI],Toast.LENGTH_SHORT).show();
                }
            });

            mSliderLayout.addSlider(sliderView);
        }
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);  //将小圆点设置到右下方

//        return mView;
    }

    public void connetHttpGetStr(int pageNo) {
        AsyncHttpClient client = new AsyncHttpClient();
        int pageSize = 20;
        int page = pageNo * pageSize;
        //此处网址要填写正确
        final String url = "http://c.m.163.com/nc/article/headline/"+urlId+"/"+ page + "-" + pageSize + ".html";
        Logs.e("url--------------------------------"+url);
        client.get(getActivity(), url, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                Toast.makeText(getActivity(),"链接失败，请检查你的网络",Toast.LENGTH_SHORT).show();
                String data = mGetFile.readFile(url);
                if (data != null){
                    ArrayList<HashMap<String, Object>> list = parseHttpData(data);
                    adapter.setList(list);
                }

            }

            @Override
            public void onSuccess(int i, String s) {
                super.onSuccess(i, s);
                ArrayList<HashMap<String, Object>> list = parseHttpData(s);
                getSliderLayoutView(mImage, mString);
                mGetFile.writeFile(url,s);
                if (isUp) {
                    adapter.setList(list);
                    isUp = false;
                    onLoad();
                } else {
                    adapter.addList(list);
                    onLoad();
                }

//                if (mView == null){
//                    mView = getSliderLayoutView(mImage, mString);  //自动轮播加载数据
//                }
//                //自动轮播
//                mContentList.addHeaderView(mView);

            }

        });
    }

    /*
     * 下载图片
	 */
    public Bitmap downLoadImage(String httpUrl) {
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            InputStream is = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void onButtonPressed(String url) {
        if (mListener != null) {
            mListener.onFragmentArgument(url);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnArgumentItemListener) {
            mListener = (OnArgumentItemListener) context;
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
        HashMap<String, Object> doc = (HashMap<String, Object>) adapter.getItem(position-2);
        String url = (String) doc.get("docid");
        onButtonPressed(url);

    }

    public interface OnArgumentItemListener {
        // TODO: Update argument type and name
        void onFragmentArgument(String url);
    }

    /*
     * 自定义适配器
	 */
    public class NewsListAdapter extends BaseAdapter {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        private LayoutInflater inflater;

        public NewsListAdapter(Context context) {

            inflater = LayoutInflater.from(context);
        }

        public void setList(ArrayList<HashMap<String, Object>> list) {
            this.list = list;
            notifyDataSetChanged();// 更新数据
        }

        public void addList(ArrayList<HashMap<String, Object>> list) {
            this.list.addAll(list);
            notifyDataSetChanged();// 更新数据
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;

        }

        //多个布局时重写下面两个方法
        @Override
        public int getItemViewType(int position) {
            HashMap<String,Object> itemType = (HashMap<String, Object>) getItem(position);
            int type = (int) itemType.get("type");

            if (TYPE_ONE_IMAGE == type) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            ;
            HashMap<String, Object> item = (HashMap<String, Object>) getItem(position);
            String title = (String) item.get("title");
            String imageFirst = (String) item.get("imgsrc");

            String digestFlag = (String) item.get("digest");
            int type = getItemViewType(position);
            if (type == 0) {
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.content_adapter_layout, null);
                    viewHolder.iconImageitem = (ImageView) convertView.findViewById(R.id.adapter_img);
                    viewHolder.title_view = (TextView) convertView.findViewById(R.id.adapter_title_txt);
                    viewHolder.content_view = (TextView) convertView.findViewById(R.id.adapter_content_txt);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                loadBitmap(getResources(), imageFirst, viewHolder.iconImageitem, R.mipmap.base_list_default_icon);
                viewHolder.title_view.setText(title);
                viewHolder.content_view.setText(digestFlag);

            } else {
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.three_image_item_layout, null);
                    viewHolder.image_title = (TextView) convertView.findViewById(R.id.image_three_title_txt);
                    viewHolder.image_one = (ImageView) convertView.findViewById(R.id.image_one_img);
                    viewHolder.image_two = (ImageView) convertView.findViewById(R.id.image_two_img);
                    viewHolder.image_three = (ImageView) convertView.findViewById(R.id.image_three_img);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.image_title.setText(title);
                loadBitmap(getResources(), imageFirst, viewHolder.image_one, R.mipmap.base_list_default_icon);
                loadBitmap(getResources(), (String) item.get("imageTwo"), viewHolder.image_two, R.mipmap.base_list_default_icon);
                loadBitmap(getResources(), (String) item.get("imageThree"), viewHolder.image_three, R.mipmap.base_list_default_icon);

            }


            return convertView;
        }

        class ViewHolder {
            ImageView iconImageitem, image_one, image_two, image_three;
            TextView content_view, title_view, image_title;
        }

    }

    /**
     * 初始化LRUCache
     */
    public void initLruCache() {
        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 4;
        // 设置图片缓存大小为程序最大可用内存的1/4
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    /**
     * 添加图片到内存缓存
     */
    public void addBitmapToCache(String url, Bitmap bitmap) {
        if (getCacheBitmap(url) == null) {
            mMemoryCache.put(url, bitmap);
        }
    }

    /**
     * 从缓存中获取图片
     *
     * @param url
     * @return
     */
    public Bitmap getCacheBitmap(String url) {
        Bitmap bitmap = mMemoryCache.get(url);
        return bitmap;
    }

    /**
     * 通常类似 ListView 与 GridView 等视图组件在使用上面演示的AsyncTask 方法时会同时带来另外一个问题。
     * 为了更有效的处理内存，那些视图的子组件会在用户滑动屏幕时被循环使用。如果每一个子视图都触发一个AsyncTask ，
     * 那么就无法确保当前视图在结束task时，分配的视图已经进入循环队列中给另外一个子视图进行重用。 而且, 无法确保所有的
     * 异步任务能够按顺序执行完毕。
     *
     * @param imageUrl
     * @param imageView
     * @param resId     默认图片资源
     */
    public void loadBitmap(Resources res, String imageUrl, ImageView imageView,
                           int resId) {

        // 第一步：根据图片地址，判断图片是否被缓存在内存
        Bitmap bitmap = getCacheBitmap(imageUrl);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        if (cancelPotentialWork(imageUrl, imageView)) {
            BitmapWorkerTask task = new BitmapWorkerTask(imageView);

            AsyncDrawable asyncDrawable = new AsyncDrawable(res,
                    BitmapFactory.decodeResource(res, resId), task);
            imageView.setImageDrawable(asyncDrawable);

            task.execute(imageUrl);
        }
    }

    public static boolean cancelPotentialWork(String imageUrl,
                                              ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.data;
            if (!bitmapData.equals(imageUrl)) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was
        // cancelled
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    /**
     * 创建一个专用的Drawable的子类来储存返回工作任务的引用。在这种情况下，当任务完成时BitmapDrawable会被使用
     */
    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(
                    bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return (BitmapWorkerTask) bitmapWorkerTaskReference.get();
        }
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;// 弱引用
        // ，特点当内存不够时，自动回收
        private String data = "";

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage
            // collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            data = params[0];
            return downLoadImage(data);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = (ImageView) imageViewReference
                        .get();
                final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                    addBitmapToCache(data, bitmap);   //添加图片到缓存
                }
            }
        }
    }

    private void onLoad() {
        mContentList.stopRefresh();
        mContentList.stopLoadMore();
        mContentList.setRefreshTime(format.format(new Date(System.currentTimeMillis())));  //设置当前时间
    }

    @Override
    public void onRefresh() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                currentPage = 0;
                isUp = true;
                connetHttpGetStr(currentPage);
                Logs.e("shuaxin-----");
            }
        });

    }

    @Override
    public void onLoadMore() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                connetHttpGetStr(++currentPage);
                Logs.e("jiazai----");
            }
        });

    }

    /*
     * 解析数据信息,获取数据源
	 */
    public ArrayList<HashMap<String, Object>> parseHttpData(String str) {
//        if (mView != null){
//            Logs.e("111111111111");
//            mContentList.removeHeaderView(mView);    //加载时移除view
//
//        }
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        try {
            JSONObject object = new JSONObject(str);
            JSONArray array = object.getJSONArray(urlId);

            JSONObject stu = array.getJSONObject(0);
            JSONArray adsImage = stu.getJSONArray("ads");
            for (int k = 0; k < adsImage.length(); k++) {
                JSONObject adsStr = adsImage.getJSONObject(k);
                mImage[k] = adsStr.getString("imgsrc");
                mString[k] = adsStr.getString("title");
            }

            int len = array.length();
            for (int i = 1; i < len; i++) {
                final HashMap<String, Object> item = new HashMap<String, Object>();
                JSONObject student = array.getJSONObject(i);
                String digest = student.getString("digest");  //简要内容
//                     url_3w = student.getString("url_3w");   //全篇新闻地址
                docid = student.getString("docid");   //文本id
                title = student.getString("title");    //新闻题目
                item.put("type",TYPE_ONE_IMAGE);

                //多张图解析
                if (digest.equals("")) {
                    JSONArray image = student.getJSONArray("imgextra");

                    JSONObject img = image.getJSONObject(0);
                    String imageTwo = img.getString("imgsrc");
                    JSONObject img1 = image.getJSONObject(1);
                    String imageThree = img1.getString("imgsrc");
                    item.put("imageTwo", imageTwo);
                    item.put("imageThree", imageThree);
                    item.put("type",TYPE_THREE_IMAGE);
                }


                imgsrc = student.getString("imgsrc");    //单张图片（简要图）

//                item.put("url_3w", url_3w);
                item.put("digest", digest);
                item.put("docid", docid);
                item.put("title", title);
                item.put("imgsrc", imgsrc);


                list.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


}
