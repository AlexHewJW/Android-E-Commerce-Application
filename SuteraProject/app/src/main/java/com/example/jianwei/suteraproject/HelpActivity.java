package com.example.jianwei.suteraproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
/**
 * @author Jian Wei Hew eeyjwh@nottingham.ac.uk
 * @version 1
 * @since april 2018
 */
/**
 * This class is for users to send complaint to the admin,
 * This involves recording subject and complain message,
 * Then open email app in the phone and automatically paste the message
 */
public class HelpActivity extends AppCompatActivity {
    private EditText mEditTextSubject,mEditTextMessage;

    /**
     *Initialisation
     * Initialise button
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        mEditTextSubject = findViewById(R.id.edit_text_subject);           //LINK TO XML FILE
        mEditTextMessage = findViewById(R.id.edit_text_message);

        Button buttonSend = findViewById(R.id.button_send);             //INITIALISE THE SEND BUTTON
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

    }
    /**
     *When the button is clicked, mail app opens
     * Retrieve subject and message from edittext
     * Receipent's email, subject and message are pasted automatically to the mail app
     *
     */
    private void sendMail(){
        String receipientList = "alexhewjianwei@yahoo.com";
        String[] receipients = receipientList.split(",");

        String subject = mEditTextSubject.getText().toString();        //RECEIVE SUBJECT TEXT
        String message = mEditTextMessage.getText().toString();         //RECEIVE MESSAGE TEXT

        Intent intent = new Intent(Intent.ACTION_SEND);             //OPEN MAIL APP
        intent.putExtra(Intent.EXTRA_EMAIL,receipients);            //PASTE SUBJECT AND MESSAGE ONTO EMAIL APP
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose email app"));

    }
}
