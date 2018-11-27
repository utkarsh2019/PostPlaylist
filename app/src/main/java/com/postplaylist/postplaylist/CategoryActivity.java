package com.postplaylist.postplaylist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText CatEditText;
    private ValueEventListener valueEventListener1;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Button logoutButton;
        Button savebutton;
        System.out.println("flag 8");
        logoutButton = (Button) findViewById(R.id.logout_button);
        savebutton = (Button) findViewById(R.id.save_button);
        CatEditText = (EditText)findViewById(R.id.category_edittext);

        logoutButton.setOnClickListener(this);
        savebutton.setOnClickListener(this);

        // setting up the listView to display the categories. They will be displayed asynchronously
        // after onStart is called. We pair stop and start for firebase with onStart and onStop
        ListView categoryList = findViewById(R.id.category_listview);

        ArrayAdapter<String> categoryListAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1);

        categoryList.setAdapter(categoryListAdapter);



        System.out.println("flag 8");
        }
    @Override
    public void onClick (View v) {
        switch (v.getId()){
            case R.id.logout_button :
                MainActivity.logout(getApplicationContext());
                break;

            case R.id.save_button:
                String CatName = CatEditText.getText().toString();
                if(CatName.isEmpty()){
                    AlertDialog alertDialog = new AlertDialog.Builder(CategoryActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Category Name field cannot be empty");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    break;
                }

                //TODO checks if category is already present from the database
                if(! MainActivity.performLoginCheckup(this))
                    return;

                else
                {
                    // add to the list of the categories
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference userRoot = db.getReference().child(
                            "Users/" + MainActivity.mAuth.getUid());
                    userRoot.child("categories").push().setValue(CatName);
                }
                break;
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        stopListening();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        setUpListening();

    }


    public void setUpListening()
    {
        System.out.println("flag 7");
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            return;

        ListView categoryList = findViewById(R.id.category_listview);
        final ArrayAdapter<String> categoryListAdapter = (ArrayAdapter<String>) categoryList.getAdapter();


        valueEventListener1 = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                System.out.println("flag 6");

                HashMap<String, Object> categoriesSnapShot;
                categoriesSnapShot = (HashMap<String, Object>)dataSnapshot.getValue();
                ArrayList<String> categories = new ArrayList<String>();
                for(Object val : categoriesSnapShot.values())
                    categories.add((String) val);
                categoryListAdapter.clear();
                categoryListAdapter.addAll(categories);
                categoryListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        };

        String uid;
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRoot = FirebaseDatabase.getInstance().
                getReference().
                child("Users/" + uid);

        userRoot.child("categories").addValueEventListener(valueEventListener1);
    }

    public void stopListening()
    {
        super.onStop();
        if(FirebaseAuth.getInstance() == null)
        {
            return;
        }

        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference userRoot = FirebaseDatabase.getInstance().getReference().child("Users/" + uid);
        userRoot.child("categories").removeEventListener(valueEventListener1);
    }
}
