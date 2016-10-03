package msd.com.smartstreet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class CustomerSupportActivity extends AppCompatActivity {

    public static Button mail_button;

    /**
     * adds button listeners to mail_button
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_support);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        onClickMailButtonListener();
    }

    public void onClickMailButtonListener() {
        mail_button = (Button) findViewById(R.id.button2);
        mail_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CustomerSupportActivity.this, SupportEmail.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
