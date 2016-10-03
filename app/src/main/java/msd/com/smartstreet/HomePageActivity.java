package msd.com.smartstreet;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import msd.com.utils.Constants;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Declaring ImageButtons for the About and Nearby buttons
    Button maboutButton = null;
    Button mnearbyButton = null;
    Button cameraButton = null;
    Button shareButton = null;
    Button commnetButton = null;
    Button interacttButton = null;
    private Firebase mRef;

    private String mUserId;
    NavigationView navigationView;

    /**
     * Called on creation of the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Referring to Home Page Activity
        setContentView(R.layout.activity_home_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRef = new Firebase(Constants.FIREBASE_URL);
        try {
            mUserId = mRef.getAuth().getUid();
        } catch (Exception e) {
          e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.home).setVisible(false);
        if(mUserId == null) {
            navigationView.getMenu().findItem(R.id.action_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.action_userProfile).setVisible(false);
        }else{
            navigationView.getMenu().findItem(R.id.action_signin).setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(this);

        final Intent intent = getIntent();
        final String userName = intent.getStringExtra("userName");


        maboutButton = (Button) findViewById(R.id.aboutButton);
        //Setting Listner for About button
        maboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), BarcodeActivity.class);
                startActivity(i);
            }
        });

        mnearbyButton = (Button) findViewById(R.id.nearbyButton);
        //Setting Listner for Nearby button
        mnearbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.nearbyButton) {
                    Intent i = new Intent(v.getContext(), MapsActivity.class);
                    startActivity(i);
                }
            }
        });

        interacttButton = (Button) findViewById(R.id.interact);
        interacttButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mUserId == null) {
                    Intent i = new Intent(v.getContext(), LoginActivity.class);
                    i.putExtra("val","fromInteract");
                    startActivity(i);
                } else {
                    Intent i = new Intent(v.getContext(), InteractActivity.class);
                    i.putExtra("userName",userName);
                    startActivity(i);
                }
            }
        });

        cameraButton = (Button) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.cameraButton){
                    Intent i = new Intent(v.getContext(), CameraActivity.class);
                    startActivity(i);
                }
            }
        });

        shareButton = (Button) findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUserId == null) {
                    Intent i = new Intent(v.getContext(), LoginActivity.class);
                    i.putExtra("val","fromShare");
                    startActivity(i);
                }else{

                    Intent i = new Intent(v.getContext(), ShareActivity.class);
                    i.putExtra("userName",userName);
                    startActivity(i);
                }

            }
        });

        commnetButton = (Button) findViewById(R.id.commentButton);
        commnetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUserId == null) {
                    Intent i = new Intent(v.getContext(), LoginActivity.class);
                    i.putExtra("val","fromComment");
                    startActivity(i);
                } else {
                    Intent i = new Intent(v.getContext(), CommentsActivity.class);
                    i.putExtra("userName",userName);
                    startActivity(i);
                }

            }
        });


    }

    private void loadLoginView() {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * adding menu options
     * @param menu
     * @return true or false depending on the menu items
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
        return true;
    }

    /**
     *  To perform some action on the menu option selected
     * @param menu
     * @return true or false depending on the menu option selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, signout long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = menu.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signin) {
            Intent i = new Intent(HomePageActivity.this,LoginActivity.class);
            i.putExtra("val","fromMenu");
            startActivity(i);
        }else if (id == R.id.action_logout) {
            mRef.unauth();
            loadLoginView();
        }else if (id == R.id.action_userProfile) {
            Intent i = new Intent(HomePageActivity.this,UserProfileActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

    /**
     * executes on navigation item selected
     * @param item
     * @return
     */

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

     if (id == R.id.action_signin) {
            Intent i = new Intent(HomePageActivity.this,LoginActivity.class);
            i.putExtra("val", "fromMenu");
            startActivity(i);
        }else if (id == R.id.action_userProfile) {
            Intent intent_profile = new Intent(HomePageActivity.this,UserProfileActivity.class);
            startActivity(intent_profile);

        }else if (id == R.id.customersupport) {
            Intent intent_csupport = new Intent(HomePageActivity.this,CustomerSupportActivity.class);
            startActivity(intent_csupport);

        }else if (id == R.id.tutorial) {
            Intent intent_tutorial = new Intent(HomePageActivity.this,TutorialActivity.class);
            startActivity(intent_tutorial);

        }else if (id == R.id.action_logout) {
            mRef.unauth();
            loadLoginView();

        }else if (id == R.id.appwebsite) {
            Intent i = new Intent(HomePageActivity.this,AppWebsite.class);
            startActivity(i);
        }else if (id == R.id.exit) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

