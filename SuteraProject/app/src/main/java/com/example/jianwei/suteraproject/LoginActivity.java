package com.example.jianwei.suteraproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
/**
 * @author Jian Wei Hew eeyjwh@nottingham.ac.uk
 * @version 1
 * @since april 2018
 */
/**
 * This class is for ordinary users to login into the app,
 * This involves receiving email and password from the EditText,
 * Then check with firebase for authorisation,
 * If the user exist, the user is redirected to a new activity page
 */
public class LoginActivity extends AppCompatActivity {
    private EditText userEmail,userPass;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    static int userkey;

    /**
     *Initialisation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userEmail = findViewById(R.id.userEmail);           //LINK BACK TO XML FILE
        userPass = findViewById(R.id.userPass);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users"); //USER LOGIN DETAIL DATABASE

    }

    /**
     *Receive user email and password from edittext
     * Ensure both edittext fields are filled
     * Authorisation
     * When authorisation successful, check if the user exist in the database
     */
    public void signinButtonClicked(View view){
        String email = userEmail.getText().toString().trim();      //RECEIVE EMAIL ADDRESS INPUT BY USER ON PHONE
        String pass = userPass.getText().toString().trim();         //RECEIVE PASSWORD INPUT BY USER ON PHONE

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){    //CHECK IF ANY FIELD IS EMPTY
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {      //AUTHORISATION CHECK
                    if(task.isSuccessful()){
                        checkUserExists();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Email & password don't match",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else
            Toast.makeText(getApplicationContext(),"Missing field",Toast.LENGTH_SHORT).show();

    }

    /**
     *If user exist in the database under "user", login and open menu activity
     * Clear activity history
     * Failed login will have toast notice
     */
    public void checkUserExists(){
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id)){            //IF USER EXIST, NEW ACTIVITY PAGE
                    userkey = 1;
                    AdminLogin.allow = 0;
                    Intent menuIntent = new Intent(LoginActivity.this,MenuActivity.class);
                    startActivity(menuIntent);
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),"Account doesn't exist",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
