package com.example.learningserviceremote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.wrclibrary.RemoteService_Impl;
import com.example.wrclibrary.RemoteService_aidl;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "wrc_app_1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate!");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent_remote = new Intent(this, RemoteService_Impl.class);
        bindService(intent_remote, mConnection_remote, Context.BIND_AUTO_CREATE);// 绑定远程服务
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop!");
        unbindService(mConnection_remote);
    }

    /** 远程 library 的服务 */
    private RemoteService_aidl m_RemoteService;
    private ServiceConnection mConnection_remote = new ServiceConnection() {
        // Called when the connection with the service is established
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "library onServiceConnected");
            // Following the example above for an AIDL interface,
            // this gets an instance of the IRemoteInterface, which we can use to call on the service
            m_RemoteService = RemoteService_aidl.Stub.asInterface(service);
        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "library Service has unexpectedly disconnected");
            m_RemoteService = null;
        }
    };

    /** 按钮的实现 */
    public void onButtonClick_Remote(View v) throws RemoteException {
        int num =  m_RemoteService.getRandomNumber_remote();
        Toast.makeText(this, "pid = " + num, Toast.LENGTH_SHORT).show();
    }

}