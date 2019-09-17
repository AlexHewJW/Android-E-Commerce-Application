package com.example.jianwei.suteraproject;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
/**
 * @author Jian Wei Hew eeyjwh@nottingham.ac.uk
 * @version 1
 * @since april 2018
 */
/**
 * This class add new menu item into the database,
 * This involves opening phone gallery getting image uri,
 * Involves receiving text from edittext and upload all to database under the title item
 */

public class AddKain extends AppCompatActivity {


    private ImageButton itemImage;              //INITIALISATION
    private static final int GALLREQ = 1;
    private EditText name, description, price;
    private Uri uri = null;
    private StorageReference storageReference = null;
    private DatabaseReference mRef;
    private FirebaseDatabase firebaseDatabase;
    /**
     * Initialisation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kain);
        name = (EditText) findViewById(R.id.itemName);                                 //LINK TO XML FILES
        description = (EditText) findViewById(R.id.itemDesc);
        price = (EditText) findViewById(R.id.itemPrice);
        storageReference = FirebaseStorage.getInstance().getReference("Item");    //DATABASE FIND DATA UNDER ITEM
        mRef = FirebaseDatabase.getInstance().getReference("Item");

    }
    /**
     * Open Phone Gallery when green box is clicked
     */
    public void ImageButtonClicked(View view) {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);   //OPEN PHONE GALLERY WHEN PRESSED
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLREQ);

    }
    /**
     * Storing image uri
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLREQ && resultCode==RESULT_OK){
            uri = data.getData();                                           //GET THE IMAGE URI
            itemImage = (ImageButton) findViewById(R.id.ItemImageButton);
            itemImage.setImageURI(uri);
        }


    }
    /**
     * Upload new item with image into firebase
     * Clear activity history
     * Move to Admin Menu activity
     */
    public void uploadItemButtonClicked(View view) {

        final String name_text = name.getText().toString().trim();
        final String desc_text = description.getText().toString().trim();
        final String price_text = price.getText().toString().trim();

        if(!TextUtils.isEmpty(name_text) && !TextUtils.isEmpty(desc_text) && !TextUtils.isEmpty(price_text)){
            StorageReference filepath = storageReference.child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadurl = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newPost = mRef.push();
                    newPost.child("name").setValue(name_text);                  //ADD NEW DATA TO DATABASE WHEN PRESSED
                    newPost.child("desc").setValue(desc_text);
                    newPost.child("price").setValue(price_text);
                    newPost.child("image").setValue(downloadurl.toString());

                }
            });
            Toast.makeText(AddKain.this,"Image Uploaded",Toast.LENGTH_LONG).show();
            Intent balik = new Intent(this,AdminMenu.class);
            balik.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);                        //CLEAR ACTIVITY HISTORY
            balik.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(balik);
            finish();
        }
    }

}
