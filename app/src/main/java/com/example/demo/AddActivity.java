package com.example.demo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.demo.Dao.DBHelper;
import com.example.demo.Dao.Person;
//往数据库里存放一个生日

public class AddActivity extends Activity {

    private final String TAG = "ADD";
    Person person;
    TextView bithtv;
    RadioGroup group;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add);

        group = findViewById(R.id.group);
        person = new Person();
        bithtv = findViewById(R.id.birth);
        bithtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                       person.setYear(y);
                       person.setMonth(m+1);
                       person.setDay(d);
                       bithtv.setText(person.getYear()+"年"+person.getMonth()+"月"+person.getDay()+"日");
                    }
                },person.getYear(),person.getMonth()-1,person.getDay());
                dialog.show();
            }
        });
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameEt = findViewById(R.id.name);
                String name  = nameEt.getText().toString();//拿到用户输入的姓名
                person.setName(name);
                int gender = group.getCheckedRadioButtonId()==R.id.boy? Person.GENDER_BOY :Person.GNNDER_GIRL;//1代表男0代表女
                person.setGender(gender);
                DBHelper.getInstance(AddActivity.this).add(person);
                Toast.makeText(AddActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
