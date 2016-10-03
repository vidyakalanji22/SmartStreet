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

import msd.com.utils.Constants;
import msd.com.utils.InteractRecords;
import msd.com.utils.ShareFirebase;

public class InteractedData extends AppCompatActivity {

    List<InteractRecords> interactRecordsList = new ArrayList<InteractRecords>();
    ListView listView;
    private Map<String, Object> mUserId;
    private String itemsUrl;

    private Firebase mFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interacted_data);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        itemsUrl = Constants.FIREBASE_URL + "/InteractionRecords/";
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


        InteractRecords records = ds.getValue(InteractRecords.class);
        interactRecordsList.add(records);


        if(interactRecordsList.size()>0){
            ArrayAdapter adapter = new InteractArrayAdapter(InteractedData.this,interactRecordsList);
            //ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, shareFirebaseList);
            System.out.println("shareList : "+interactRecordsList);
            listView.setAdapter(adapter);
        }else{
            Toast.makeText(InteractedData.this, "No data", Toast.LENGTH_SHORT);
        }
    }
}
