package msd.com.smartstreet;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    private static final int PHOTO_TAKEN = 0;
    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int BROWSE_GALLERY_REQUEST = 2;
    Uri videoUri;
    private File imageFile, mediaFile;
    private Button galleryButton;

    /**
     * This method calls the methods for photo, audio, video and brows gallery
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (hasCamera()) {
            addSnapButtonListener();
            dispatchTakeVideoIntent();
            browswGallery();
            recordAudio();
        } else {
            Toast.makeText(this, "No camera found ", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This method intializes the audio button and records the audio
     */
    private void recordAudio() {
        Button audioButton = (Button) findViewById(R.id.audio);
        audioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AudioActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * This method browses the gallery
     */
    private void browswGallery() {
        galleryButton = (Button) findViewById(R.id.gallery);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, BROWSE_GALLERY_REQUEST);

            }
        });

    }

    /**
     * This method checks if the device has camera or not
     * @return true or false depending on the availability of the camera
     */
    private boolean hasCamera() {
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FRONT)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Listener method for the Photo button
     */
    private void addSnapButtonListener() {
        Button snapButton = (Button) findViewById(R.id.snap);
        snapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);

                try {
                    imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                    i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(i, PHOTO_TAKEN);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    /**
     * Listener method for the Video button
     */
    private void dispatchTakeVideoIntent() {
        Button videoButton = (Button) findViewById(R.id.video);
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String mediaFileName = "MP4_" + timeStamp + "_";
                File videoStorageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);

                try {
                    mediaFile = File.createTempFile(mediaFileName, ".mp4", videoStorageDir);
                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    videoUri = Uri.fromFile(mediaFile);
                    takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * This method is called upon after some activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView imageView;
        VideoView videoView;
        imageView = (ImageView) findViewById(R.id.photoView);
        videoView = (VideoView) findViewById(R.id.videoView);
        if (requestCode == PHOTO_TAKEN) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    try{
                    Bitmap photo = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    if (photo != null) {
                        imageView.setVisibility(View.VISIBLE);
                        videoView.setVisibility(View.INVISIBLE);
                        imageView.setImageBitmap(photo);
                    }}catch(Exception e){
                        Intent intent = new Intent(CameraActivity.this, CameraActivity.class);
                        startActivity(intent);
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
                default:
                    break;

            }
        } else if (requestCode == REQUEST_VIDEO_CAPTURE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (videoUri != null) {
                        imageView.setVisibility(View.INVISIBLE);
                        videoView.setVisibility(View.VISIBLE);
                        videoView.setVideoURI(videoUri);
                        videoView.start();
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(this, "Video recording cancelled.",
                            Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(this, "Failed to record video",
                            Toast.LENGTH_LONG).show();
                    break;
            }

        }

    }


}
