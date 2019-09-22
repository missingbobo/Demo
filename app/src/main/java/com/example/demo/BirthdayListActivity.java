package com.example.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.demo.Dao.DBHelper;
import com.example.demo.Dao.Person;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class BirthdayListActivity extends Activity {
    //从数据库里取数据
    //展示到界面
    ListView listView;
    private final String TAG = "BirthList";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_birthdaylist);
        findViewById(R.id.addbith).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(BirthdayListActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });
        listView = findViewById(R.id.list);
        getWeatherInfo();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Person> items = DBHelper.getInstance(BirthdayListActivity.this).getItems();
        mAdapter adapter = new mAdapter(items);
        listView.setAdapter(adapter);
    }

    class mAdapter extends BaseAdapter{
        ArrayList<Person> items = new ArrayList<>();
        public mAdapter(ArrayList<Person> data){
            this.items = data;
        }
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int index, View view, ViewGroup viewGroup) {
            View convertView = getLayoutInflater().inflate(R.layout.birth_item_layout,null);
            TextView name = convertView.findViewById(R.id.name);
            TextView birth = convertView.findViewById(R.id.birth);
            Person person = items.get(index);
            String gender = person.getGender()==Person.GENDER_BOY?"♂":"♀";
            String avatar = "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=20577780,3476556477&fm=26&gp=0.jpg";
            ImageView avatarImg = convertView.findViewById(R.id.avatar);
            Glide.with(BirthdayListActivity.this).load(avatar).into(avatarImg);
            name.setText(gender+"  "+person.getName());
            birth.setText(person.getYear()+"年"+person.getMonth()+"月"+person.getDay()+"日");
            return convertView;
        }
    }
    private void getWeatherInfo(){
        OkGo.<String>get("http://wthrcdn.etouch.cn/weather_mini?city=北京").tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.e(TAG, "onSuccess: "+response.body());
                try {
                    JSONObject obj = new JSONObject(response.body());
                    JSONObject data = obj.optJSONObject("data");
                    JSONArray forecast = data.optJSONArray("forecast");
                    JSONObject info = forecast.optJSONObject(0);
                    TextView weather = findViewById(R.id.weather);
                    weather.setText(data.optString("city")+" "+info.optString("type"));
                }catch (Exception e){

                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.e(TAG, "onError: "+response.getException() );
            }

        });
    }


}
