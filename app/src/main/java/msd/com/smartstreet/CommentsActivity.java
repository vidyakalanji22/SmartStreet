package msd.com.smartstreet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
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

public class CommentsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int EDITOR_REQUEST_CODE = 100;
    List<Comments> commentList = new ArrayList<Comments>();
    ListView listView = null;
    private CursorAdapter cursorAdapter;
    private Map<String, Object> mUserId;
    private String itemsUrl;

    /* References to the Firebase */
    private Firebase mFirebaseRef;

    /**
     * Initiates the Listview for displaying comments and sets OnClickListener on it
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.action_signin).setVisible(false);
        navigationView.setNavigationItemSelectedListener(this);

        /**
         * Create Firebase references
         */
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        try {
            mUserId = mFirebaseRef.getAuth().getProviderData();
            System.out.println("User ProviderData : " + mUserId);
        } catch (Exception e) {
            //loadLoginView();
        }

        listView = (ListView) findViewById(android.R.id.list);
        this.retrieveData();

    }

    //Retrieve data
    private void retrieveData() {
        itemsUrl = Constants.FIREBASE_URL + "/comments/";
        System.out.println("URL value : " + itemsUrl);
        // Use Firebase to populate the list.
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


    private void getUpdates(DataSnapshot ds) {

        Comments comments = ds.getValue(Comments.class);
        commentList.add(comments);

        if (commentList.size() > 0) {
            ArrayAdapter adapter = new customArrayAdapter(CommentsActivity.this, commentList);
            System.out.println("commentList : " + commentList);
            listView.setAdapter(adapter);
        } else {
            Toast.makeText(CommentsActivity.this, "No data", Toast.LENGTH_SHORT);
        }
    }

    public void openEditorForNewComment(View view) {

        String userName = getIntent().getStringExtra("userName");
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra("userName", userName);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
            Intent intent_home = new Intent(CommentsActivity.this, HomePageActivity.class);
            startActivity(intent_home);
        } else if (id == R.id.action_userProfile) {
            Intent intent_profile = new Intent(CommentsActivity.this, UserProfileActivity.class);
            startActivity(intent_profile);
        } else if (id == R.id.customersupport) {
            Intent intent_csupport = new Intent(CommentsActivity.this, CustomerSupportActivity.class);
            startActivity(intent_csupport);

        } else if (id == R.id.tutorial) {
            Intent intent_tutorial = new Intent(CommentsActivity.this, TutorialActivity.class);
            startActivity(intent_tutorial);

        } else if (id == R.id.action_logout) {
            mFirebaseRef.unauth();
            Intent i = new Intent(CommentsActivity.this, HomePageActivity.class);
            startActivity(i);

        } else if (id == R.id.appwebsite) {
            Intent i = new Intent(CommentsActivity.this, AppWebsite.class);
            startActivity(i);
        } else if (id == R.id.exit) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
