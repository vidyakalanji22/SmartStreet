package msd.com.smartstreet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

// Importing the third part Zxing libraries
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring the variables for the Scan button, Home button and the Content text
    private ImageButton scanBtn;
   // private  ImageButton homeButton;
    private TextView contentTxt;

    /**
     * Called on creation of the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //referring to the activity_barcode.xml file
        setContentView(R.layout.activity_barcode);
        // Taking the values for Scan button, Home button and content text from xml activity
        scanBtn = (ImageButton)findViewById(R.id.scanButton);
        contentTxt = (TextView)findViewById(R.id.content);
       // homeButton = (ImageButton) findViewById(R.id.homeButton);

        scanBtn.setOnClickListener(this);
      //  homeButton.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    /**
     * Onclick method for buttons
     * @param v
     */
    @Override
    public void onClick(View v) {
        //respond to clicks
        if(v.getId()==R.id.scanButton){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
        /*if(v.getId()==R.id.homeButton) {
            Intent i = new Intent(v.getContext(), HomePageActivity.class);
            startActivity(i);
        }*/
    }

    /**
     * Activity for the obtained result
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
    //we have a scanning result
            String scanContent = scanningResult.getContents();
            contentTxt.setText("Found URL: " + scanContent);

        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
