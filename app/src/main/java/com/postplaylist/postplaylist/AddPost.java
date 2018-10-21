package com.postplaylist.postplaylist;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.guna.libmultispinner.MultiSelectionSpinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddPost extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener
{
    private boolean flag;
    private ArrayList<String> newSelectedArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);
        Bundle extras = getIntent().getExtras();
        flag = extras.getBoolean("editFlag",false);
        ArrayList<String> allCategories = new ArrayList<>();
        allCategories.add("Pics");
        allCategories.add("Videos");
        allCategories.add("News");
        allCategories.add("Sports");

        final String uid;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //To be used for Utkarsh only. Set to 0 for all other developers
        int testUtkarsh = 1;

        if(mAuth.getCurrentUser() == null && testUtkarsh != 1) {
            finish();
        }


        MultiSelectionSpinner type = (MultiSelectionSpinner) findViewById(R.id.spinner_type);
        String[] cats = {"Pics", "Videos", "News", "Sports"};
        type.setItems(cats);
        type.setListener(this);

        RatingBar r = (RatingBar) findViewById(R.id.rating_rating_bar);
        EditText w = (EditText) findViewById(R.id.edit_text_website);
        EditText l = (EditText) findViewById(R.id.edit_text_link);

        Button submitButton = (Button) findViewById(R.id.submit_button);
        Button deleteButton = (Button) findViewById(R.id.delete_button);

        if (testUtkarsh != 1) {
            uid = mAuth.getCurrentUser().getUid();
        } else {
            uid = "Y1ftkRetggVB7nRm18Sxw17B85G3";
        }

        DatabaseReference categories = FirebaseDatabase.getInstance().getReference("Users/"
                + uid + "/categories");

        if(!flag) {
            type.setSelection(new int[]{});

            final RatingBar rating = (RatingBar) findViewById(R.id.rating_rating_bar);
            final EditText website = (EditText) findViewById(R.id.edit_text_website);
            final EditText link = (EditText) findViewById(R.id.edit_text_link);

            deleteButton.setClickable(false);

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (link.getText().toString().trim().equals("")) {
                        Toast.makeText(getBaseContext(), "Please enter a link", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else {
                        long outRating = (long)rating.getRating();
                        String outDescription = website.getText().toString().trim();
                        String outLink = link.getText().toString().trim();

                        DatabaseReference posts = FirebaseDatabase.getInstance().getReference().child(
                                "Users/"+uid+"/posts");
                        String outKey = posts.push().getKey();
                        PostItem newPost = new PostItem(outDescription,newSelectedArray,outLink,outRating,outKey);
                        posts.child(outKey).setValue(newPost);
                        finish();
                    }
                }
            });
        }
        else {
            final int pos = extras.getInt("position");
            PostItem post = MainActivity.posts.get(pos);
            String inDescription = post.getDescription();
            String inLink = post.getLink();

            ArrayList<String> inCategory = post.getCategories();
            if(inCategory == null)
                inCategory = new ArrayList<String>();
            long inRating = post.getRating();
            final String inKey = post.getKey();

            r.setRating((float)inRating);
            w.setText(inDescription);
            l.setText(inLink);
            ArrayList<Integer> index = new ArrayList<>();
            for (int i=0;i < inCategory.size();i++) {
                for (int j=0;j < allCategories.size();j++) {
                    if (inCategory.get(i).equals(allCategories.get(j))) {
                        index.add(j);
                        break;
                    }
                }
            }
            int [] in = new int[index.size()];
            for (int i=0;i < index.size(); i++) {
                in[i] = index.get(i);
            }
            type.setSelection(in);

            final RatingBar rating = (RatingBar) findViewById(R.id.rating_rating_bar);
            final EditText website = (EditText) findViewById(R.id.edit_text_website);
            final EditText link = (EditText) findViewById(R.id.edit_text_link);

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (link.getText().toString().trim().equals("")) {
                        Toast.makeText(getBaseContext(), "Please enter a link",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                    else {
                        long outRating = (long)rating.getRating();
                        String outDescription = website.getText().toString().trim();
                        String outLink = link.getText().toString().trim();
                        PostItem newPost = new PostItem(outDescription,newSelectedArray,
                                outLink,outRating,inKey);
                        DatabaseReference posts = FirebaseDatabase.getInstance().getReference(
                                "Users/"+uid+"/posts/"+inKey);
                        posts.setValue(newPost);
                        MainActivity.posts.set(pos, newPost);
                        finish();
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference posts = FirebaseDatabase.getInstance().getReference(
                            "Users/"+uid+"/posts/"+inKey);
                    posts.setValue(null);
                    finish();
                }
            });
        }
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
    public void selectedStrings(List<String> categories) {
        newSelectedArray = new ArrayList<String>(categories);
    }
}