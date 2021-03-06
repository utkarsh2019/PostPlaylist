package com.postplaylist.postplaylist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
{
    Context context;
    ArrayList<PostItem> posts;
    public MyAdapter(Context context, ArrayList<PostItem> postItems)
    {
        this.context = context;
        this.posts = postItems;

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        View v;
        public ViewHolder(View v)
        {
            // we need to call the super class for book keeping ??
            super(v);

            this.v = v;
        }
    }
    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // If we do not give a root to add the view into, then the attributes of the view at the
        // root of that XML file would be lost as there are no parents for it to have a proper
        // LayoutParams on it.
        // Further, RecyclerView requires that we do not attach to that root, and that is done by
        // giving an extra argument to inflate
        View v = inflater.inflate(R.layout.post_item_layout, parent, false);
        System.out.println("flag 12");
        if(ViewGroup.LayoutParams.MATCH_PARENT == v.getLayoutParams().width)
        {
            System.out.println("MATCH_PARENT (-1)");
        }
        else
            System.out.println("not MATCH_PARENT");
        ViewHolder viewHolder = new ViewHolder(v);

        // TODO: add a listeners for the view itself and the link button
        return viewHolder;
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position)
    {
        View v = holder.v;
        final PostItem post = posts.get(position);
        TextView textView = v.findViewById(R.id.detailsText1);
        textView.setText(post.getDescription());

        RatingBar ratingBar = v.findViewById(R.id.ratingBar1);
        ratingBar.setRating((float)post.getRating());
        v.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent editIntent = new Intent(view.getContext(), AddPost.class);
                editIntent.putExtra("editFlag", true);
                editIntent.putExtra("post", post);
                context.startActivity(editIntent);
            }

        });
        Button button = v.findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String link;
                if(! post.getLink().startsWith("http"))
                    link = "https://" + post.getLink();
                else
                    link = post.getLink();

                Uri uri = Uri.parse(link);

                // Open a web link !!!!
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                // basically run a startActivity from a good context (of the app)
                view.getContext().getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return posts.size();
    }

    public void delete(PostItem postItem)
    {
        // delete from the posts, which are the source of truth for the UI
        posts.remove(postItem);
    }

    public void add(PostItem postItem)
    {
        posts.add(postItem);
    }

    public void clearAll()
    {
        posts.clear();
    }
    public void sort(Comparator<PostItem> comp)
    {
        Collections.sort(posts, comp);
    }
}
