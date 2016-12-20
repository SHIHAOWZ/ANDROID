package com.example.shihao.afinal;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShiHao on 2016/12/13.
 */

public class HomeFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager viewPager1,viewPager2;

    private ImageView[] tips;
    private ImageView[] mImageViews;
    private int[] imgIdArray ;

    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private View view1, view2;
    private List<View> mViewList = new ArrayList<>();//页卡视图集合

    private List<Map<String ,Object>> data;
    private Context context = null;

    private String[] all_str;
    private String[][] all_data;
    private Bitmap[] bitmap;
    private ListView listView1,listView2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context=getActivity().getApplicationContext();
        viewPager1 = (ViewPager)getActivity().findViewById(R.id.vp1);
        mTabLayout = (TabLayout)getActivity().findViewById(R.id.tabs);
        viewPager2 = (ViewPager)getActivity().findViewById(R.id.vp2);
        ViewGroup group = (ViewGroup)getActivity().findViewById(R.id.viewGroup);
        //viewPager1
        imgIdArray = new int[]{R.drawable.ex1,R.drawable.ex2};

        sendRequestWithHttpURLConnection();

        tips = new ImageView[imgIdArray.length];
        for(int i=0; i<tips.length; i++){
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10,10));
            tips[i] = imageView;
            if(i == 0){
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            group.addView(imageView, layoutParams);
        }
        mImageViews = new ImageView[imgIdArray.length];
        for(int i=0; i<mImageViews.length; i++){
            ImageView imageView = new ImageView(context);
            mImageViews[i] = imageView;
            imageView.setBackgroundResource(imgIdArray[i]);
        }
        //设置Adapter
        viewPager1.setAdapter(new MyAdapter());
        //设置监听，主要是设置点点的背景
        viewPager1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setImageBackground(position % mImageViews.length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
        viewPager1.setCurrentItem((mImageViews.length) * 100);

        //viewPager2
        mInflater = LayoutInflater.from(getActivity());
        view1 = mInflater.inflate(R.layout.fragment_home_detail, null);
        view2 = mInflater.inflate(R.layout.fragment_home_detail, null);


        mViewList.add(view1);
        mViewList.add(view2);
        mTitleList.add("商品区");
        mTitleList.add("求助区");

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));

        MyPagerAdapter mAdapter = new MyPagerAdapter(mViewList);
        viewPager2.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(viewPager2);//将TabLayout和ViewPager关联起来。

    }

    class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public MyPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();//页卡数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));//删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);//页卡标题
        }
    }

    public class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }
        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            try {
                ((ViewPager)container).addView(mImageViews[position % mImageViews.length], 0);
            }catch(Exception e){
            }
            return mImageViews[position % mImageViews.length];
        }

    }
    /**
     * 设置选中的tip的背景
     */

    private void setImageBackground(int selectItems){
        for(int i=0; i<tips.length; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
        }
    }
    public Bitmap getPicture(String path){
        Bitmap bm=null;
        try{
            URL url=new URL(path);
            URLConnection connection=url.openConnection();
            connection.connect();
            InputStream inputStream=connection.getInputStream();
            bm= BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  bm;
    }

    private void sendRequestWithHttpURLConnection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                String url = "http://172.19.51.194:8080/android_login/home.jsp";
                try{
                    Log.i("key","Begin the connection");
                    connection = (HttpURLConnection)((new URL(url.toString()).openConnection()));
                    connection.setRequestMethod("POST");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    //request = URLEncoder.encode(request,"utf-8");
                    String parameter = "";
                    out.writeBytes(parameter);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine())!=null){
                        response.append(line);
                    }
                    Log.i("key",response.toString());
                    if(connection!=null){
                        connection.disconnect();
                    }
                    String msg = "";
                    //Log.i("key",response.toString());
                    msg = response.toString();

                    //Bitmap 获取图片
                    String temp_url = "http://172.19.51.194:8080/android_login/file/";
                    bitmap = new Bitmap[8];
                    for(int i=0;i<8;i++){
                        bitmap[i] = getPicture(temp_url+msg.split(";")[i].split(" ")[4]);
                    }

                    Message message = new Message();
                    message.what = 1;
                    message.obj = msg;
                    handler.sendMessage(message);


                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler handler = new Handler(){
        public void handleMessage(final Message message){
            switch (message.what){
                case 1:
                    all_str = message.obj.toString().split(";");
                    all_data = new String[8][];
                    for(int i=0;i<8;i++){
                        all_data[i] = all_str[i].split(" ");
                    }
                    //ListView
                    listView1 = (ListView)view1.findViewById(R.id.list);
                    listView2 = (ListView)view2.findViewById(R.id.list);
                    data = new ArrayList<>();
                    for(int i=0;i<8;i++){
                        Map<String,Object> tmp = new LinkedHashMap<>();
                        tmp.put("pic",bitmap[i]);
                        tmp.put("user_name","卖家ID："+ all_data[i][0]);
                        tmp.put("good_name","物品名称："+ all_data[i][1]);
                        tmp.put("price","价格："+ all_data[i][3]);
                        data.add(tmp);
                    }
                    SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),data,R.layout.list_item,new String[]{"pic","user_name","good_name","price"},new int[]{R.id.pic,R.id.user_name,R.id.good_name,R.id.price});
                    simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                        @Override
                        public boolean setViewValue(View view, Object data, String textRepresentation) {
                            if(view instanceof ImageView && data instanceof Bitmap){
                                ImageView i = (ImageView)view;
                                i.setImageBitmap((Bitmap) data);
                                return true;
                            }
                            return false;
                        }
                    });
                    listView1.setAdapter(simpleAdapter);
                    listView2.setAdapter(simpleAdapter);

                    listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(),good_detail.class);
                            ByteArrayOutputStream stream=new ByteArrayOutputStream();
                            bitmap[position].compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte [] bitmapByte =stream.toByteArray();
                            intent.putExtra("data",all_data[position]);
                            intent.putExtra("bitmap",bitmapByte);
                            startActivity(intent);
                        }
                    });
            }
        }
    };
}

   /* public class HomeFragment extends Fragment {
        ImageButton IB;
        //MainActivity parentActivity = (MainActivity) getActivity();
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view =  inflater.inflate(R.layout.fragment_home, container, false);
            IB = (ImageButton)view.findViewById(R.id.imageButton);
            IB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"hhhhhhh",Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }
    }*/