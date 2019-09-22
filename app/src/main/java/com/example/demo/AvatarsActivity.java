package com.example.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.demo.Dao.Utils;

public class AvatarsActivity extends Activity {
    String[] items;
    private String selectAvatar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avatar_layout);
        items = Utils.getAvatar(AvatarsActivity.this);
        GridView gridview = findViewById(R.id.gridview);
        gridview.setAdapter(new mAdapter());
        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("avatar",selectAvatar);
                setResult(1,intent);
                finish();
            }
        });
    }
    class mAdapter extends BaseAdapter{

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
            View view = LayoutInflater.from(AvatarsActivity.this).inflate(R.layout.griditem,null);
            ImageView avatar = view.findViewById(R.id.img);
            ImageView mask = view.findViewById(R.id.mask);
            final String url =  items[i];
            boolean isShow = url.equals(selectAvatar);
            mask.setVisibility(isShow?View.VISIBLE:View.GONE);
            Glide.with(AvatarsActivity.this).load(url).into(avatar);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectAvatar = url;
                    notifyDataSetChanged();
                }
            });
            return view;
        }
    }
}
