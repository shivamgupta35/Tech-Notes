package com.example.techandnotez4;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {
    EditText name, dob, username, pwd;
    CheckBox termsAndConditions;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = findViewById(R.id.name);
        dob = findViewById(R.id.birthDay);
        username = findViewById(R.id.userName);
        pwd = findViewById(R.id.passWord);
        termsAndConditions = findViewById(R.id.tersAndConditions);
        DB = new DBHelper(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void selectBirthday(View view) {
        DatePickerDialog dp = new DatePickerDialog(this, 0);
        dp.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int m = month + 1;
                dob.setText(dayOfMonth + "/" + m + "/" + year);
            }
        });
        dp.show();
    }

    public void signInIntoExisting(View view) {
        Intent i = new Intent(SignUp.this, SignIn.class);
        startActivity(i);
        finish();
    }


    public void signUpButtonClicked(View view) {
        String NAME = name.getText().toString();
        String DOB = dob.getText().toString();
        String USERNAME = username.getText().toString();
        String PASSWORD = pwd.getText().toString();
        if (NAME.equals("") || DOB.equals("") || USERNAME.equals("") || PASSWORD.equals("")) {
            Toast.makeText(SignUp.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
        } else {
            if (termsAndConditions.isChecked()) {
                Boolean checkuser = DB.checkusername(USERNAME);
                if (checkuser == false) {
                    Boolean insert = DB.insertData(NAME, DOB, USERNAME, PASSWORD);
                    if (insert == true) {
                        Toast.makeText(SignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SignUp.this, News.class);
                        i.putExtra("getCurrentUsername",USERNAME);
                        i.putExtra("getCurrentPassword",PASSWORD);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Toast.makeText(SignUp.this, "Username already exists", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SignUp.this, "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
