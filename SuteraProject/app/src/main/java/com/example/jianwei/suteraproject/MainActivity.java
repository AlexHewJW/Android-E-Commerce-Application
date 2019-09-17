package com.example.jianwei.suteraproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/**
 * @author Jian Wei Hew eeyjwh@nottingham.ac.uk
 * @version 1
 * @since april 2018
 */
/**
 * This page is mainly for sign up purpose,
 * Receive email and password from user and double check with database,
 * If user exist, cannot sign up,
 * If user dont exist, register and redirect to item menu activity page
 */
public class MainActivity extends AppCompatActivity {
    private EditText email,pass;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    /**
     *Initialisation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.editEmail);        //LINK BACK TO XML FILE
        pass = (EditText) findViewById(R.id.editPass);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

    }

    /**
     *When sign up button clicked,
     * Check if both edittext boxes are filled correctly with relevant email address
     * IF ok, create new account
     * Open Login activity
     * IF account already exist, give toast notice
     */
    public void signupButtonClicked(View view){
        final String email_text = email.getText().toString().trim();
        String pass_text = pass.getText().toString().trim();

        if(!TextUtils.isEmpty(email_text) && !TextUtils.isEmpty(pass_text)){ //CHECK IF FIELDS ARE FILLED CORRECTLY
            mAuth.createUserWithEmailAndPassword(email_text,pass_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                public void onComplete(@NonNull Task<AuthResult> task) {  //CREATE NEW AUTHORISATION
                    if(task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();       //NEW ACTIVITY AFTER SIGN UP
                        DatabaseReference current_user = mDatabase.child(user_id);
                        current_user.child("Name").setValue(email_text);
                        Intent login = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(login);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Account Exists or Error Input",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    /**
     *Open User login activity page
     */
    public void signinButtonClicked(View view){
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);     //GO TO USER LOGIN PAGE
        startActivity(loginIntent);

    }


    /**
     *Open Admin Login activity Page
     */
    public void signinButtonClicked1(View view){
        Intent loginIntent1 = new Intent(MainActivity.this,AdminLogin.class);       //GO TO ADMIN LOGIN PAGE
        startActivity(loginIntent1);

    }
}
