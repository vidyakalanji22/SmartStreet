package msd.com.smartstreet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import msd.com.utils.Comments;

public class customArrayAdapter extends ArrayAdapter<Comments> {

    List<Comments> commentList;
    private Map<String, Object> mUserData;

    /**
     * array adapter for the comment layout
     * @param context
     * @param comments
     */
    public customArrayAdapter(Context context, List<Comments> comments) {
        super(context, R.layout.comment_list_item, comments);
        this.commentList = comments;

    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Comments getItem(int position) {
        return commentList.get(position);
    }

    /**
     * Gets the view and sets all the values
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.comment_list_item, parent, false);
        final ImageView iv = (ImageView) customView.findViewById(R.id.iv);
        final TextView tvComment = (TextView) customView.findViewById(R.id.tvComment);
        TextView tvUser = (TextView) customView.findViewById(R.id.tvUser);
        final TextView tvDate = (TextView) customView.findViewById(R.id.tvDate);
        final RatingBar ratingBar = (RatingBar) customView.findViewById(R.id.ratingBar);

        iv.setImageResource(R.drawable.ic_rating);
        Comments comment = getItem(position);
        tvComment.setText(comment.getCommentText());
        tvUser.setText(comment.getUser());
        tvDate.setText(comment.getCreated().toString());
        ratingBar.setRating((float) comment.getRating());
        return customView;
    }
}
