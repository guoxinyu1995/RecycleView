package com.example.week3_04_01.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.week3_04_01.R;

public class ShowActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        //获取资源id
        textView = findViewById(R.id.j_text);
        Intent intent = getIntent();
        String bu = intent.getStringExtra("bu");
        textView.setText(bu);
    }
}
