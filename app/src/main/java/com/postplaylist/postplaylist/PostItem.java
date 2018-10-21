package com.postplaylist.postplaylist;

import java.io.Serializable;
import java.util.Comparator;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PostItem implements Serializable{

    private String description;
    private String date;
    private String tag;
    private int rating;

    //getting current date and time
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat mdformat = new SimpleDateFormat("MM-dd-yyyy",
            java.util.Locale.getDefault());
    private String strDate = mdformat.format(calendar.getTime());

    //constructor with tags
    public PostItem (String description, String tag, int rating){
        this.description = description;
        this.date = strDate;
        this.tag = tag;
        this.rating = rating;
    }

    //constructor without tags
    public PostItem(String description, int rating){
        this.description = description;
        this.date = strDate;
        this.rating = rating;
    }
    //getters and setters for the 4 characteristics
    public String getDescription() {
        return description;
    }
    public void setDescription (String description){this.description = description; }

    public String getDate() {
        return date;
    }
    //don't need a setter for date

    public String getTag() {
        return tag;
    }
    public void setTag(String tag){this.tag = tag;}

    public int getRating() {
        return rating;
    }
    public void setRating(int rating){this.rating = rating;}

    static Comparator<PostItem> getClosestAddedComparator() {
        return new Comparator<PostItem>() {
            @Override
            public int compare(PostItem postItem1, PostItem postitem2) {
                int val = postItem1.getDate().compareTo(postitem2.getDate());
                return val;
            }
        };
    }

    //add more sorting options
}
