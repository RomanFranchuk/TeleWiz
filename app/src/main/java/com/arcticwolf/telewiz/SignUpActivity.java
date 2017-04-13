package com.arcticwolf.telewiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

/**
 * Created by michael on 4/6/17.
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private final static int LAYOUT = R.layout.activity_sign_up;
    private Button buttonRegister;
    private TextView textViewSingIn;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Sign up");
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        buttonRegister = (Button) findViewById(R.id.buttonSignUp);
        textViewSingIn = (TextView) findViewById(R.id.textViewSingIn);

        buttonRegister.setOnClickListener(this);
        textViewSingIn.setOnClickListener(this);

    }

    private void register(){

        final String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_LONG).show();
        }

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.length() <6){
            Toast.makeText(this, "Your password length less than 6 ", Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirmPassword)){
            Toast.makeText(this, "Password and confirm password don't match", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Registered succesfully", Toast.LENGTH_LONG).show();
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    User userInfo = new User(name, user.getEmail());

                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("users").child(user.getUid()).setValue(userInfo);

                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);

                }else{
                    progressDialog.dismiss();

                    Toast.makeText(SignUpActivity.this, "Could not register, please try again", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
    @Override
    public void onClick(View v) {
        if(v == buttonRegister){
            register();
        }

        if (v == textViewSingIn){
            Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
            startActivity(intent);
        }
    }
}
