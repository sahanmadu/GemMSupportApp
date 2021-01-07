package com.example.gemsupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText fname,email,password,phone;
    Button btnsignup;
    TextView mytext;
    FirebaseAuth fbauth;
    ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fname = findViewById(R.id.editTextTextPersonName);
        email = findViewById(R.id.editTextTextPersonEmail);
        password = findViewById(R.id.editTextTextPersonPassword);
        phone = findViewById(R.id.editTextTextPersonPhoneno);
        btnsignup = findViewById(R.id.button);
        mytext = findViewById(R.id.txtlogin);
        pbar= findViewById(R.id.progressBar3);
        fbauth = FirebaseAuth.getInstance();

        //for registered users
        if(fbauth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),HomePage.class));
            finish();
        }

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname=fname.getText().toString().trim();
                String emails=email.getText().toString().trim(); // to format data
                String passwords=password.getText().toString().trim();
                String no=phone.getText().toString().trim();
                //validate the data
                if(TextUtils.isEmpty(uname)){
                    email.setError("your username is required");
                    return;
                }
                if(TextUtils.isEmpty(emails)){
                    email.setError("your email is required");
                    return;
                }
                if(TextUtils.isEmpty(passwords)){
                    password.setError("your password is required");
                    return;
                }
                if(TextUtils.isEmpty(no)){
                    phone.setError("your phone number is required");
                    return;
                }
                if(Patterns.EMAIL_ADDRESS.matcher(emails).matches()){
                    email.setError("please enter valid email adddress");
                }

                if(passwords.length()<8){
                    password.setError("passwrd must be at least 8 characters!");
                    return;
                }
                pbar.setVisibility(View.VISIBLE);

                fbauth.createUserWithEmailAndPassword(emails,passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "Successfully added!", Toast.LENGTH_SHORT).show();
                            //send user to the main activity ----home
                            startActivity(new Intent(getApplicationContext(),HomePage.class));
                        }
                        else{
                            Toast.makeText(Register.this, "some eror occured! please check again"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


         mytext.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(getApplicationContext(),Login.class));
             }
         });
    }
}