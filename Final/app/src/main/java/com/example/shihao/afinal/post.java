package com.example.shihao.afinal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by ShiHao on 2016/12/14.
 */

public class post extends AppCompatActivity {
    Button btn_add_pic;
    Button post_item;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setPositiveButton("相册", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which){
                Toast.makeText(post.this,"从相册",Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("相机",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which){
                Toast.makeText(post.this,"从相机",Toast.LENGTH_SHORT).show();
            }
        }).create();
        btn_add_pic=(Button)findViewById(R.id.add_pic);
        post_item=(Button)findViewById(R.id.postForSure);
        btn_add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            alertDialog.show();
            }
        });
    }

}
