package msd.com.smartstreet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ActivitySplashScreen extends AppCompatActivity {

    /**
     * Executes on creation of a page.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_splash_screen);

        /**
         * set the spalsh screen for 2 seconds
         */
        Thread startTimer = new Thread() {
            public void run(){
                try {
                    sleep(2000);
                    Intent intent = new Intent(ActivitySplashScreen.this,HomePageActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        startTimer.start();
    }
}
