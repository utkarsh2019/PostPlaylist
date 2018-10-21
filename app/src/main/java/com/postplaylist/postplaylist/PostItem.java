package com.postplaylist.postplaylist;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/*
TODO: We still need to provide a str to date conversion. With that, we will make a getter/setter
for the date field in this class. After that, we can store/remember/serialize the date field too
assign to: Mazahir
 */
public class PostItem implements Serializable{

    private String description;
    private String date;
    private ArrayList<String> categories;
    private String link;
    private int rating;

    //getting current date and time
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat mdformat = new SimpleDateFormat("MM-dd-yyyy",
            java.util.Locale.getDefault());
    private String strDate = mdformat.format(calendar.getTime());

    public PostItem()
    {
        // Default constructor required for calls to DataSnapshot.getValue(PostItem.class)
    }

    //constructor
    /*
        This constructor is to be used for internal purposes and probably when adding a post
        The date of this PostItem instance is the time of generation
     */
    public PostItem (String description, ArrayList<String> categories, String link, int rating){
        this.description = description;
        this.date = strDate;
        this.categories = categories;
        this.link = link;
        this.rating = rating;
    }

    //getters and setters
    public String getDescription() {
        return description;
    }
    public void setDescription (String description){this.description = description; }


    public ArrayList<String> getCategories() {
        return categories;
    }
    public void setCategories(ArrayList<String> categories){this.categories = categories;}

    public int getRating() {
        return rating;
    }
    public void setRating(int rating){this.rating = rating;}

    public String getLink()
    {
        return link;
    }
    public void setLink(String link)
    {
        this.link = link;
    }

    public String getDate()
    {
        return date;
    }
    public void setDate(String date)
    {
        this.date = date;
    }

    static Comparator<PostItem> getClosestComparator() {
        return new Comparator<PostItem>() {
            @Override
            public int compare(PostItem postItem1, PostItem postItem2) {
                return postItem1.getDate().compareTo(postItem2.getDate());
            }
        };
    }
    static Comparator<PostItem> getFarthestComparator() {
        return new Comparator<PostItem>() {
            @Override
            public int compare(PostItem postItem1, PostItem postItem2) {
                return postItem2.getDate().compareTo(postItem1.getDate());
            }
        };
    }

    /*
    This method converts a mapping of a post item obtained from the firebase to a normal postItem
     */
    public static PostItem getFromMapping(DataSnapshot dataSnapshot)
    {
        for(DataSnapshot post: dataSnapshot.getChildren()){
            String description = (String) post.child("description").getValue();
            String link = (String) post.child("link").getValue();
            String date = (String) post.child("date").getValue();
            int rating = (int) post.child("rating").getValue();
            ArrayList categories = (ArrayList) post.child("categories").getValue();
            PostItem postItem = new PostItem(description, categories, link, rating);
            postItem.setDate(date);
            return postItem;
        }
        return null;
    }
    //add more sorting options


    @Override
    public boolean equals(Object obj) {
        if (this.getLink().equals(((PostItem) obj).getLink())){
            return true;
        }
        else
            return false;
    }
}
