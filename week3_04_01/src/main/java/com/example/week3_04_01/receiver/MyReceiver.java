package com.example.week3_04_01.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.example.week3_04_01.activity.ShowActivity;

import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
      if(intent.getAction().equals("cn.jpush.android.intent.NOTIFICATION_OPENED")){
          String bu = bundle.getString(JPushInterface.EXTRA_ALERT);
          Intent intent1 = new Intent(context,ShowActivity.class);
          intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
          intent1.putExtra("bu",bu);
          context.startActivity(intent1);
        }
    }
}
