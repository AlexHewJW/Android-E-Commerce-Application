package com.example.jianwei.suteraproject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
/**
 * @author Jian Wei Hew eeyjwh@nottingham.ac.uk
 * @version 1
 * @since april 2018
 */
/**
 * Shopping cart is shown here,
 * Total Expenditure is displayed here,
 * Reset button will reset the whole shopping cart,
 * Checkout Button will redirect the user to the paypal page to make payment
 */
public class ViewCart extends AppCompatActivity {
    TextView counttotal;                            //initialisation
    ArrayList<String> users;
    PayPalConfiguration m_configuration;
    String m_paypalClientId = "AeWO7Biq0vtA9A247N8mtLYoXlhCCG2bzbBAe9bwHe19bkrej8bMvJQ5j4orUWsOKfru4x7k37ctJX8P";
    Intent m_service;
    int m_paypalRequestCode = 999;
    private static final String TAG = "ViewCart";

    /**
     * Initialisations
     * Print the item and price logic
     * Display all of the selected items in the shopping cart with the price.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        Cart cart = MenuActivity.lart;
        counttotal = (TextView) findViewById(R.id.tspending);    //link to the textview in xml file
        m_configuration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(m_paypalClientId);  //PAYPAL CODE
        //sandbox for testing, no real money paypal test
        m_service = new Intent(this, PayPalService.class);
        m_service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,m_configuration); //configuration above
        startService(m_service); //paypal service, listening to call to paypal app
        LinearLayout cartLayout = (LinearLayout) findViewById(R.id.cart);
        users = new ArrayList<>();
        Set<Product> products = cart.getProducts();

        Iterator iterator = products.iterator();
        while (iterator.hasNext()){
            Product product = (Product) iterator.next();

            //logic
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            TextView name = new TextView(this);
            TextView quantity = new TextView(this);
            name.setText(product.getName());
            users.add(product.getName());
            quantity.setText(Double.toString(product.getValue())+"£");
            linearLayout.addView(name);
            linearLayout.addView(quantity);

            //display
            name.setTextSize(15);    //print item name
            quantity.setTextSize(15);//print item price
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,50, Gravity.CENTER);
            layoutParams.setMargins(20,50,20,50);
            linearLayout.setLayoutParams(layoutParams);
            name.setLayoutParams(new TableLayout.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT,1));
            quantity.setLayoutParams(new TableLayout.LayoutParams(0,ActionBar.LayoutParams.WRAP_CONTENT,1));
            name.setGravity(Gravity.CENTER);
            quantity.setGravity(Gravity.CENTER);
            cartLayout.addView(linearLayout);
            counttotal.setText("Total Spending = "+MenuActivity.lart.getValue()+"£");
            for(int i = 0; i<users.size();i++){
                Log.d(TAG, "onCreate: name: "+users.get(i));
            }

        }
    }
    /**
     * This button when pressed, will open the paypal 3rd party page.
     * The total cart value will be passed into the paypal page.
     */
    public void payp(View view){  //basic paypal code, open 3rd party paypal page when checkut button is pressed
        PayPalPayment cart = new PayPalPayment(new BigDecimal(MenuActivity.lart.getValue()),"GBP","Cart",PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this,PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,m_configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,cart);
        startActivityForResult(intent,m_paypalRequestCode);

    }
    /**
     * This button will reset the whole shopping cart when pressed
     */
    public void resetcart(View view){  //reset shopping cart when the reset button is pressed
        MenuActivity.lart.empty();
        MenuActivity.i=0;
        counttotal.setText("");
        startActivity(new Intent(ViewCart.this,MenuActivity.class));
        finish();

    }
    /**
     * If the payment is successful, open the upload order form page.
     * Fail payment procedure will show notice.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data){  //basic paypal code
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == m_paypalRequestCode){
            if (resultCode == Activity.RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null){
                    String state = confirmation.getProofOfPayment().getState();
                    if (state.equals("approved")) {
                        counttotal.setText("payment approved");
                        Intent uploaddata = new Intent(this, TestingCheckout.class);  //new activity page when paypal is success
                        startActivity(uploaddata);
                    }
                    else
                        counttotal.setText("Error in the payment");
                }
                else
                    counttotal.setText("confirmation is null");
            }
        }

    }
}
