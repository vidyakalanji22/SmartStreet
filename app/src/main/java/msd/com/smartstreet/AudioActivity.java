package msd.com.smartstreet;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AudioActivity extends AppCompatActivity {

    private MediaRecorder mRecorder;
    private MediaPlayer myPlayer;
    private File outputFile = null;
    private Button startBtn;
    private Button stopBtn;
    private Button playBtn;
    private Button pauseBtn;
    private TextView text;

    /**
     * Oncreate method initializes the start, stop, play, pause buttons and adds
     * onClickListeners on them
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        text = (TextView) findViewById(R.id.text1);
        // store it to sd card
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String audioFileName = "MP3_" + timeStamp + "_";
        File storageDir = Environment.getExternalStorageDirectory();

        try {
            outputFile = File.createTempFile(audioFileName, ".mp3", storageDir);
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            mRecorder.setOutputFile(outputFile.toString());
            startBtn = (Button) findViewById(R.id.start);
            startBtn.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {
                    start(v);

                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        stopBtn = (Button) findViewById(R.id.stop);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                stop(v);

            }

        });

        playBtn = (Button) findViewById(R.id.play);
        playBtn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                play(v);

            }

        });

        pauseBtn = (Button) findViewById(R.id.stopPlay);
        pauseBtn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                stopPlay(v);

            }
        });

    }

    /**
     * This method starts the recording of the audio
     * @param view
     */
    public void start(View view) {
        try {
            mRecorder.prepare();
            mRecorder.start();

        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
        } catch (IOException e) {
            // prepare() fails
            e.printStackTrace();
        }

        text.setText("Recording Status: Recording");
        startBtn.setEnabled(false);
        stopBtn.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Start recording...",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * This method stops the audio recording
     * @param view
     */
    public void stop(View view) {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            stopBtn.setEnabled(false);
            playBtn.setEnabled(true);
            text.setText("Recording Status: Stop recording");
            Toast.makeText(getApplicationContext(), "Stop recording...",
                    Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            //  it is called before start()
            e.printStackTrace();
        } catch (RuntimeException e) {
            // no valid audio/video data has been received
            e.printStackTrace();
        }
    }

    /**
     * This method plays the recorded audio
     * @param view
     */
    public void play(View view) {
        try {
            myPlayer = new MediaPlayer();
            myPlayer.setDataSource(outputFile.toString());
            myPlayer.prepare();
            myPlayer.start();
            playBtn.setEnabled(false);
            pauseBtn.setEnabled(true);
            text.setText("Recording Status: Playing");
            Toast.makeText(getApplicationContext(), "Start play the recording...",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method stops the audio playing
     * @param view
     */
    public void stopPlay(View view) {
        try {
            if (myPlayer != null) {
                myPlayer.stop();
                myPlayer.release();
                myPlayer = null;
                playBtn.setEnabled(true);
                pauseBtn.setEnabled(false);
                text.setText("Recording Status: Stop playing");
                Toast.makeText(getApplicationContext(), "Stop playing the recording...",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
