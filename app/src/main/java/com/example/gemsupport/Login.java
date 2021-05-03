package com.example.gemsupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText email,password;
    Button btnlogin;
    TextView mytext2,fpass;
    FirebaseAuth fbauth;
    ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        email = findViewById(R.id.editTextTextPersonEmail);
        password = findViewById(R.id.editTextTextPersonPassword);
        btnlogin = findViewById(R.id.button);
        pbar= findViewById(R.id.progressBar);
        mytext2 = findViewById(R.id.txtlogin);
        fpass=findViewById(R.id.txtfpass);
        fbauth = FirebaseAuth.getInstance();


        //for login button
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emails=email.getText().toString().trim();
                String passwords=password.getText().toString().trim();

                //empty field validation - email

                if(TextUtils.isEmpty(emails)){
                    email.setError("your email is required");
                    return;
                }
                //email validation with patterns
                if(Patterns.EMAIL_ADDRESS.matcher(emails).matches()){
                    email.setError("please enter valid email adddress");
                }
                //empty field validation - password
                if(TextUtils.isEmpty(passwords)){
                    password.setError("your password is required");
                    return;
                }
                //password length
                if(passwords.length()<8){
                    password.setError("passwrd must be at least 8 characters!");
                    return;
                }

               //firebase auth
                fbauth.signInWithEmailAndPassword(emails,passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            pbar.setVisibility(View.VISIBLE);
                            Toast.makeText(Login.this, "Login successfully!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),HomePage.class));
                        }
                        else{
                            Toast.makeText(Login.this, "some eror occured! please check again"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
        //link to signup page
        mytext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        //forget password
        fpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetYourPass= new EditText(view.getContext()); //current view
                AlertDialog.Builder pRdialog=new AlertDialog.Builder(view.getContext());
                pRdialog.setTitle("Reset your password");
                pRdialog.setMessage("Eneter your email here!");
                pRdialog.setView(resetYourPass);

                pRdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail=resetYourPass.getText().toString();
                        fbauth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset link send to your email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Reset link sending failed!!"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                pRdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                pRdialog.create().show();

            }
        });
    }
}
