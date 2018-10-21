package com.postplaylist.postplaylist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    static final int AUTH_UI_REQUEST_CODE = 1;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ListView listView1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // This line checks if the user is already signed in
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            // already signed in

            FirebaseDatabase db = FirebaseDatabase.getInstance();

            // grabbing the user at that uid, as the reference
            DatabaseReference userRoot = db.getReference("Users/" + mAuth.getCurrentUser().
                    getUid());

            // will, in a higher level sense, start listening to the data.
            // it will set up the recycler and the listeners inside !!
            setUpDatabaseListening(userRoot);

            //initialising the settings menu button
            //change to category button
            FloatingActionButton categoryButton = findViewById(R.id.category_button);
            categoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                    startActivity(intent);
                }
            });


            // temporary to create data on the database
            String description = "a suggestion by Mohammad Ali!";
            String link = "google.com";
            int rating = 5;
            String[] categories1 = {"sports", "leisure", "food"};
            ArrayList<String> categories = new ArrayList<String>(Arrays.asList(categories1));
            PostItem postItem = new PostItem(description, categories, link, rating);
            userRoot.child("posts").push().setValue(postItem);
        }

        else
        {
            // not signed in already
            // open the gui for Firebase login via Google/Facebook/ etc.

            AuthUI authUI = AuthUI.getInstance();

            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.FacebookBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */,
                                    true /* hints */)
                            .setAvailableProviders(providers)
                            .build(),
                    AUTH_UI_REQUEST_CODE);
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AUTH_UI_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                recreate();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private void setUpDatabaseListening(DatabaseReference aUser)
    {
        RecyclerView recyclerView = findViewById(R.id.mainRecyclerView);

        // using a linear layout manager for the recycler view
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        final MyAdapter myAdapter = new MyAdapter(this, new ArrayList<PostItem>());
        recyclerView.setAdapter(myAdapter);

        ChildEventListener childEventListener = new ChildEventListener()
        {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                System.out.println("flag 1");
                System.out.println(dataSnapshot.getValue());

                myAdapter.add((PostItem) dataSnapshot.getValue());
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                System.out.println("flag 2");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)
            {
                System.out.println("flag 3");
                // remove the matching thing in the underlying arraylist
                // TODO: there is no equals defined in between two posts !
                myAdapter.delete((PostItem) dataSnapshot.getValue());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                System.out.println("flag 4");
            }
        };

        aUser.child("posts").addChildEventListener(childEventListener);
    }
}
