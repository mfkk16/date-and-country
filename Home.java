package com.example.calsys.testapi;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Home extends AppCompatActivity implements CountryPickerListener {
    private TextView DOB;
    public Date date_birth;
    public String date_of_birth;
    final Calendar myCalendar = Calendar.getInstance();

    private CountryPicker countryPicker;
    private ImageView countryFlagImageView;
    private TextView countryNameTextView;

    TextInputEditText user_name, email, number, reff, password;
    RadioGroup gendergp;
    int gender = 1;
    Button click;

    private RequestQueue fasilreqest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user_name = findViewById(R.id.user_name);
        email = findViewById(R.id.email);
        number = findViewById(R.id.number);
        reff = findViewById(R.id.reff);
        password = findViewById(R.id.password);
        gendergp = findViewById(R.id.gendergp);
        if (gendergp.getCheckedRadioButtonId() == R.id.male) {
            gender = 1;
        } else {
            gender = 0;
        }

        click = findViewById(R.id.click);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = user_name.getText().toString();
                String mail = email.getText().toString();
                String num = number.getText().toString();
                String refe = reff.getText().toString();

                String pass = password.getText().toString();

                String date = DOB.getText().toString();

                String nation = countryNameTextView.getText().toString();

                if (valid(uname, mail, num, refe, pass, date, nation, gender)) {
                    Login(uname, mail, num, refe, pass, date, nation, gender);

                }


            }
        });


// ------------------------------- bod -------------------------------------------------------------


        DOB = findViewById(R.id.dob);
        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    Calendar mcurrentDate = Calendar.getInstance();
                    int year = mcurrentDate.get(Calendar.YEAR);
                    int month = mcurrentDate.get(Calendar.MONTH);
                    int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    final DatePickerDialog mDatePicker = new DatePickerDialog(Home.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            myCalendar.set(Calendar.YEAR, selectedyear);
                            myCalendar.set(Calendar.MONTH, selectedmonth);
                            myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                            date_birth = (myCalendar.getTime());
                            //  inspector1 =true;
                            updateLabel();

                        }
                    }, year, month, day);
                    mDatePicker.setTitle("Please Select  Your DOB");
                    // TODO Hide Future Date Here
                    mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

                    // TODO Hide Past Date Here
                    //  mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                    mDatePicker.show();
                }
            }
        });


// ------------------------------- -----------------------------------------------------------------

        //Country Picker

        countryFlagImageView = findViewById(R.id.selected_country_flag_image_view);
        countryNameTextView = findViewById(R.id.nation);


        countryPicker = CountryPicker.newInstance("Select Country");
        countryPicker.setListener((CountryPickerListener) this);

        countryNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });

    }

    private boolean valid(String uname, String mail, String num, String refe, String pass, String date, String nation, int gender) {
        if (uname.length() <= 2) {
            Toast.makeText(Home.this, "Enter User name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mail.length() <= 5) {
            Toast.makeText(Home.this, "Enter email id", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Const.isValidMobile(number)) {
            Toast.makeText(Home.this, "Enter a valid number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (refe.length() <= 2) {
            Toast.makeText(Home.this, "Enter a valid name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pass.length() <= 5) {
            Toast.makeText(Home.this, "Enter a valid mobile number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (date.isEmpty()) {
            Toast.makeText(Home.this, "Select a date", Toast.LENGTH_SHORT).show();
            return false;
        } else if (nation.isEmpty()) {
            Toast.makeText(Home.this, "Select a Country", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Cou Picker

    @Override
    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
        countryFlagImageView.setImageResource(flagDrawableResID);
        countryNameTextView.setText(name);
        countryPicker.dismiss();
    }

// --------------------------------------------BOD--------------------------------------------------

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        String myFormatdb = "yyyy/MM/dd";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(myFormat, Locale.US);

        DOB.setText(sdf.format(myCalendar.getTime()));

    }

    //-------------------------------------------- api ---------------------------------------------

    private void Login(final String uname, final String mail, final String num, final String refe, final String pass, final String date, final String nation, final int gender) {
        fasilreqest = Volley.newRequestQueue(this);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Const.URL_Student_register, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj1 = new JSONObject(response);
                    String responseResult = obj1.getString("res");
                    if (responseResult.equals("success")) {
                        Toast.makeText(Home.this, "Sucess", Toast.LENGTH_SHORT).show();

                        Const_values.USER_NAME = user_name.getText().toString();
                        Const_values.USER_MOB = number.getText().toString();

                        Intent stud_intent = new Intent(getApplicationContext(), Student_Home.class);
                        startActivity(stud_intent);
                        finish();

                    } else {
                        Toast.makeText(Home.this, "FAIL", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Err", "Error: " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", uname);
                params.put("email", mail);
                params.put("phn", num);
                params.put("nationality", nation);
                params.put("gend", String.valueOf(gender));
                params.put("reffby", refe);
                params.put("pwd", pass);
                params.put("dob", date);
                return params;
            }
        };
        fasilreqest.add(strReq);
    }
//--------------------------------------------------------------------------------------------------


//-----------------------------------------Back Button----------------------------------------------

    private long lastPressedTime;
    private static final int PERIOD = 2000;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Press again to exit.",
                                Toast.LENGTH_SHORT).show();
                        lastPressedTime = event.getEventTime();
                    }
                    return true;
            }
        }
        return false;
    }

}
