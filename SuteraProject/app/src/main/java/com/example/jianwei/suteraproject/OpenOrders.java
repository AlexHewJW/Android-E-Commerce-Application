package com.example.jianwei.suteraproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/**
 * @author Jian Wei Hew eeyjwh@nottingham.ac.uk
 * @version 1
 * @since april 2018
 */
/**
 * Retrieve order form data from firebase,
 * Display order form in recyclerview
 */
public class OpenOrders extends AppCompatActivity {

    private RecyclerView mKainList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    /**
     *Initialisation
     * If the admin is without authorisation, go back to sign up page
     * Find data from database under the name "Purchased"
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_orders);

        mKainList = (RecyclerView) findViewById(R.id.orderLayout);      //LINK BACK TO XML FILE
        mKainList.setHasFixedSize(true);
        mKainList.setLayoutManager(new LinearLayoutManager(this)); //basic recycler view procedures
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Purchased");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(OpenOrders.this,MainActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);     //FLAG AND NEW ACTIVITY
                    startActivity(loginIntent);
                }
            }
        };

    }
    /**
     *Show all of the customer order from in the recyclerview
     * If any of the order form is pressed, open single order form activity page
     */
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter <Order,OrderViewHolder> FRBA = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(
                Order.class,
                R.layout.singleorderlayout,
                OrderViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Order model, int position) {
                viewHolder.setUserName(model.getUsername());
                viewHolder.setItemName(model.getItemname());
                viewHolder.setAddress(model.getAddress());


                final String kain_key = getRef(position).getKey().toString();
                viewHolder.orderView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent deleteActivity = new Intent(OpenOrders.this,DeleteActivity.class);
                        deleteActivity.putExtra("KainId",kain_key);
                        startActivity(deleteActivity);
                        finish();

                    }
                });
            }

        };
        mKainList.setAdapter(FRBA);

    }
    /**
     *Recycler view for customers order forms.
     */
    public static class OrderViewHolder extends RecyclerView.ViewHolder{
        View orderView;


        public OrderViewHolder(View itemView) { //constructor
            super(itemView);
            orderView = itemView;
        }

        public void setUserName(String username){
            TextView username_content = (TextView) orderView.findViewById(R.id.orderUserName);
            username_content.setText(username);
        }

        public void setItemName(String itemname){
            TextView itemname_content = (TextView) orderView.findViewById(R.id.orderItemName);
            itemname_content.setText(itemname);
        }

        public void setAddress(String address){
            TextView address_content = (TextView) orderView.findViewById(R.id.orderAddress);
            address_content.setText(address);
        }
    }
}

