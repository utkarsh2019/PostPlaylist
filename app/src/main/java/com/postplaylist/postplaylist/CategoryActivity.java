package com.postplaylist.postplaylist;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener{

    private Button logoutButton;
    private Button savebutton;
    private EditText CatEditText;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        logoutButton = (Button) findViewById(R.id.logout_button);
        savebutton = (Button) findViewById(R.id.save_button);
        CatEditText = (EditText)findViewById(R.id.category_edittext);

        logoutButton.setOnClickListener(this);
        savebutton.setOnClickListener(this);
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

}
