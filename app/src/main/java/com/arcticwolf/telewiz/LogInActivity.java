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

import com.arcticwolf.telewiz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int LAYOUT = R.layout.activity_log_in;
    private Button buttonLogin;
    private TextView textViewSingUP;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.log_in);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogIn);
        textViewSingUP = (TextView) findViewById(R.id.textViewSingUp);


        buttonLogin.setOnClickListener(this);
        textViewSingUP.setOnClickListener(this);

    }

    private void loginIn(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Login User...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            progressDialog.dismiss();

                            Toast.makeText(LogInActivity.this, "Could not login, please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
    @Override
    public void onClick(View v) {
        if(v == buttonLogin){
            loginIn();
        }

        if (v == textViewSingUP){
            startActivity(new Intent(this, SignUpActivity.class));

        }
    }
}
