package com.example.jianwei.suteraproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
/**
 * @author Jian Wei Hew eeyjwh@nottingham.ac.uk
 * @version 1
 * @since april 2018
 */
/**
 * This class is for the main menu for admin,
 * There are only four button in this page,
 * Each button clicked will redirect to a new activity page
 */
public class AdminMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

    }
    /**
     * Open Add New Menu Item activity
     */
    public void addKainButtonClicked(View view){    //NEW ACTIVITY WHEN THIS BUTTON TRIGGERED
        Intent addKainIntent = new Intent(AdminMenu.this,AddKain.class);
        startActivity(addKainIntent);

    }
    /**
     * Open Item Menu activity with access to delete item from menu button
     */
    public void removeKainButtonClicked(View view){         //NEW ACTIVITY WHEN THIS BUTTON TRIGGERED
        Intent removeKainIntent = new Intent(AdminMenu.this,MenuActivity.class);
        startActivity(removeKainIntent);

    }
    /**
     * Open view customer order activity
     */
    public void viewOrders(View view){              //NEW ACTIVITY WHEN THIS BUTTON TRIGGERED
        Intent seeOrder = new Intent(AdminMenu.this,OpenOrders.class);
        startActivity(seeOrder);

    }
    /**
     * Remove all previous activity history and logout
     */
    public void logot1(View view){              //NEW ACTIVITY WHEN THIS BUTTON TRIGGERED
        Intent logot = new Intent(AdminMenu.this,MainActivity.class);
        logot.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);         //CLEAR ACTIVITY HISTORY
        logot.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logot);
        finish();

    }



}
