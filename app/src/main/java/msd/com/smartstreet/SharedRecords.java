package msd.com.smartstreet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import msd.com.utils.Comments;
import msd.com.utils.Constants;
import msd.com.utils.ShareFirebase;

public class SharedRecords extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 100;
    List<ShareFirebase> shareFirebaseList = new ArrayList<ShareFirebase>();
    ListView listView;
    private Map<String, Object> mUserId;
    private String itemsUrl;
    private Firebase mFirebaseRef;

    /**
     * sets the custom list view for the shared records.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_records);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        try {
            mUserId = mFirebaseRef.getAuth().getProviderData();
            System.out.println("User ProviderData : " +mUserId);
        } catch (Exception e) {

        }

        listView = (ListView)findViewById(android.R.id.list);
        this.retrieveSharedData();
    }

    private void retrieveSharedData() {

        itemsUrl = Constants.FIREBASE_URL + "/ShareDetails/";
        System.out.println("URL value : " +itemsUrl);

        new Firebase(itemsUrl)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        getUpdates(dataSnapshot);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        getUpdates(dataSnapshot);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
    }

    private void getUpdates(DataSnapshot ds){


        ShareFirebase shareFirebase = ds.getValue(ShareFirebase.class);
        shareFirebaseList.add(shareFirebase);


        if(shareFirebaseList.size()>0){
            ArrayAdapter adapter = new shareArrayAdapter(SharedRecords.this,shareFirebaseList);
            //ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, shareFirebaseList);
            System.out.println("shareList : "+shareFirebaseList);
            listView.setAdapter(adapter);
        }else{
            Toast.makeText(SharedRecords.this, "No data", Toast.LENGTH_SHORT);
        }
    }
}
