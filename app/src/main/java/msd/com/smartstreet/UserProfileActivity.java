package msd.com.smartstreet;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import msd.com.utils.Constants;

public class UserProfileActivity extends AppCompatActivity {

    TextView uName;
    TextView mEmail;
    TextView mPassword;
    TextView contactNum;
    private String mUserId;
    private String itemsUrl;
    private Firebase mRef;

    /**
     * Initializes the username, password, email and phone number fields
     * and displays them. Gets the data from database
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        uName = (TextView) findViewById(R.id.uName);
        mPassword = (TextView) findViewById(R.id.password);
        mEmail = (TextView) findViewById(R.id.email);
        contactNum = (TextView) findViewById(R.id.phone);
        mRef = new Firebase(Constants.FIREBASE_URL);
        try {
            mUserId = mRef.getAuth().getUid();
        } catch (Exception e) {
           e.printStackTrace();
        }

        itemsUrl = Constants.FIREBASE_URL + "/users/" + mUserId;

        // Use Firebase to populate the list.
        new Firebase(itemsUrl)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String user = (String) dataSnapshot.child("user").getValue();
                        String email = (String) dataSnapshot.child("email").getValue();
                        String password = (String) dataSnapshot.child("password").getValue();
                        String phone = (String) dataSnapshot.child("phone").getValue();
                        uName.setText(user);
                        mEmail.setText(email);
                        mPassword.setText(password);
                        contactNum.setText(phone);

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }

                });

    }

}
