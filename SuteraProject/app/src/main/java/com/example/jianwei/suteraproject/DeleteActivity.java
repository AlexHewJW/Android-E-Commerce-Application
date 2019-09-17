package com.example.jianwei.suteraproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
/**
 * @author Jian Wei Hew eeyjwh@nottingham.ac.uk
 * @version 1
 * @since april 2018
 */
/**
 * THis class is for admin to delete order form from the database
 */
public class DeleteActivity extends AppCompatActivity {

    private String kain_key = null;
    private DatabaseReference mDatabase,userData,userData2;
    private TextView deletea, deleteb;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private String dela,delb;

    /**
     *Initialisation
     * Retrieve customer email and ordered item name from firebase and print in textview
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        kain_key = getIntent().getExtras().getString("KainId");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Purchased"); //find database data under Purchased


        deletea = findViewById(R.id.deleteuser);    //LINK TO TEXTVIEW IN XML FILE
        deleteb = findViewById(R.id.deletename);
        mDatabase.child(kain_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dela = (String) dataSnapshot.child("username").getValue(); //RETRIEVE DATA FROM DATABASE
                delb = (String) dataSnapshot.child("itemname").getValue();

                deletea.setText(dela);  //PRINT THE BUYER EMAIL ON PHONE SCREEN
                deleteb.setText(delb);  //PRINT THE BUYER'S ITEM NAME ON PHONE SCREEN
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    /**
     *Delete the particular item from database
     *Return back to open order activity
     * delete activity history
     */
    public void deleteButton(View view){    //WHEN DELETE BUTTON IS PRESSED, NEW ACTIVITY PAGE WILL OPEN
        mDatabase.child(kain_key).removeValue();
        Intent finishdelete = new Intent(DeleteActivity.this,OpenOrders.class);
        startActivity(finishdelete);
        finish(); //delete activity history

    }
}
