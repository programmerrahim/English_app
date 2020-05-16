package com.greenlove.photocontest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddNewActivity extends AppCompatActivity {

    private String name,description,cdate;
    private EditText nameEditTxt;
    private EditText descriptionEditTxt;
    private Button addButton;

    private ProgressDialog loadingBar;

    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        this.setTitle("Add New Activity");

        mFirestore = FirebaseFirestore.getInstance();

        nameEditTxt = findViewById(R.id.addNewNameId);
        descriptionEditTxt = findViewById(R.id.addNewDescriptionId);
        addButton = findViewById(R.id.addButtonId);

        loadingBar = new ProgressDialog(this);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateGovtJobAddData();
            }
        });


    }

    //validateGovtJobAddData method start
    private void validateGovtJobAddData() {

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
        String currentDate = dateFormat.format(date);

        name = nameEditTxt.getText().toString();
        description = descriptionEditTxt.getText().toString();
        cdate = currentDate;



         if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please write name.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please write Description", Toast.LENGTH_SHORT).show();
        }
        else{
             SaveProductInfoToDatabase();
             loadingBar.show();
        }
    }
    //validateGovtJobAddData method ends

    //SaveProductInfoToDatabase method starts
    private void SaveProductInfoToDatabase() {



        Map<String,Object> productMap=new HashMap<>();
        productMap.put("name",name);
        productMap.put("description",description);
        productMap.put("date",cdate);

        mFirestore.collection("Products").add(productMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    loadingBar.dismiss();
                    Intent mintent = new Intent(AddNewActivity.this,MainActivity.class);
                    startActivity(mintent);
                    finish();
                    Toast.makeText(AddNewActivity.this, "Product is added successfully.", Toast.LENGTH_SHORT).show();
                }else{
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(AddNewActivity.this, "Error :" +message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    //SaveProductInfoToDatabase method ends
}
