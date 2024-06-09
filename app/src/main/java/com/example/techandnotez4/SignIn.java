package com.example.techandnotez4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignIn extends AppCompatActivity {
    TextView username, password;
    Button loginBtn;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        username = (TextView) findViewById(R.id.userName);
        password = (TextView) findViewById(R.id.passWord);
        loginBtn = (Button) findViewById(R.id.loginButton);
        DB = new DBHelper(this);
    }

    public void loginButtonClicked(View view) {
        String USERNAME = username.getText().toString();
        String PASSWORD = password.getText().toString();
        if (USERNAME.equals("") || PASSWORD.equals("")) {
            Toast.makeText(SignIn.this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
        } else {
            Boolean checkuserpass = DB.checkusernamepassword(USERNAME, PASSWORD);
            if (checkuserpass == true) {
                Toast.makeText(SignIn.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SignIn.this, News.class);
                i.putExtra("getCurrentUsername",USERNAME);
                i.putExtra("getCurrentPassword",PASSWORD);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(SignIn.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void createNewAccount(View view) {
        Intent i = new Intent(SignIn.this, SignUp.class);
        startActivity(i);
        finish();
    }
}