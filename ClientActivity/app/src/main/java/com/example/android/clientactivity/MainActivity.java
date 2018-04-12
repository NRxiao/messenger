package com.example.android.clientactivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //隐式启动service，设置包名
        Intent intent = new Intent();
        intent.setAction("com.example.android.serviceactivity.GetServiceActivity");
        intent.setPackage("com.example.android.serviceactivity");
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMessenger != null){
                    mMessage.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", "呼叫01");
                    mMessage.setData(bundle);
                    try {
                        mMessenger.send(mMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private Message mMessage;
    private Messenger mMessenger;
    private Messenger getMessenger = new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                System.out.println(msg.getData().getString("msg"));
            }
            super.handleMessage(msg);
        }
    });
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessenger = new Messenger(service);
            mMessage = Message.obtain(null, 0);
            mMessage.replyTo = getMessenger;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }
}
