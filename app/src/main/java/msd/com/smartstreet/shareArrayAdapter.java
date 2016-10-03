package msd.com.smartstreet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import msd.com.utils.ShareFirebase;

public class shareArrayAdapter extends ArrayAdapter<ShareFirebase> {

    List<ShareFirebase> shareFirebaseList;
    private Map<String, Object> mUserData;

    /**
     * adapter for the shared list
     * @param context
     * @param sharedRecords
     */
    public shareArrayAdapter(Context context, List<ShareFirebase> sharedRecords) {
        super(context, R.layout.share_list_item, sharedRecords);
        this.shareFirebaseList = sharedRecords;
    }


    @Override
    public ShareFirebase getItem(int position) {
        return shareFirebaseList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        System.out.println("Entered getView");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.share_list_item, parent, false);
        final TextView text_view_app = (TextView) customView.findViewById(R.id.text_view_app);
        final TextView text_view_date = (TextView) customView.findViewById(R.id.text_view_date);


        ShareFirebase share = getItem(position);
        text_view_app.setText(share.getDesc());
        text_view_date.setText(share.getShared().toString());

        return customView;
    }
}
