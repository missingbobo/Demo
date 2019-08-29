package com.example.demo.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.demo.Dao.MyWindowManager;

public class FlowService extends Service {
    public FlowService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyWindowManager.createWindow(getApplicationContext());
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
