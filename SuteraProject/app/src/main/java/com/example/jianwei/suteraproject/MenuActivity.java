package com.example.jianwei.suteraproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
/**
 * @author Jian Wei Hew eeyjwh@nottingham.ac.uk
 * @version 1
 * @since april 2018
 */
/**
 * Item menu. Display all menu items in recycle view,
 * Has a custom made activity bar for help, logout and shopping cart purpose
 */
public class MenuActivity extends AppCompatActivity {


    private RecyclerView mKainList;         //INITIALISATION
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    static Cart lart;
    static int i = 0;

    /**
     *Initialisation
     * In case the user is without authorisation, go back to sign up activity page.
     * Find data from firebase under the name "Items"
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        mKainList = findViewById(R.id.kainList);            //LINK TO XML FILE
        mKainList.setHasFixedSize(true);
        mKainList.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Item");
        lart = new Cart();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(MenuActivity.this,MainActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);  //FLAG
                    startActivity(loginIntent);
                }
            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     *Three menu options provided at the action bar, help, logout and open shopping cart.
     * Each button pressed will open different activity page
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.help:             //3 BUTTON ON THE ACTION BAR
                Intent help = new Intent(this,HelpActivity.class); //EACH BUTTON GO TO NEW ACTIVITY PAGE
                startActivity(help);
                break;
            case R.id.update:
                Intent important = new Intent(this,ViewCart.class);
                startActivity(important);
                break;
            case R.id.settings:
                Intent logout = new Intent(this, MainActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);
                AdminLogin.allow = 0;
                LoginActivity.userkey = 0;
                finish();
                break;
            default:
                //unknown error

        }


        return super.onOptionsItemSelected(item);
    }

    /**
     *Print out each item details such as name, description, price and image into the recycle view
     * If any of the item clicked, open single item page activity
     */
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<Kain,KainViewHolder> FBRA = new FirebaseRecyclerAdapter<Kain, KainViewHolder>(
                Kain.class,
                R.layout.singlemenuitem,
                KainViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(KainViewHolder viewHolder, Kain model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getImage());

                final String kain_key = getRef(position).getKey().toString();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singleKainActivity = new Intent(MenuActivity.this,SingleKainActivity.class);
                        singleKainActivity.putExtra("KainId",kain_key);
                        startActivity(singleKainActivity);
                        //finish();

                    }
                });
            }

        };
        mKainList.setAdapter(FBRA);
    }

    /**
     *Recycle view for items menu
     */
    public static class KainViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public KainViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setName(String name){
            TextView kain_name = (TextView) mView.findViewById(R.id.kainName);
            kain_name.setText(name);
        }
        public void setDesc(String desc){
            TextView kain_desc = (TextView) mView.findViewById(R.id.kainDesc);
            kain_desc.setText(desc);
        }
        public void setPrice(String price){
            TextView kain_price = (TextView) mView.findViewById(R.id.kainPrice);
            kain_price.setText(price+"£");
        }
        public void setImage(Context ctx, String image){
            ImageView kain_image =(ImageView) mView.findViewById(R.id.kainImage);
            Picasso.with(ctx).load(image).into(kain_image);

        }
    }

}
