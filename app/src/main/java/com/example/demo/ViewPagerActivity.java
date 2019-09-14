package com.example.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ViewPagerActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_layout);
        ViewPager pager = findViewById(R.id.viewpager);
        pager.setAdapter(new mAdapter(getItems()));
    }

    private ArrayList<View> getItems() {
        ArrayList<View> items = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            View view = LayoutInflater.from(ViewPagerActivity.this).inflate(R.layout.viewpager_item_layout, null);
            TextView show = view.findViewById(R.id.show);
            show.setText("pager:" + i);
            items.add(view);
        }
        return items;
    }

    class mAdapter extends PagerAdapter {
        ArrayList<View> items = new ArrayList<View>();

        public mAdapter(ArrayList<View> a) {
            this.items = a;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(items.get(position));
            return items.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(items.get(position));
        }
    }
}
