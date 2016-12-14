package com.example.shihao.afinal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by ShiHao on 2016/12/14.
 */

public class good_detail extends AppCompatActivity {
    ImageButton btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item_detail);
        btn = (ImageButton) findViewById(R.id.back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        /*Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int pic = bundle.getInt("pic");
        String user_name = bundle.getString("user_name","default");
        String good_name = bundle.getString("good_name","default");
        String price = bundle.getString("price","default");

        TextView u_name = (TextView)findViewById(R.id.user_name);
        TextView g_name = (TextView)findViewById(R.id.good_name);
        TextView text_price = (TextView)findViewById(R.id.price);
        ImageView imageView = (ImageView)findViewById(R.id.background);

        imageView.setImageResource(pic);
        u_name.setText(user_name);
        g_name.setText(good_name);
        text_price.setText(price);*/
    }
}
