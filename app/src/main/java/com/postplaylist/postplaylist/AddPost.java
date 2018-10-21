package com.postplaylist.postplaylist;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.guna.libmultispinner.MultiSelectionSpinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AddPost extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener
{
    private List<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth == null) {
            finish();
        }
        DatabaseReference categories = FirebaseDatabase.getInstance().getReference("Users/"
                        +mAuth.getUid()+"/categories");

        String [] cats = {"Pics","Videos","News","Sports"};
        MultiSelectionSpinner type = (MultiSelectionSpinner) findViewById(R.id.spinner_type);
        type.setItems(cats);
        type.setSelection(new int[]{});
        type.setListener(this);


        final RatingBar rating = (RatingBar) findViewById(R.id.rating_rating_bar);
        final EditText website = (EditText) findViewById(R.id.edit_text_website);
        final EditText link = (EditText) findViewById(R.id.edit_text_link);

        Button submitButton = (Button) findViewById(R.id.submit_button);
        //final TextView ratingDisplayTextView = (TextView) findViewById(R.id.rating_display_text_View);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (link.getText().toString().trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Please enter a link", Toast.LENGTH_SHORT).show();
                }
                else {
                    System.out.println("Rating"+rating.getRating());
                    System.out.println("Website"+website.getText().toString().trim());
                    System.out.println("Link"+link.getText().toString().trim());
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {
        Toast.makeText(this, strings.toString(), Toast.LENGTH_LONG).show();
    }
}