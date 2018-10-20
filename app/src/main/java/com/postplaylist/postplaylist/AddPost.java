package com.postplaylist.postplaylist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;

public class AddPost extends AppCompatActivity
{
    private DatabaseReference dr;

    protected void onCreate(DatabaseReference d) {
        //super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);

        final RatingBar rating = (RatingBar) findViewById(R.id.rating_rating_bar);
        final EditText website = (EditText) findViewById(R.id.edit_text_website);
        final EditText link = (EditText) findViewById(R.id.edit_text_link);
        final Spinner type = (Spinner) findViewById(R.id.spinner_type);

        Button submitButton = (Button) findViewById(R.id.submit_button);
        //final TextView ratingDisplayTextView = (TextView) findViewById(R.id.rating_display_text_View);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (rating.getRating() == 0 || website.getText().toString().trim().equals("")  || link.getText().toString().trim().equals("")){
//
//                }

            }
        });
    }
}