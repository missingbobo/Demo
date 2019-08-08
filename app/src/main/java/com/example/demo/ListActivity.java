package com.example.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        Toast.makeText(this,phone,Toast.LENGTH_SHORT).show();
        listView = findViewById(R.id.list);
        test();
    }
    private void test(){
        ArrayList<String> items = new ArrayList<>();
        for(int i = 0;i<100;i++){
            items.add("item:"+i);
        }
        mAdapter adapter = new mAdapter();
        listView.setAdapter(adapter);
        adapter.setData(items);
    }



    class mAdapter extends BaseAdapter{
        ArrayList<String> names = new ArrayList<>();
        public void setData(ArrayList<String> items){
            this.names = items;
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return names.size();
        }

        @Override
        public Object getItem(int position) {
            return names.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            View view;
            if(convertView == null){
                holder = new Holder();
                view = LayoutInflater.from(ListActivity.this).inflate(R.layout.list_item,null);
                holder.tv = view.findViewById(R.id.name);
                view.setTag(holder);
            }else{
                view = convertView;
                holder = (Holder) convertView.getTag();
            }
            holder.tv.setText((String)getItem(position));
            return view;
        }
        class Holder{
            TextView tv;
        }
    }
}
