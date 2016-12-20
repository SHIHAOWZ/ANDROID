package com.example.shihao.afinal;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shihao.afinal.settingActivity;

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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShiHao on 2016/12/13.
 */

public class PersonFragment extends Fragment {
    private static final int UPDATE_CONTENT = 0;
    Bundle usermsg;

    Bundle postUserName;
    String headPath;
    Bitmap temp;
    ImageView publish;
    TextView mainUserName;
    TextView mainSetting;
    TextView mainNickname;
    EditText getLoginUsername;
    EditText getLoginPassword;
    EditText getRegUserName;
    EditText getRegPassword;
    EditText getRegPhone;
    Dialog dialog;
    ImageView head;
    AlertDialog.Builder builderLogin;
    private int flag = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainNickname = (TextView) getActivity().findViewById(R.id.name);
        mainUserName = (TextView) getActivity().findViewById(R.id.user);
        mainSetting = (TextView)getActivity().findViewById(R.id.setting);
        head =(ImageView)getActivity().findViewById(R.id.head);
//        点击发布事件
        final TextView sale = (TextView)getActivity().findViewById(R.id.sale);

        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater factory = LayoutInflater.from(getActivity());
                view = factory.inflate(R.layout.fragment_person_upload,null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);
                final Dialog dialogSale = builder.show();
                Button save = (Button)view.findViewById(R.id.save);
                Button back = (Button)view.findViewById(R.id.back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogSale.dismiss();
                    }
                });
            }
        });


        publish = (ImageView)getActivity().findViewById(R.id.tab_post_icon);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == 0){
                    Toast.makeText(getActivity(),"请登录",Toast.LENGTH_SHORT).show();
                }
                else{

                    Intent intent = new Intent(getActivity(), post.class);
                    intent.putExtras(postUserName);
                    startActivity(intent);
                }
            }
        });

        //点击求助事件
        final TextView help = (TextView)getActivity().findViewById(R.id.gethelp);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater factory = LayoutInflater.from(getActivity());
                view = factory.inflate(R.layout.fragment_person_upload,null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);
                final Dialog dialogHelp = builder.show();
                Button save = (Button)view.findViewById(R.id.save);
                Button back = (Button)view.findViewById(R.id.back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogHelp.dismiss();
                    }
                });
            }
        });

        mainSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag == 0){
                    Toast.makeText(getActivity(),"请登录",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(getActivity(), PersonalSetting.class);
                    intent.putExtras(usermsg);
                    startActivity(intent);
                }
            }
        });





        final Button button = (Button) getActivity().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater factory = LayoutInflater.from(getActivity());
                view = factory.inflate(R.layout.fragment_person_log,null);
                builderLogin = new AlertDialog.Builder(getActivity());
                builderLogin.setView(view);
                dialog = builderLogin.show();
                Button login = (Button)view.findViewById(R.id.login);
                Button reg = (Button)view.findViewById(R.id.reg);
                getLoginUsername = (EditText)view.findViewById(R.id.userName);
                getLoginPassword = (EditText)view.findViewById(R.id.password);
                reg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        LayoutInflater factory = LayoutInflater.from(getActivity());
                        view = factory.inflate(R.layout.fragment_person_reg,null);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setView(view);
                        final Dialog dialogReg = builder.show();
                        getRegUserName = (EditText)view.findViewById(R.id.userName);
                        getRegPassword = (EditText)view.findViewById(R.id.password);
                        final EditText confirm = (EditText)view.findViewById(R.id.confirm);
                        getRegPhone = (EditText)view.findViewById(R.id.phone);

                        Button register = (Button) view.findViewById(R.id.reg);
                        register.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(getRegUserName.getText().toString().isEmpty())
                                {
                                    Toast.makeText(getActivity(),"账户名不能为空",Toast.LENGTH_SHORT).show();
                                }
                                else if(getRegPassword.getText().toString().isEmpty())
                                {
                                    Toast.makeText(getActivity(),"密码不能为空",Toast.LENGTH_SHORT).show();
                                }
                                else if(!getRegPassword.getText().toString().equals(confirm.getText().toString()))
                                {
                                    Toast.makeText(getActivity(),"密码不一致",Toast.LENGTH_SHORT).show();
                                }
                                else if(getRegPhone.getText().toString().isEmpty())
                                {
                                    Toast.makeText(getActivity(),"电话不能为空",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    sendRequestWithHttpURLConnection("register");
                                    Toast.makeText(getActivity(),"注册成功",Toast.LENGTH_SHORT).show();
                                    dialogReg.dismiss();
                                }
                            }
                        });


                    }
                });
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendRequestWithHttpURLConnection("login");
                    }
                });
            }
        });
    }
    private void sendRequestWithHttpURLConnection(final String type){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                String url = "";
                String parameter = "";
                try{
                    Log.i("key","Begin the connection");
                    String getusername = getLoginUsername.getText().toString();
                    String getpassword = getLoginPassword.getText().toString();
                    if(type.equals("login"))
                    {
                        url = "http://172.19.51.194:8080/android_login/login.jsp";
                        parameter = "username=" + getusername + "&password="+getpassword;
                    }
                    if(type.equals("register"))
                    {
                        url = "http://172.19.51.194:8080/android_login/register.jsp";
                        parameter = "username="+getRegUserName.getText().toString()+"&password="+getRegPassword.getText().toString()+"&phone="+getRegPhone.getText().toString();
                    }
                    connection = (HttpURLConnection)((new URL(url.toString()).openConnection()));
                    connection.setRequestMethod("POST");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    //  request = URLEncoder.encode(request,"utf-8");
                    out.writeBytes(parameter);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine())!=null){
                        response.append(line);
                    }
                    if(connection!=null){
                        connection.disconnect();
                    }
                    String msg = "";
                    Log.i("key",response.toString());
                    msg = response.toString();
                    usermsg = new Bundle();
                    usermsg.putString("usermsg",msg);
                    postUserName = new Bundle();
                    postUserName.putString("userName",msg.split(" ")[1]);
                    if(msg.split(" ")[0].equals("correct")){
                        temp = getPicture("http://172.19.51.194:8080/android_login/file/"+msg.split(" ")[8]);
                        //temp = getPicture("http://172.19.51.194:8080/android_login/file/2016-12-18_12-31-38.jpg");
                    }
                    Message message = new Message();
                    message.what = UPDATE_CONTENT;
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

                case UPDATE_CONTENT:
                    final String []msg = message.obj.toString().split(" ");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            if(msg[0].equals("password incorrect"))
                            {
                                Toast.makeText(getActivity(),"密码不正确",Toast.LENGTH_SHORT).show();
                            }
                            else if(msg[0].toString().equals("user not exist"))
                            {
                                Toast.makeText(getActivity(),"用户不存在",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getActivity(),"登录成功",Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                                flag = 1;
                                headPath = msg[8];
                                mainUserName.setText(msg[1]);
                                mainNickname.setText(msg[7]);
                                head.setImageBitmap(temp);
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                        Log.i("111111111111111111111","222222222222222");
//                                        temp = getPicture("http://172.18.59.116:8080/android_login/file/618922343311227866.jpg");
//
//                                }
//                            });

                            }
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
            connection.setReadTimeout(80000);
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