package msd.com.smartstreet;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import msd.com.utils.Constants;

public class CommentsCursorAdapter extends CursorAdapter{

    private String mUserId;
    private String itemsUrl;
    private Firebase mRef;

    /**
     * Constructor for the CommentsCursorAdapter
     * @param context
     * @param c
     * @param flags
     */
    public CommentsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /**
     * This initiates the newView for the list items
     * @param context
     * @param cursor
     * @param parent
     * @return
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.comment_list_item,parent,false);
    }

    /**
     * This binds the data with the view
     * @param convertView
     * @param context
     * @param cursor
     */


    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {

        mRef = new Firebase(Constants.FIREBASE_URL);

        final ImageView iv = (ImageView) convertView.findViewById(R.id.iv);
        final TextView tvComment = (TextView) convertView.findViewById(R.id.tvComment);
        final TextView tvUser = (TextView) convertView.findViewById(R.id.tvUser);
        final TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        final RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);

        itemsUrl = Constants.FIREBASE_URL + "/users/" + mUserId;

        // Use Firebase to populate the list.
        new Firebase(itemsUrl)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String commentText = (String) dataSnapshot.child("commentText").getValue();
                        Float rating = (Float) dataSnapshot.child("rating").getValue();

                        iv.setImageResource(R.drawable.ic_rating);
                        tvComment.setText(commentText);
                        ratingBar.setRating(rating);

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }

                });


                }
    }
