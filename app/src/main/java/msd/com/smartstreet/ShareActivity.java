package msd.com.smartstreet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import msd.com.utils.Constants;
import msd.com.utils.ShareFirebase;

public class ShareActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int BROWSE_GALLERY_REQUEST = 2;
    int REQUEST_CAMERA = 0, SELECT_PHOTO_FILE = 1, SELECT_VIDEO_FILE = 2, SELECT_AUDIO_FILE = 3;
    ShareDialog shareDialog;
    private Button sendEmail, viewGallery;
    private CallbackManager callbackManager;
    private Button shareFB, shareAudio;
    private Firebase mRef;
    private String mUserId;
    private String itemsUrl;
    /* References to the Firebase */
    private Firebase mFirebaseRef;
    private Map<String, Object> mUserData;

    /**
     * This method initializes the View Gallery, Email, Facebook
     * and Share Audio Buttons and Sets OnClickListener on them
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.activity_share);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.action_signin).setVisible(false);
        navigationView.setNavigationItemSelectedListener(this);


        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);


        viewGallery = (Button) findViewById(R.id.viewGallery);
        viewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  browseGallery();
                Intent intent = new Intent(ShareActivity.this, SharedRecords.class);
                startActivity(intent);

            }
        });

        shareDialog = new ShareDialog(this);

        sendEmail = (Button) findViewById(R.id.email);
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("message/rfc822");
                    startActivity(emailIntent);
                    saveOnline("Email");
                } catch (ActivityNotFoundException anfe) {
                    Toast.makeText(ShareActivity.this, "No email client found", Toast.LENGTH_LONG).show();
                }
            }
        });

        shareFB = (Button) findViewById(R.id.fb);
        shareFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        shareAudio = (Button) findViewById(R.id.audio);
        shareAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String path = Environment.getExternalStorageDirectory()
                        .getPath();
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("audio/*");
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + path));
                startActivity(Intent.createChooser(share, "Share Sound File"));
                saveOnline("Whatsapp");
            }
        });
    }

    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Initializes the Facebook sdk
     */
    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    //Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    /**
     * Browses the Gallery for videos and photos
     */
    private void browseGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, BROWSE_GALLERY_REQUEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    /**
     * This is the pop up when Facebook button is clicked gives four options
     * Take Photo
     * Choose Photo from library
     * Choose Video from library
     * Cancel
     */
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose photo from Library",
                "Choose video from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ShareActivity.this);
        builder.setTitle("Select a Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose photo from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_PHOTO_FILE);
                } else if (items[item].equals("Choose video from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    //intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_VIDEO_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
        saveOnline("facebook");
    }


    /**
     * Executed after the execution of the activity onCreate
     *
     * @param requestCode
     * @param responseCode
     * @param data
     */
    @Override
    protected void onActivityResult(final int requestCode, final int responseCode, final Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
        if (responseCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PHOTO_FILE)
                onSelectPhotoFromGalleryResult(data);
            else if (requestCode == SELECT_AUDIO_FILE)
                onSelectAudioFromGalleryResult(data);
            else if (requestCode == SELECT_VIDEO_FILE)
                onSelectVideoFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }

    }

    private void onSelectAudioFromGalleryResult(Intent data) {
        Uri selectedAudioUri = data.getData();
        ShareDialog(selectedAudioUri);
    }

    private void onSelectVideoFromGalleryResult(Intent data) {
        Uri selectedVideoUri = data.getData();
        ShareDialog(selectedVideoUri);

    }

    /****
     * this method used for select image From Gallery
     *****/

    private void onSelectPhotoFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap thumbnail;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        thumbnail = BitmapFactory.decodeFile(selectedImagePath, options);

        ShareDialog(thumbnail);
    }

    /**
     * this method used for take a photo
     **/

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {

            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ShareDialog(thumbnail);
    }

    /**
     * Share dialog for the Photo
     *
     * @param imagePath
     */
    public void ShareDialog(Bitmap imagePath) {
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(imagePath)
                .setCaption("V")
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        shareDialog.show(content);
    }

    /**
     * Share dialog for the video
     *
     * @param videoUrl
     */
    public void ShareDialog(Uri videoUrl) {
        ShareVideo video = new ShareVideo.Builder()
                .setLocalUrl(videoUrl)
                .build();
        ShareVideoContent content = new ShareVideoContent.Builder()
                .setVideo(video)
                .build();
        shareDialog.show(content);

    }

    public void saveOnline(String desc) {

        ShareFirebase sr = new ShareFirebase();
        String userName = null;
        String[] parts = null;
        try {
            mUserData = mFirebaseRef.getAuth().getProviderData();
            System.out.println("mUserData keyset" + mUserData.keySet());
            userName = (String) mUserData.get("email");
            parts = userName.split("@");

        } catch (Exception e) {
            //loadLoginView();
        }
        sr.setName(parts[0]);
        sr.setDesc(desc);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        sr.setShared(sdf.format(new Date()));
        mFirebaseRef.child("ShareDetails").push().setValue(sr);
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
            Intent intent_home = new Intent(ShareActivity.this, HomePageActivity.class);
            startActivity(intent_home);
        } else if (id == R.id.action_userProfile) {
            Intent intent_profile = new Intent(ShareActivity.this, UserProfileActivity.class);
            startActivity(intent_profile);

        } else if (id == R.id.customersupport) {
            Intent intent_csupport = new Intent(ShareActivity.this, CustomerSupportActivity.class);
            startActivity(intent_csupport);

        } else if (id == R.id.tutorial) {
            Intent intent_tutorial = new Intent(ShareActivity.this, TutorialActivity.class);
            startActivity(intent_tutorial);

        } else if (id == R.id.action_logout) {
            mFirebaseRef.unauth();
            Intent i = new Intent(ShareActivity.this, HomePageActivity.class);
            startActivity(i);

        } else if (id == R.id.appwebsite) {
            Intent i = new Intent(ShareActivity.this, AppWebsite.class);
            startActivity(i);
        } else if (id == R.id.exit) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
