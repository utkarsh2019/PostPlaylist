// old implementation, not being utilized now

package com.postplaylist.postplaylist;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;


import java.util.ArrayList;



public class PostPlaylistAdapter extends ArrayAdapter{
    public PostPlaylistAdapter(@NonNull Context context, ArrayList<PostItem> posts)
    {
        super(context, R.layout.post_item_layout);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return null;
    }

}
