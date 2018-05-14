package com.lansent.cannan.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lansent.cannan.util.Log;


/**
 * Created by Cannan on 2017/7/26 0026.
 */

public abstract class BaseBroadCast extends BroadcastReceiver {

    public String TAG;

    @Override
    public final void onReceive(Context context, Intent intent) {
            TAG=getClass().getSimpleName();
            long time=System.currentTimeMillis();
           if(intent==null){
               Log.i(TAG,"intent is null");
               return;
           }
           received(intent);
           Log.i(TAG,"intent action = " + intent.getAction()+ " take time "+(System.currentTimeMillis()-time)+" ms");
    }

    protected abstract void received(Intent intent);
}
