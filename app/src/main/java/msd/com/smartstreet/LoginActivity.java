package msd.com.smartstreet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

import msd.com.utils.Constants;

public class LoginActivity extends AppCompatActivity {

    //Initializing DatabaseHelper class to use database
    private EditText mEmail;
    private EditText mPassword;
    private Button mSigninButton;
    TextView registerLink;


    /* References to the Firebase */
    private Firebase mFirebaseRef;

    /**
     * Initializes email, password and register link and
     * adds OnClickListener to them
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /**
         * Create Firebase references
         */
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mSigninButton = (Button) findViewById(R.id.signin);
        registerLink = (TextView) findViewById(R.id.registerHere);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
        mSigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = getIntent();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                email = email.trim();
                password = password.trim();

                if (email.isEmpty() || password.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.login_error_message)
                            .setTitle(R.string.login_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {

                    final String emailAddress = email;
                    final String passwd = password;
                    final String userName = intent.getStringExtra("userName");
                    final String phone = intent.getStringExtra("phone");



                    //Login with an email/password combination
                    final String finalEmail = email;
                    mFirebaseRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            // Authenticated successfully with payload authData
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("email", emailAddress);
                            map.put("password", passwd);
                            if(userName != null || phone != null) {

                                map.put("user", userName);
                                map.put("phone", phone);
                                mFirebaseRef.child("users").child(authData.getUid()).setValue(map);
                            }

                            String val = intent.getStringExtra("val");
                            if(val == null || val.equals("fromMenu")){
                                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                intent.putExtra("userName", finalEmail);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else if(val.equals("fromInteract")){
                                Intent intent = new Intent(LoginActivity.this, InteractActivity.class);
                                intent.putExtra("userName",finalEmail);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else if(val.equals("fromShare")){

                                Intent intent = new Intent(LoginActivity.this, ShareActivity.class);
                                intent.putExtra("userName",finalEmail);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else if(val.equals("fromComment")){
                                Intent intent = new Intent(LoginActivity.this, CommentsActivity.class);
                                intent.putExtra("userName",finalEmail);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }


                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            // Authenticated failed with error firebaseError
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage(firebaseError.getMessage())
                                    .setTitle(R.string.login_error_title)
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }
            }
        });

    }


}
