package msd.com.smartstreet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.Uri;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class TutorialActivity extends AppCompatActivity {

    /**
     * Provides video demo for first time user.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        VideoView myVideoView = (VideoView) findViewById(R.id.videoViewTutorial);
        try{
            String vidpath = "android.resource://" + getPackageName() + "/" + R.raw.demo;
            myVideoView.setVideoURI(Uri.parse(vidpath));
            myVideoView.setMediaController(new MediaController(this));
            myVideoView.requestFocus();
            myVideoView.start();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
        }

    }

}
