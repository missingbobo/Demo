package com.example.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.demo.Dao.DBHelper;
import com.example.demo.Dao.Person;

import java.util.ArrayList;

public class BirthdayListActivity extends Activity {
    //从数据库里取数据
    //展示到界面
    ListView listView;
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
            name.setText(gender+"  "+person.getName());
            birth.setText(person.getYear()+"年"+person.getMonth()+"月"+person.getDay()+"日");
            return convertView;
        }
    }


}