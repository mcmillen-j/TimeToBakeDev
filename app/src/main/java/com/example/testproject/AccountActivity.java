package com.example.testproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AccountActivity extends AppCompatActivity {

    private Button updateAccountInfo;
    private EditText firstName, lastName, DOB, dietaryReq;

    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        InitializeFields();

        updateAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateAccount();
            }
        });

        RetrieveUserInfo();
    }

    private void InitializeFields() {
        updateAccountInfo = (Button) findViewById(R.id.updateAccountInfoButton);
        firstName = (EditText) findViewById(R.id.setFirstName);
        lastName = (EditText) findViewById(R.id.setLastName);
        DOB = (EditText) findViewById(R.id.setDOB);
        dietaryReq = (EditText) findViewById(R.id.setDietaryReq);
    }

    private void UpdateAccount() {
        String setFirstName = firstName.getText().toString();
        String setLastName = lastName.getText().toString();
        String setDOB = DOB.getText().toString();
        String setDietaryReq = dietaryReq.getText().toString();


        if (TextUtils.isEmpty(setFirstName)) {
            Toast.makeText(this, "Please enter your first name.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setLastName)) {
            Toast.makeText(this, "Please enter your last name.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setDOB)) {
            Toast.makeText(this, "Please enter your date of birth.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setDietaryReq)) {
            Toast.makeText(this, "Please enter your dietary requirement. If none, please enter 'none'.", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, String> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserID);
            profileMap.put("firstname", setFirstName);
            profileMap.put("lastname", setLastName);
            profileMap.put("dob", setDOB);
            profileMap.put("dietaryReq", setDietaryReq);
            RootRef.child("Users").child(currentUserID).setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                SendUserToMainActivity();
                                Toast.makeText(AccountActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                String message  = task.getException().toString();
                                Toast.makeText(AccountActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void RetrieveUserInfo() {
        RootRef.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("firstname") && (dataSnapshot.hasChild("lastname") && (dataSnapshot.hasChild("dob"))))){
                            String retrieveFirstName = dataSnapshot.child("firstname").getValue().toString();
                            String retrieveLastName = dataSnapshot.child("lastname").getValue().toString();
                            String retrieveDOB = dataSnapshot.child("dob").getValue().toString();
                            String retrieveDietaryReq = dataSnapshot.child("dietaryReq").getValue().toString();

                            firstName.setText(retrieveFirstName);
                            lastName.setText(retrieveLastName);
                            DOB.setText(retrieveDOB);
                            dietaryReq.setText(retrieveDietaryReq);

                        } else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("firstname"))) {
                            String retrieveFirstName = dataSnapshot.child("firstname").getValue().toString();
                            firstName.setText(retrieveFirstName);
                        } else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("lastname"))) {
                            String retrieveLastName = dataSnapshot.child("lastname").getValue().toString();
                            lastName.setText(retrieveLastName);
                        } else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("DOB"))) {
                            String retrieveDOB = dataSnapshot.child("dob").getValue().toString();
                            DOB.setText(retrieveDOB);
                        } else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("dietaryReq"))) {
                            String retrieveDietaryReq = dataSnapshot.child("dietaryReq").getValue().toString();
                            dietaryReq.setText(retrieveDietaryReq);
                        } else {
                            Toast.makeText(AccountActivity.this, "Please set and update your profile information.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void SendUserToMainActivity() {
        Intent AccountIntent = new Intent(AccountActivity.this, MainActivity.class);
        AccountIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(AccountIntent);
        finish();
    }
}