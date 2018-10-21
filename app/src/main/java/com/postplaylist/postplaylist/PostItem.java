package com.postplaylist.postplaylist;

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


    public String getDate()
    {
        return date;
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

    static Comparator<PostItem> getClosestAddedComparator() {
        return new Comparator<PostItem>() {
            @Override
            public int compare(PostItem postItem1, PostItem postItem2) {
                return postItem1.getDate().compareTo(postItem2.getDate());
            }
        };
    }

    /*
    This method converts a mapping of a post item obtained from the firebase to a normal postItem
     */
    public static PostItem getFromMapping(HashMap<String, Object> mapping)
    {
        for(Map.Entry<String, Object> entry : mapping.entrySet())
        {
            if(entry.getKey().equals("description"))
            {

            }
        }

        return null;
    }
    //add more sorting options
}
