package com.postplaylist.postplaylist;

import android.app.Activity;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    static final int AUTH_UI_REQUEST_CODE = 1;
    public static FirebaseAuth mAuth;

    // Posts and categories of the user. TODO: these might me in size of MB's later on !?
    public static ArrayList<String> categories;
    public static ArrayList<String> posts;
    public static MyAdapter myAdapter;

    ChildEventListener childEventListener1;

    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpUI();
        // This line checks if the user is already signed in
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            // already signed in

            //initialising the settings menu button
            //change to category button
            FloatingActionButton categoryButton = findViewById(R.id.fab);
            categoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent startAddPost = new Intent(MainActivity.this, AddPost.class);
                    startActivity(startAddPost);
                }
            });

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
            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
            startActivity(intent);
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
                mAuth = FirebaseAuth.getInstance();
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
        myAdapter = new MyAdapter(this, new ArrayList<PostItem>());
        recyclerView.setAdapter(myAdapter);

        childEventListener1 = new ChildEventListener()
        {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                System.out.println("flag 1");
                System.out.println(dataSnapshot.getValue());

                myAdapter.add(PostItem.getFromMapping(dataSnapshot));
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
                myAdapter.delete(PostItem.getFromMapping(dataSnapshot));
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

        aUser.child("posts").addChildEventListener(childEventListener1);
    }

    public void setUpUI()
    {
        //setting up the spinner
        Spinner sortSpinner = (Spinner) findViewById(R.id.sort_spinner);
        ArrayList<String> sortByStrings = new ArrayList<>();
        sortByStrings.add("Date: Newest");
        sortByStrings.add("Date: Oldest");
        sortByStrings.add("Rating: Highest");
        sortByStrings.add("Rating: Lowest");
        sortByStrings.add("Category");
        ArrayAdapter arrayAdapter = new ArrayAdapter(
                getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item,
                sortByStrings);
        sortSpinner.setAdapter(arrayAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(myAdapter == null)
                    return;
                switch (position)
                {
                    case 0:
                        // pass
                        break;
                    case 1:
                        //TODO: Change the myadapter thingmthingm
                        myAdapter.sort(new Comparator<PostItem>()
                        {
                            @Override
                            public int compare(PostItem t1, PostItem t2) {
                                return t1.getDate().compareTo(t2.getDate());
                            }
                        });
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        myAdapter.sort(new Comparator<PostItem>()
                        {
                            @Override
                            public int compare(PostItem t1, PostItem t2) {
                                return t1.getDate().compareTo(t2.getDate());
                            }
                        });
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 3:
                        myAdapter.sort(new Comparator<PostItem>()
                        {
                            @Override
                            public int compare(PostItem t1, PostItem t2) {
                                float rating1 = t1.getRating();
                                float rating2 = t2.getRating();
                                if(rating1 > rating2){
                                    return -1;
                                }
                                else if(rating2 > rating1){
                                    return 1;
                                }
                                else {
                                    return 0;
                                }
                            }
                        });
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 4:
                        myAdapter.sort(new Comparator<PostItem>() {
                            @Override
                            public int compare(PostItem t1, PostItem t2) {
                                float rating1 = t1.getRating();
                                float rating2 = t2.getRating();
                                if(rating1 < rating2){
                                    return -1;
                                }
                                else if(rating2 < rating1){
                                    return 1;
                                }
                                else {
                                    return 0;
                                }
                            }
                        });
                        myAdapter.notifyDataSetChanged();
                        break;

                    case 5:
                        myAdapter.sort(new Comparator<PostItem>() {
                                           @Override
                                           public int compare(PostItem t1, PostItem t2) {
                                               if (t1.getCategories()!= null && t2.getCategories() != null){
                                                   ArrayList<String> cats1 = t1.getCategories();
                                                   ArrayList<String> cats2 = t2.getCategories();
                                                   Collections.sort(cats1);
                                                   Collections.sort(cats2);
                                                   String cat1 = cats1.get(0);
                                                   String cat2 = cats2.get(0);
                                                   int Val = cat1.compareToIgnoreCase(cat2);
                                                   if(Val > 0){
                                                       return -1;
                                                   }
                                                   else if(Val < 0){
                                                       return 1;
                                                   }
                                                   else {
                                                       return 0;
                                                   }
                                               }
                                               else
                                                   return 0;
                                           }
                                       }
                        );
                        myAdapter.notifyDataSetChanged();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // set up a button
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
    }

    public static boolean performLoginCheckup(Context context)
    {
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            System.out.println("flag 11");
            Intent openLogin = new Intent(context, MainActivity.class);
            openLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(openLogin);
            // there is no back from the login page, OK USER .!?
            return false;
        }

        else
            return true;
    }

    public static void logout(Context context)
    {
        FirebaseAuth.getInstance().signOut();
        mAuth = null;
        posts = null;
        categories = null;

        // TODO: clear the adapter data after a logout too. Or think about clearing the window data
//        MainActivity.myAdapter
        performLoginCheckup(context);
    }

    public void stopListening()
    {
        super.onStop();
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            return;

        String uid = mAuth.getUid();
        DatabaseReference userRoot = FirebaseDatabase.getInstance().getReference().child("Users/" + uid);
        userRoot.child("/posts").removeEventListener(childEventListener1);
        System.out.println("flag 9");
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

        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            return;

        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference userRoot;
        // grabbing the user at that uid, as the reference
        userRoot = db.getReference("Users/" + mAuth.getCurrentUser().
                getUid());

        // will, in a higher level sense, start listening to the data.
        // it will set up the recycler and the listeners inside !!
        setUpDatabaseListening(userRoot);
    }
}