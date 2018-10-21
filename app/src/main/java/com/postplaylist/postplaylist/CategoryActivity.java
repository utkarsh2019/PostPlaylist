package com.postplaylist.postplaylist;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener{

    private Button logoutButton;
    private Button savebutton;
    private EditText CatEditText;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        logoutButton = (Button) findViewById(R.id.logout_button);
        savebutton = (Button) findViewById(R.id.save_btn);
        CatEditText = (EditText)findViewById(R.id.category_edittext);

        logoutButton.setOnClickListener(this);
        savebutton.setOnClickListener(this);
    }
    @Override
    public void onClick (View v) {
        switch (v.getId()){
            case R.id.logout_button :
                onBackPressed();

            case R.id.save_button :
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
                }

                //TODO checks if category is already present from the database
                else if(false){
                    AlertDialog alertDialog = new AlertDialog.Builder(CategoryActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Category Name already exists");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else {
                   //adds to database
                    onBackPressed();
                }

        }
    }

}
