package com.example.jianwei.suteraproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
 * This class is solely for admin to login,
 * This involves receiving email and password from edit text,
 * Check with firebase for authorisation under "admin",
 * If the user exist, they are allow to proceed to the item menu activity page
 */
public class AdminLogin extends AppCompatActivity {
    private EditText adminE, adminP;            //INITIALISATION
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    static int allow=0;

    /**
     * Initialisation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminE = (EditText) findViewById(R.id.adminemail);      //LINK BACK TO XML FILE
        adminP = (EditText) findViewById(R.id.adminpass);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("admins"); //DATABASE


    }
    /**
     * Receive text input
     * Ensure both password and email edittext box are filled
     * Check account authorisation with firebase
     * failed login will have Toast Notice
     */
    public void AdminLoginButtonClicked(View view){
        String email = adminE.getText().toString().trim();          //RECEIVE TEXT INSERTED BY USER IN PHONE
        String pass = adminP.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){  //make sure the both edittext are not empty
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        checkAdminExists(); //see if the admin is in our database or not
                        allow = 1;
                        LoginActivity.userkey = 0;

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Email & password don't match",Toast.LENGTH_SHORT).show();
                        allow = 0;
                    }
                }
            });
        }
        else
            Toast.makeText(getApplicationContext(),"Missing field",Toast.LENGTH_SHORT).show();


    }
    /**
     * Check if account available in database under the name "admins"
     * Successful sign in will open admin menu activity
     * Failed sign in will have Toast notice
     */
    public void checkAdminExists(){ //check using unique ID
        final String admin_id = mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {  //check if id matches
                if(dataSnapshot.hasChild(admin_id)){
                    Intent donelogin = new Intent(AdminLogin.this,AdminMenu.class);
                    startActivity(donelogin);
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),"Account don't exist",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
