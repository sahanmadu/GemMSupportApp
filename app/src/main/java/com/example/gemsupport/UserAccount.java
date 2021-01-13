package com.example.gemsupport;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UserAccount extends AppCompatActivity {
    TextView fname,email,pno;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        fname=findViewById(R.id.txtFname);
        email=findViewById(R.id.txtEmails);
        pno=findViewById(R.id.txtNo);
        fauth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        uid=fauth.getCurrentUser().getUid();

        DocumentReference documentReference=fstore.collection("users").document(uid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                fname.setText(value.getString("Fullname"));
                email.setText(value.getString("Email"));
                pno.setText(value.getString("PhoneNumber"));
            }
        });
    }
}