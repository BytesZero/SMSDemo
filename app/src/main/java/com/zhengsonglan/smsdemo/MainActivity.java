package com.zhengsonglan.smsdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkland.sdk.sms.SMSCaptcha;
import com.thinkland.sdk.util.BaseData;
import com.thinkland.sdk.util.CommonFun;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends ActionBarActivity {
    EditText et_phone;
    Button bt_send;
    TextView tv_code;


//    private BroadcastReceiver smsReceiver;
//    private IntentFilter filter2;
//    private Handler handler;
//    private String strContent;
//    private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";

    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CommonFun.initialize(getApplicationContext(),true);

        setContentView(R.layout.activity_main);

        et_phone= (EditText) findViewById(R.id.main_et_phone);
        bt_send= (Button) findViewById(R.id.main_bt_send);
        tv_code= (TextView) findViewById(R.id.main_tv_code);

        //发送短信验证码
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=et_phone.getText()+"";
                if (phone!=null&&!phone.equals("")) if (phone.length() == 11) {
                    String content = "test";
                    showToast(content);
                    SMSCaptcha smsCaptcha = SMSCaptcha.getInstance();
                    smsCaptcha.sendCaptcha(phone, new BaseData.ResultCallBack() {
                        @Override
                        public void onResult(int i, String s, String s2) {
                            if (i == 0)
                            {
                                showToast("验证码发送成功");

                            }else if(i == 1){
                                showToast("验证码发送错误");
                            }else if(i == -2){
                                showToast("本地网络异常");
                            }else if(i == -3){
                                showToast("服务器网络异常");
                            }else if(i == -4){
                                showToast("解析错误");
                            }else if(i == -5){
                                showToast("初始化异常");
                            }else{
                                showToast("未知异常");
                            }
                        }
                    });

                } else {
                    showToast("请输入正确的手机号码");
                }
                else{
                    showToast("请输入手机号码");
                }


            }
        });

        //生成广播处理
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();

        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        //注册广播
        this.registerReceiver(mSMSBroadcastReceiver, intentFilter);

        mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void onReceived(String message) {
                showToast("验证码为："+message);
                tv_code.setText("验证码为："+message);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    /**
     * 显示Toast
     * @param content 显示的内容
     */
    private void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mSMSBroadcastReceiver);
    }

//    /**
//     * 匹配短信中间的6个数字（验证码等）
//     *
//     * @param patternContent
//     * @return
//     */
//    private String patternCode(String patternContent) {
//        if (TextUtils.isEmpty(patternContent)) {
//            return null;
//        }
//        Pattern p = Pattern.compile(patternCoder);
//        Matcher matcher = p.matcher(patternContent);
//        if (matcher.find()) {
//            return matcher.group();
//        }
//        return null;
//    }
}
