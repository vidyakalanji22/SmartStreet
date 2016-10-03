package msd.com.smartstreet;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.List;
import java.util.Map;

import msd.com.utils.InteractRecords;
import msd.com.utils.ShareFirebase;

/**
 * Array adapter for the interact list
 */
public class InteractArrayAdapter extends ArrayAdapter<InteractRecords>{

    private Map<String, Object> mUserData;
    List<InteractRecords> interactRecordsList;

    public InteractArrayAdapter(Context context, List<InteractRecords> interactRecordsList) {
        super(context, R.layout.interact_list,interactRecordsList);
        this.interactRecordsList = interactRecordsList;
    }

    @Override
    public InteractRecords getItem(int position) {

        return interactRecordsList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.interact_list, parent, false);
        final TextView tvTree = (TextView) customView.findViewById(R.id.tvTree);
        final TextView tvDate = (TextView) customView.findViewById(R.id.tvDate);
        final TextView tvUser = (TextView) customView.findViewById(R.id.userN);
        final TextView tvMedia = (TextView) customView.findViewById(R.id.tvMedia);


        InteractRecords records = getItem(position);
        tvTree.setText(records.getInteractedTree());
        tvDate.setText(records.getInteractedDate().toString());
        tvUser.setText(records.getInteractedUser());
        tvMedia.setText(records.getInteractedMedia());

        return customView;
    }
}
