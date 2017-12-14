package com.sty.aidl.local;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.sty.aidl.remote.service.IMyService;
import com.sty.aidl.remote.service.IPayService;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private Button btnCallRemoteMethod;
    private Button btnCallPayMethod;

    private MyConnForRemote connForRemote;
    private MyConnForPay connForPay;
    private IMyService iMyService;
    private IPayService iPayService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myBindRemoteService();
        myBindPayService();

        initViews();
        setListeners();
    }

    private void initViews(){
        btnCallRemoteMethod = findViewById(R.id.btn_call_remote_method);
        btnCallPayMethod = findViewById(R.id.btn_call_pay_method);
    }

    private void setListeners(){
        btnCallRemoteMethod.setOnClickListener(this);
        btnCallPayMethod.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        //当Activity销毁的时候取消绑定
        unbindService(connForRemote);
        unbindService(connForPay);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_call_remote_method:
                try {
                    iMyService.callTestMethod();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_call_pay_method:
                try {
                    boolean result = iPayService.callPay("abc", "123", 200);
                    if(result){
                        Toast.makeText(this, "买豆成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "买豆失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void myBindRemoteService(){
        Intent intent = new Intent();
        //设置一个action
        intent.setAction("com.sty.service.remote.service");
        connForRemote = new MyConnForRemote();
        //目的是为了获取定义的中间人对象
        bindService(intent, connForRemote, BIND_AUTO_CREATE);
    }

    private void myBindPayService(){
        Intent intent = new Intent();
        //设置一个action
        intent.setAction("com.sty.service.pay.service");
        connForPay = new MyConnForPay();
        //目的是为了获取定义的中间人对象
        bindService(intent, connForPay, BIND_AUTO_CREATE);
    }

    private class MyConnForRemote implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //获取中间人对象
            iMyService = IMyService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private class MyConnForPay implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iPayService = IPayService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
