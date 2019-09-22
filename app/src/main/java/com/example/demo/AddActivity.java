package com.example.demo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.demo.Dao.DBHelper;
import com.example.demo.Dao.Person;
//往数据库里存放一个生日

public class AddActivity extends Activity {

    private final String TAG = "ADD";
    Person person;
    TextView bithtv;
    RadioGroup group;
    ImageView avatar;
    private int REQUESTID_AVATAR = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add);
        group = findViewById(R.id.group);
        avatar = findViewById(R.id.chooseavatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(AddActivity.this, AvatarsActivity.class);
                startActivityForResult(intent, REQUESTID_AVATAR);
            }
        });
        person = new Person();
        bithtv = findViewById(R.id.birth);
        bithtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        person.setYear(y);
                        person.setMonth(m + 1);
                        person.setDay(d);
                        bithtv.setText(person.getYear() + "年" + person.getMonth() + "月" + person.getDay() + "日");
                    }
                }, person.getYear(), person.getMonth() - 1, person.getDay());
                dialog.show();
            }
        });
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameEt = findViewById(R.id.name);
                String name = nameEt.getText().toString();//拿到用户输入的姓名
                person.setName(name);
                int gender = group.getCheckedRadioButtonId() == R.id.boy ? Person.GENDER_BOY : Person.GNNDER_GIRL;//1代表男0代表女
                person.setGender(gender);
                DBHelper.getInstance(AddActivity.this).add(person);
                Toast.makeText(AddActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTID_AVATAR) {
            if(resultCode==1) {
                String avatr = data.getStringExtra("avatar");
                Glide.with(AddActivity.this).load(avatr).into(avatar);
            }else{
                Toast.makeText(this, "没有设置头像", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
