package com.example.shihao.afinal;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShiHao on 2016/12/13.
 */

public class CityFragment extends Fragment {
    private ImageButton button;
    private EditText editText;

    private Bitmap[] bitmap;
    private String[] all_str;
    private String[][] all_data;
    private ListView listView;

    private List<Map<String ,Object>> data;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        button = (ImageButton)getActivity().findViewById(R.id.search_btn);
        listView = (ListView)getActivity().findViewById(R.id.lv);
        editText = (EditText)getActivity().findViewById(R.id.search_text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();
                sendRequestWithHttpURLConnection(str);
            }
        });

    }
    private void sendRequestWithHttpURLConnection(final String Str){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                String url = "http://172.19.51.194:8080/android_login/search.jsp";
                try{
                    Log.i("key","Begin the connection");
                    connection = (HttpURLConnection)((new URL(url.toString()).openConnection()));
                    connection.setRequestMethod("POST");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    String request = URLEncoder.encode(Str,"utf-8");
                    out.writeBytes("searchContent="+request);
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
                    if(msg.equals("")){
                        throw new RuntimeException();
                    }
                    //Bitmap 获取图片
                    String temp_url = "http://172.19.51.194:8080/android_login/file/";
                    bitmap = new Bitmap[msg.split(";").length];
                    for(int i=0;i<msg.split(";").length;i++){
                        bitmap[i] = getPicture(temp_url+msg.split(";")[i].split(" ")[4]);
                    }
                    Message message = new Message();
                    message.what = 1;
                    message.obj = msg;
                    handler.sendMessage(message);

                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler handler = new Handler(){
        public void handleMessage(final Message message){
            switch (message.what){
                case 0:
                    Toast.makeText(getActivity(),"查询结果为空",Toast.LENGTH_LONG).show(); break;
                case 1:
                    all_str = message.obj.toString().split(";");
                    int len = all_str.length;
                    all_data = new String[len][];
                    for(int i=0;i<len;i++){
                        all_data[i] = all_str[i].split(" ");
                    }
                    //ListView
                    listView = (ListView)getActivity().findViewById(R.id.lv);
                    data = new ArrayList<>();
                    for(int i=0;i<len;i++){
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
                    listView.setAdapter(simpleAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(),good_detail.class);
                            //Bundle bundle = new Bundle();
                            //bundle.putStringArray("data",all_data[position]);
                            //bundle.putInt("pic",pic[position]);
                            //bundle.putString("user_name",user_name[position]);
                            //bundle.putString("good_name",good_name[position]);
                            //bundle.putString("price",price[position]);
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
}