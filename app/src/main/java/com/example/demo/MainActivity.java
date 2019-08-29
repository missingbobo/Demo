package com.example.demo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity {
    String[] items = new String[]{"List", "Dialog"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(new mAdapter());

    }

    class mAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return items[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            View view;
            if (convertView == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.main_item, null);
                holder.tv = view.findViewById(R.id.show);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setOnClickListener(new mClick(items[i]));
            holder.tv.setText(items[i]);
            return view;
        }

        class ViewHolder {
            TextView tv;
        }

    }

    class mClick implements View.OnClickListener {
        String item;

        public mClick(String id) {
            this.item = id;
        }

        @Override
        public void onClick(View view) {
            if (getActivityByName(item) != null) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, getActivityByName(item));
                startActivity(intent);
            }
        }
    }

    private Class getActivityByName(String name) {
        if (name.equals("List")) {
            return ListActivity.class;
        }
        if (name.equals("Dialog")) {
            return DialogActivity.class;
        }

        return null;

    }


}

