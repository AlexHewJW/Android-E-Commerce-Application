package com.example.jianwei.suteraproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
/**
 * @author Jian Wei Hew eeyjwh@nottingham.ac.uk
 * @version 1
 * @since april 2018
 */
/**
 * After paypal is successful,the owner will be asked to input delivery address,
 * Once the button is pressed, the order will be uploaded,
 * Uploaded data include email address, item name and delivery address
 */
public class TestingCheckout extends AppCompatActivity {

    private DatabaseReference aRef;         //INITIALISATION
    private DatabaseReference userData;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private EditText userAdd;
    private int j = 0;
    ArrayList<String> upload;

    /**
     *Initialisation,
     * New order will be uploaded to firebase under the name "Purchased"
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_checkout);
        userAdd = findViewById(R.id.addressInput);          //LINK BACK TO XML FILE
        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();
        userData = FirebaseDatabase.getInstance().getReference().child("users").child(current_user.getUid());   //USERID
        aRef = FirebaseDatabase.getInstance().getReference().child("Purchased");
        upload = new ArrayList<>();
        Cart cart = MenuActivity.lart;
        Set<Product> products = cart.getProducts();
        Iterator iterator = products.iterator();
        while (iterator.hasNext()){
            Product product = (Product) iterator.next();
            upload.add(product.getName());
        }

    }

    /**
     *Ask user to enter delivery address,
     * if the field is not empty means the address has been entered.
     * Upload the itemname, user email address and delivery address into the firebase under the name "Purchased"
     * If the field is empty, toast will appear to ask the user to input delivery address.
     */
    public void testingonly(View view){
        final String uploadhouse = userAdd.getText().toString().trim();
        if(!TextUtils.isEmpty(uploadhouse)) {
            j = 0;
            for (int i = 0; i < upload.size(); i++) {
                final DatabaseReference lastOrder = aRef.push();
                userData.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        lastOrder.child("itemname").setValue(upload.get(j));    //UPLOAD NEW ORDER FROM TO DATABASE
                        j++;
                        lastOrder.child("address").setValue(uploadhouse);
                        lastOrder.child("username").setValue(dataSnapshot.child("Name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(TestingCheckout.this, MenuActivity.class));
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        }
        else
            Toast.makeText(getApplicationContext(),"Input delivery address",Toast.LENGTH_SHORT).show();
    }

}
