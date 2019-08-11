package com.example.demo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;



public class MainActivity extends Activity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter();
        filter.addAction("asdfas");
        registerReceiver(registerReceiver, filter);
        findViewById(R.id.list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("phone", "123134243");
                startActivity(intent);
//               finish();
//                 a();
//                getToken();
            }
        });
        findViewById(R.id.put).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.saveToken("this is loged", MainActivity.this);
            }
        });
        button = findViewById(R.id.button);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    Toast.makeText(getApplicationContext(),"down",Toast.LENGTH_SHORT).show();

                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    Toast.makeText(getApplicationContext(),"up",Toast.LENGTH_SHORT).show();
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){

                }
                return false;
            }
        });

    }

    BroadcastReceiver registerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };


    private void getToken() {
        String token = Utils.getToken(MainActivity.this);
        toast(token);
    }

    private void a() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toast("time is up");
            }
        }, 2000);
    }

    private void toast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("main", "onResume: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(registerReceiver);

    }
}

