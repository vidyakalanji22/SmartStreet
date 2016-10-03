package msd.com.smartstreet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import msd.com.utils.Constants;
import msd.com.utils.InteractRecords;
import msd.com.utils.PubNubHelper;
import msd.com.utils.ShareFirebase;

public class InteractActivity extends AppCompatActivity {


    public static final int COLOR_RED   = 0;
    public static final int COLOR_BLUE  = 1;
    public static final int COLOR_GREEN = 2;

    SeekBar mRedSeek, mGreenSeek, mBlueSeek;
    LinearLayout mRGBValueHolder;
    private PubNubHelper helper;
    Button musicChange, interactRecords;

    private long lastUpdate = System.currentTimeMillis();
    private boolean pHueOn = false;

    Switch light = null;
    private Map<String, Object> mUserData;

    /* References to the Firebase */
    private Firebase mFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        helper = new PubNubHelper();

        // Get Seek Bars
        mRedSeek   = (SeekBar) findViewById(R.id.seekBarRed);
        mGreenSeek = (SeekBar) findViewById(R.id.seekBarGreen);
        mBlueSeek  = (SeekBar) findViewById(R.id.seekBarBlue);
        mRGBValueHolder = (LinearLayout) findViewById(R.id.buttonLayout);
        musicChange = (Button) findViewById(R.id.musicButton);
        interactRecords = (Button) findViewById(R.id.interactRecords);



        light = (Switch) findViewById(R.id.light);

        light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // do something, the isChecked will be
                // true if the switch is in the On position
                if (isChecked) {
                    Toast.makeText(InteractActivity.this, "On", Toast.LENGTH_LONG);
                    lightOn(buttonView);
                } else {
                    Toast.makeText(InteractActivity.this, "Off", Toast.LENGTH_LONG);
                    lightOff(buttonView);
                }
            }
        });


        // Setup Seek Bars
        setupSeekBar(mRedSeek, COLOR_RED);
        setupSeekBar(mGreenSeek, COLOR_GREEN);
        setupSeekBar(mBlueSeek, COLOR_BLUE);

        musicChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playMusic(v);

            }
        });


        interactRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InteractActivity.this,InteractedData.class);
                startActivity(intent);

            }
        });

}

    public void setupSeekBar(SeekBar seekBar, final int colorID){
        seekBar.setMax(255);        // Seek bar values goes 0-255
        seekBar.setProgress(255);   // Set the knob to 255 to start
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                helper.publish(mRedSeek.getProgress(), mGreenSeek.getProgress(), mBlueSeek.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                saveOnline("Color");
                helper.publish(mRedSeek.getProgress(), mGreenSeek.getProgress(), mBlueSeek.getProgress());
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                TextView colorValueText;

                switch (colorID) {  // Get the TextView identified by the colorID
                    case COLOR_RED:
                        colorValueText = (TextView) findViewById(R.id.red_value);
                        break;
                    case COLOR_GREEN:
                        colorValueText = (TextView) findViewById(R.id.green_value);
                        break;
                    case COLOR_BLUE:
                        colorValueText = (TextView) findViewById(R.id.blue_value);
                        break;
                    default:
                        Log.e("SetupSeek", "Invalid color.");
                        return;
                }
                colorValueText.setText(String.valueOf(progress));  // Update the 0-255 text
                int red = mRedSeek.getProgress();     // Get Red value 0-255
                int green = mGreenSeek.getProgress();   // Get Grn value 0-255
                int blue = mBlueSeek.getProgress();    // Get Blu value 0-255
                updateRGBViewHolderColor(red, green, blue); // Change the background of the viewholder

                long now = System.currentTimeMillis();    // Only allow 1 pub every 100 milliseconds
                if (now - lastUpdate > 100 && fromUser) { // Threshold and only send when user sliding
                    lastUpdate = now;
                    helper.publish(red, green, blue);          // Stream RGB values to the Pi
                }
            }
        });

    }

    private void updateRGBViewHolderColor(int red, int green, int blue){
        int alpha = 255; // No opacity
        int color = (alpha << 24) | (red << 16) | (green << 8) | blue;
        mRGBValueHolder.setBackgroundColor(color);
    }

    public void lightOff(View view){
        helper.publishStop();
        setRGBSeeks(0, 0, 0);
    }

    public void lightOn(View view) {
        helper.publishStart();
        setRGBSeeks(255, 255, 255);
    }

    public void playMusic(View view) {
        saveOnline("music");
        Intent intent = new Intent(this, ChangeMusic.class);
        startActivity(intent);
    }

    public void setRGBSeeks(int red, int green, int blue){
        mRedSeek.setProgress(red);
        mGreenSeek.setProgress(green);
        mBlueSeek.setProgress(blue);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        this.pHueOn = false;
        return super.dispatchTouchEvent(ev);
    }

    public void pHueOn(View view){
        this.pHueOn = true;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (int i = 0; i < 720; i+=3) {
                        if (!pHueOn) return;
                        int r = posSinWave(50, i, 0.5);
                        int g = posSinWave(50, i, 1);
                        int b = posSinWave(50, i, 2);
                        helper.publish(r, g, b);
                        setRGBSeeks(r, g, b);
                        try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                }
            }
        });
        t.start();

    }
    public int posSinWave(int amplitude, int angle, double frequency) {
        return (int)((amplitude + (amplitude * Math.sin(Math.toRadians(angle) * frequency)))/100.0*255);
    }

    public void saveOnline(String interactedMedia) {

        InteractRecords interactRecords = new InteractRecords();
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
        interactRecords.setInteractedUser(parts[0]);
        interactRecords.setInteractedTree("tree2");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        interactRecords.setInteractedDate(sdf.format(new Date()));
        interactRecords.setInteractedMedia(interactedMedia);
        mFirebaseRef.child("InteractionRecords").push().setValue(interactRecords);
    }
}
