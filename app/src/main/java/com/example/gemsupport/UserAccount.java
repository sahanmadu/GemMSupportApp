package com.example.gemsupport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UserAccount extends AppCompatActivity {
    TextView fname,email,pno,vemail;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String uid;
    Button emailverify;
    Button resetPass;
    ImageView profilephoto;
    FirebaseUser user2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        fname=findViewById(R.id.txtFname);
        email=findViewById(R.id.txtEmails);
        pno=findViewById(R.id.txtNo);
        emailverify=findViewById(R.id.btnresend);
        vemail=findViewById(R.id.txtemailverify);
        profilephoto=findViewById(R.id.profileimage);

        resetPass=findViewById(R.id.btnResetPassword);

        fauth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        uid=fauth.getCurrentUser().getUid();


        //email verification show hidden message
        user2=fauth.getCurrentUser();
        if(!user2.isEmailVerified()){
            vemail.setVisibility(View.VISIBLE);
            emailverify.setVisibility(View.VISIBLE);

            emailverify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    FirebaseUser user2=fauth.getCurrentUser();
                    user2.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(view.getContext(), "Verification email is sent!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            });
        }

        DocumentReference documentReference=fstore.collection("users").document(uid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                fname.setText(value.getString("Fullname"));
                email.setText(value.getString("Email"));
                pno.setText(value.getString("PhoneNumber"));
            }
        });

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetPass= new EditText(view.getContext()); //current view
                AlertDialog.Builder pRdialog=new AlertDialog.Builder(view.getContext());
                pRdialog.setTitle("Reset your password");
                pRdialog.setMessage("Eneter your new password here");
                pRdialog.setView(resetPass);

                pRdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String yourNewPass=resetPass.getText().toString();
                        user2.updatePassword(yourNewPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UserAccount.this, "Password reset successfully!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UserAccount.this, "Reset is failed!", Toast.LENGTH_SHORT).show();
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