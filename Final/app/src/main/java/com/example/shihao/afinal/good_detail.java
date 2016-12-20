package com.example.shihao.afinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
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

public class good_detail extends AppCompatActivity {

    private TextView d_tel;
    private String[] data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item_detail);

        sendRequestWithHttpURLConnection();

        Intent intent = getIntent();
        data = intent.getStringArrayExtra("data");
        byte [] bis=intent.getByteArrayExtra("bitmap");
        Bitmap bitmap= BitmapFactory.decodeByteArray(bis, 0, bis.length);

        TextView u_name = (TextView)findViewById(R.id.d_user_name);
        TextView g_name = (TextView)findViewById(R.id.d_good_name);
        TextView d_price = (TextView)findViewById(R.id.d_price);
        TextView d_des = (TextView)findViewById(R.id.d_good_des);
        d_tel = (TextView)findViewById(R.id.d_tel);
        ImageView imageView = (ImageView)findViewById(R.id.background);

        imageView.setImageBitmap(bitmap);
        u_name.setText("卖家ID："+data[0]);
        g_name.setText("物品名称："+data[1]);
        d_price.setText("价格："+data[2]);
        d_des.setText("卖家描述："+data[3]);
    }
    private void sendRequestWithHttpURLConnection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                String url = "http://172.19.51.194:8080/android_login/detailPic.jsp";
                try{
                    Log.i("key","Begin the connection");
                    connection = (HttpURLConnection)((new URL(url.toString()).openConnection()));
                    connection.setRequestMethod("POST");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    String request = URLEncoder.encode(data[0],"utf-8");
                    out.writeBytes("username="+request);
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
                    d_tel.setText("联系电话："+message.obj.toString());
            }
        }
    };
}
