package com.arcticwolf.telewiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int LAYOUT = R.layout.activity_profile;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private TextView textViewEmail;
    private EditText editTextFullName;
    private Button buttonSave;
    private Button buttonLogOut;
    private FirebaseUser user;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.profile);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        editTextFullName = (EditText) findViewById(R.id.editTextFullName);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonLogOut = (Button) findViewById(R.id.buttonLogOut);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();

        loadUserInfo();

        buttonSave.setOnClickListener(this);
        buttonLogOut.setOnClickListener(this);

    }

    private void loadUserInfo(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User s = dataSnapshot.child("users").child(user.getUid()).getValue(User.class);

                editTextFullName.setText(s.getName());
                textViewEmail.setText(s.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveUserInformation(){
        String fullName = editTextFullName.getText().toString().trim();
        String emailUser = firebaseAuth.getCurrentUser().getEmail();

        User userInfo = new User(fullName, emailUser);

        databaseReference.child("users").child(user.getUid()).setValue(userInfo);

        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);


    }

    @Override
    public void onClick(View v) {

        if (v == buttonSave){
            saveUserInformation();
        }

        if (v == buttonLogOut){
            if (firebaseAuth.getCurrentUser() != null){
                firebaseAuth.signOut();
                Intent intent = new Intent(ProfileActivity.this, SplashScreen.class);
                startActivity(intent);
            }
        }
    }
}
