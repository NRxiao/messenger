package com.example.android.serviceactivity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by sz132 on 2018/4/12.
 */

public class GetServiceActivity extends Service {

    private Messenger mMessenger = new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                System.out.println(msg.getData().getString("msg"));
                Messenger messenger = msg.replyTo;
                Message message = Message.obtain(null, 1);
                Bundle bundle = new Bundle();
                bundle.putString("msg", "01收到");
                message.setData(bundle);
                try {
                    messenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            super.handleMessage(msg);
        }
    });

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
