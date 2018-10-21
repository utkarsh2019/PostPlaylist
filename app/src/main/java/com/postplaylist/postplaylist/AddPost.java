package com.postplaylist.postplaylist;


import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.guna.libmultispinner.MultiSelectionSpinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddPost extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener
{
    private ArrayList<String> c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final String uid;

        // TODO: call only if edit is on
            // Kalpan code
            runAsyncQuery(getBaseContext());
            // Kalpan code

        //To be used for Utkarsh only. Set to 0 for all other developers
        int testUtkarsh = 1;

        if (testUtkarsh != 1) {
            uid = mAuth.getUid();
        }
        else {
            uid = "Y1ftkRetggVB7nRm18Sxw17B85G3";
        }

        if (mAuth == null && testUtkarsh != 1) {
            finish();
        }
        DatabaseReference categories = FirebaseDatabase.getInstance().getReference("Users/"
                        +uid+"/categories");

        String [] cats = {"Pics","Videos","News","Sports"};
        MultiSelectionSpinner type = (MultiSelectionSpinner) findViewById(R.id.spinner_type);
        type.setItems(cats);
        type.setSelection(new int[]{});
        type.setListener(this);


        final RatingBar rating = (RatingBar) findViewById(R.id.rating_rating_bar);
        final EditText website = (EditText) findViewById(R.id.edit_text_website);
        final EditText link = (EditText) findViewById(R.id.edit_text_link);

        Button submitButton = (Button) findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (link.getText().toString().trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Please enter a link", Toast.LENGTH_SHORT)
                            .show();
                }
                else {
                    long r = (long)rating.getRating();
                    String d = website.getText().toString().trim();
                    String l = link.getText().toString().trim();
                    PostItem newPost = new PostItem(d,c,l,r);
                    DatabaseReference posts = FirebaseDatabase.getInstance().getReference(
                            "Users/"+uid+"/posts");
                    posts.push().setValue(newPost);
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

    public void setGUI(ArrayList<String> categories)
    {
        // just a dummy function to pass
    }
    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> categories) {
        c = new ArrayList<String>(categories);
    }

    public void runAsyncQuery(Context context)
    {
        if(! MainActivity.performLoginCheckup(context))
            return;

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRoot = FirebaseDatabase.getInstance().getReference().child("Users/" +
                                                                                            uid);
        ValueEventListener valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                ArrayList<String> categories;
                categories = (ArrayList<String>) dataSnapshot.getValue();
                setGUI(categories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        };

        userRoot.child("categories").addValueEventListener(valueEventListener);

    }
}