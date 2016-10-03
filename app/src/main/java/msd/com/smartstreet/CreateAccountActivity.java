package msd.com.smartstreet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import msd.com.utils.Constants;

public class CreateAccountActivity extends AppCompatActivity {


    private static final String LOG_TAG = CreateAccountActivity.class.getSimpleName();
    private ProgressDialog mAuthProgressDialog;
    private EditText mEditTextUsernameCreate, mEditTextEmailCreate, mEditTextPasswordCreate, mEditTextPhone;
    private String mUserName, mUserEmail, mPassword, mPhone;
    private Firebase mFirebaseRef;
    private Button mSignupButton;
    private TextView mSigninLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * Create Firebase references
         */
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        mSignupButton = (Button) findViewById(R.id.signup);
        mEditTextUsernameCreate = (EditText) findViewById(R.id.userName);
        mEditTextEmailCreate = (EditText) findViewById(R.id.email);
        mEditTextPasswordCreate = (EditText) findViewById(R.id.password);
        mEditTextPhone = (EditText) findViewById(R.id.phone);
        mSigninLink = (TextView) findViewById(R.id.signinhere);


        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserName = mEditTextUsernameCreate.getText().toString();
                mUserEmail = mEditTextEmailCreate.getText().toString();
                mPassword = mEditTextPasswordCreate.getText().toString();
                mPhone = mEditTextPhone.getText().toString();
                mUserEmail = mUserEmail.trim();
                mPassword = mPassword.trim();

                if (mPassword.isEmpty() || mUserEmail.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccountActivity.this);
                    builder.setMessage(R.string.signup_error_message)
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    final String userName = mUserName;
                    final String emailAddress = mUserEmail;
                    final String password = mPassword;
                    final String phone = mPhone;

                    // signup
                    mFirebaseRef.createUser(mUserEmail, mPassword, new Firebase.ResultHandler() {
                        @Override
                        public void onSuccess() {


                            AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccountActivity.this);
                            builder.setMessage(R.string.signup_success)
                                    .setPositiveButton(R.string.login_button_label, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                                            intent.putExtra("userName", userName);
                                            intent.putExtra("emailAddress", emailAddress);
                                            intent.putExtra("password", password);
                                            intent.putExtra("phone", phone);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccountActivity.this);
                            builder.setMessage(firebaseError.getMessage())
                                    .setTitle(R.string.signup_error_title)
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }

            }
        });


        mSigninLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * Override onCreateOptionsMenu to inflate nothing
     *
     * @param menu The menu with which nothing will happen
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


}
