package com.example.jianwei.suteraproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;
/**
 * @author Jian Wei Hew eeyjwh@nottingham.ac.uk
 * @version 1
 * @since april 2018
 */
/**
 * When a item in the item menu is clicked, user will be redirected to a single item activity page,
 * Image, name, description and price of the chosen item will be displayed here,
 * User will then be allowed to add item to cart,
 * Admin will be able to access this page too for deleting item
 */
public class SingleKainActivity extends AppCompatActivity {

    private String kain_key = null;             //INITIALISATION
    private DatabaseReference mDatabase;
    private TextView singleKainTitle, singleKainDesc,singleKainPrice;
    private ImageView singleKainImage;
    private String kain_name,kain_price,kain_desc,kain_image;
    TextView m_response;

    /**
     *Print the image, name, description and price of the chosen item in this page.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_kain);


        kain_key = getIntent().getExtras().getString("KainId");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Item");

        singleKainDesc = findViewById(R.id.singleDesc);
        singleKainTitle = findViewById(R.id.singleTitle);           //LINK BACK TO XML FILE
        singleKainPrice = findViewById(R.id.singlePrice);
        singleKainImage = findViewById(R.id.singleImageView);
        m_response = findViewById(R.id.sponsor);
        mDatabase.child(kain_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                kain_name = (String) dataSnapshot.child("name").getValue();     //RETRIEVE DATA
                kain_price = (String) dataSnapshot.child("price").getValue();
                kain_desc = (String) dataSnapshot.child("desc").getValue();
                kain_image = (String) dataSnapshot.child("image").getValue();

                singleKainTitle.setText(kain_name);
                singleKainDesc.setText(kain_desc);              //PRINT DATA ON PHONE
                singleKainPrice.setText(kain_price+"£");
                Picasso.with(SingleKainActivity.this).load(kain_image).into(singleKainImage);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    /**
     *This button is only for users, add to cart button.
     * This button will add item into cart when pressed
     * The total price of the cart will be shown in textview
     */
    public void cartbutton(View view){
        if(LoginActivity.userkey==1){
            double j = Double.parseDouble(kain_price);          //ADD TO CART BUTTON, ADD ITEM INTO CART AND CALCULATE TOTAL
            Product product = new Product(kain_name,j);
            MenuActivity.lart.addToCart(product);
            m_response.setText("Shopping Cart Total= "+MenuActivity.lart.getValue()+"£");
        }
        else
            Toast.makeText(getApplicationContext(),"Admin cannot purchase",Toast.LENGTH_SHORT).show();

    }

    /**
     *This button is only for admins,
     * when this delete button is pressed,
     * the item will be removed from the database.
     * Once finish deleting, it will go out and back to the Admin menu activity page.
     * Clear activity history is done here
     */
    public void deleteMenu(View view){
        if(AdminLogin.allow == 1){
            mDatabase.child(kain_key).removeValue();        //ONLY FOR ADMIN TO DELETE ITEM FROM DATABASE
            Intent finishdel = new Intent(SingleKainActivity.this,AdminMenu.class);
            finishdel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //AFTER DELETE OPEN NEW ACTIVITY CLEAR HISTORY
            finishdel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(finishdel);
            finish();


        }
        else
            Toast.makeText(getApplicationContext(),"You are not admin",Toast.LENGTH_SHORT).show();
    }

}
