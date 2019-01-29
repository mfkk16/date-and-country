package com.example.calsys.testapi;

import android.widget.EditText;

public class Const {
    private static String URI = "http://web.tutorzone.qa/api/";
    public static String URL_Student_register = URI + "student.php";

//--------------------Number Check------------------------

    public static boolean isValidMobile(EditText editText) {
        String num = editText.getText().toString();

        boolean check = false;
        if (num.length()==10) {
            editText.setError(null);
            check = true;
        } else {
            check = false;
            editText.setError("Not Valid Number");
        }
        return check;
    }


}
