package com.example.calsys.testapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Student_Home extends AppCompatActivity {

    TextView stud_suer_name,stud_suer_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__home);

        stud_suer_name=findViewById(R.id.stud_suer_name);
            stud_suer_name.setText(Const_values.USER_NAME);
        stud_suer_mobile=findViewById(R.id.stud_suer_mobile);
          stud_suer_mobile.setText(Const_values.USER_MOB);



    }
}
