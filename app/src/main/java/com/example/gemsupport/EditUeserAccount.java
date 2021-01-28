package com.example.gemsupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class EditUeserAccount extends AppCompatActivity {
    EditText fullname,emails,pno1;
    ImageView myProfile;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    FirebaseUser user;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ueser_account);

        Intent data=getIntent();
        String fname=data.getStringExtra("Fullname");
        String email=data.getStringExtra("Email");
        String pno=data.getStringExtra("PhoneNumber");

        fauth=FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();
        user=fauth.getCurrentUser();

        fullname=findViewById(R.id.txtName);
        emails=findViewById(R.id.txtEmail);
        pno1=findViewById(R.id.txtPno);
        myProfile=findViewById(R.id.imageView3);
        save=findViewById(R.id.btnSave);

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditUeserAccount.this, "clicked the image", Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fullname.getText().toString().isEmpty() || emails.getText().toString().isEmpty() || pno1.getText().toString().isEmpty()){
                    Toast.makeText(EditUeserAccount.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String email=emails.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference dref=fstore.collection("users").document(user.getUid());
                        Map<String,Object> updated= new HashMap<>();
                        updated.put("Fullname",fullname.getText().toString());
                        updated.put("Email",email);
                        updated.put("PhoneNumber",pno1.getText().toString());
                        dref.update(updated).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditUeserAccount.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),UserAccount.class));
                                finish();
                            }
                        });
                        Toast.makeText(EditUeserAccount.this, "Email is changed", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditUeserAccount.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        fullname.setText(fname);
        emails.setText(email);
        pno1.setText(pno);

        Log.d("TAG", "onCreate: "+ fname+" "+email+" "+pno);
    }


}