package msd.com.smartstreet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import msd.com.utils.PubNubHelper;

public class ChangeMusic extends AppCompatActivity {

    Button mPlay,mPause,mResume,mStop;
    PubNubHelper helper = new PubNubHelper();
    ListView listView;

    /**
     * Lists the music and adds listeners to the buttons
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_music);
        mPlay = (Button) findViewById(R.id.play);
        mPause = (Button) findViewById(R.id.pause);
        mResume = (Button) findViewById(R.id.resume);
        mStop = (Button) findViewById(R.id.stop);

        String[] songList = new String[] { "Song_001",
                "Song_002",
                "Song_003",
                "Song_004",
                "Song_005",
                "Song_006",
                "Song_007",
                "Song_008"
        };

        // Get ListView object from xml
        listView = (ListView) findViewById(android.R.id.list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, songList);
        // Assign adapter to ListView
        listView.setAdapter(adapter);
        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                play(view);

            }
        });

        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(v);
            }
        });

        mPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause(v);
            }
        });

        mResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resume(v);
            }
        });

        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop(v);
            }
        });
    }

    public void play(View view){
        helper.publishPlayMusic();
    }

    public void pause(View view){
        helper.publishPauseMusic();
    }

    public void stop(View view){
        helper.publishStopMusic();
    }

    public void resume(View view){
        helper.publishResumeMusic();
    }
}
