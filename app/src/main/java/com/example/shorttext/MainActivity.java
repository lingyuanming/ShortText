package com.example.shorttext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.content.BroadcastReceiver;
import android.widget.Toast;
import android.view.*;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) this.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send(view);
            }
        });
    }
    public void send(View view) {
        EditText num = (EditText) this.findViewById(R.id.editText1);
        EditText msg = (EditText) this.findViewById(R.id.editText2);
        String str_num = num.getText().toString();// 得到电话号码
        String str_msg = msg.getText().toString();// 得到短信内容
        String texts = "短信发送器，短信内容：" + str_msg;
        //以下为发送短信代码
        String SENT_SMS_ACTION="SENT_SMS_ACTION";
        String DELIVERED_SMS_ACTION="DELIVERED_SMS_ACTION";
        //获取SmsManager的默认实例
        SmsManager sms = SmsManager.getDefault();
        Intent sentIntent = new Intent("SENT_SMS_ACTION");
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,sentIntent,  0);
        Intent deliverIntent = new Intent("DELIVERED_SMS_ACTION");
        PendingIntent deliverPI = PendingIntent.getBroadcast(this, 0,deliverIntent, 0);
        registerReceiver(new BroadcastReceiver()
        {
            public void onReceive(Context _context,Intent _intent)
            {
                switch(getResultCode()){
                    case AppCompatActivity.RESULT_OK:
                        Toast.makeText(getBaseContext(),"短信发送成功",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(),"SMS generic failure actions",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(),"SMS radio off failure actions",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(),"SMS null PDU failure actions",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        },new IntentFilter(SENT_SMS_ACTION));
        registerReceiver(new BroadcastReceiver()
        {
            public void onReceive(Context _context, Intent _intent) {
                Toast.makeText(getBaseContext(), "SMS delivered actions",
                        Toast.LENGTH_SHORT).show();
            }
        }, new IntentFilter(DELIVERED_SMS_ACTION));
        //如果短信内容超过70个字符 将这条短信拆成多条短信发送出去
        if (texts.length() > 70) {
            ArrayList<String> msgs = sms.divideMessage(texts);
            for (String ms : msgs) {
                sms.sendTextMessage(str_num, null, ms, sentPI, deliverPI);
            }
        } else {
            sms.sendTextMessage(str_num, null, texts, sentPI, deliverPI);
        }
        Toast.makeText(getBaseContext(), "短信发送完成", Toast.LENGTH_LONG).show();
//        这是一句废话
    }

}
