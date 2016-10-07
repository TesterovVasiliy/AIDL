package com.warg.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    MyService myService;
    boolean isBound = false;
    AddServiceInterface addServiceInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();
        intent.setClassName("com.warg.aidl", MyService.class.getName());
        isBound = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            addServiceInterface =  AddServiceInterface.Stub.asInterface(service);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myService = null;
            isBound = false;
        }
    };

    public void Click(View view) {
        int res = 0;
        Log.d("IS BOUD", String.valueOf(isBound));
        if(isBound){
            try {
                res = addServiceInterface.add(3,6);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        Log.d("SERVICE",String.valueOf(res));
    }
}
