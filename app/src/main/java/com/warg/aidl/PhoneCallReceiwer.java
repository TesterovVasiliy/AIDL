package com.warg.aidl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.com.android.internal.telephony.ITelephony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PhoneCallReceiwer extends BroadcastReceiver {

    Context context;
    TelephonyManager tmgr;
    ITelephony mTelephony;

    public PhoneCallReceiwer() {

    }

    public void onReceive(Context context, Intent intent) {

        try {
            // TELEPHONY MANAGER class object to register one listner
            tmgr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            //Create Listner
            MyPhoneStateListener PhoneListener = new MyPhoneStateListener();

            // Register listener for LISTEN_CALL_STATE
            tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

            this.context = context;

        } catch (Exception e) {
            Log.e("Phone Receive Error", " " + e);
        }

    }

    private class MyPhoneStateListener extends PhoneStateListener {

        public void onCallStateChanged(int state, String incomingNumber) {

            Log.d("MyPhoneListener", state + "   incoming no:" + incomingNumber);

            if (state == TelephonyManager.CALL_STATE_RINGING) {

                String msg = "New Phone Call Event. Incomming Number : " + incomingNumber;
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, msg, duration);
                toast.show();

                //killCall();
                MyIntentService.startActionBaz(context,"param 1","param 2");
            }
        }

        private void killCall() {
            try {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                Class c = Class.forName(tmgr.getClass().getName());
                Method m = c.getDeclaredMethod("getITelephony");
                m.setAccessible(true);
                mTelephony = ((ITelephony) m.invoke(tm));
                mTelephony.endCall();
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
