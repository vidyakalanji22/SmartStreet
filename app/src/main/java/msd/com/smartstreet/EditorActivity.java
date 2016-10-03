package msd.com.smartstreet;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import msd.com.utils.Comments;
import msd.com.utils.Constants;

public class EditorActivity extends AppCompatActivity {



    private String action;
    private EditText editor;
    private Button saveButton;
    private float ratingStatus;
    private String mUserId;
    private String itemsUrl;
    private Firebase mRef;
    Firebase mCommentsRef;
    private Map<String, Object> mUserData;

    /**
     * Initializes Rating bar, EditText filed and Save button and OnClickListener to them
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRef = new Firebase(Constants.FIREBASE_URL);
        mCommentsRef = mRef.child("comments");

        RatingBar rb = (RatingBar) findViewById(R.id.ratingBarEditor);
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
               ratingStatus=rating;
            }
        });

        editor = (EditText) findViewById(R.id.editText);
        action = Intent.ACTION_INSERT;
        setTitle("Add Comment");


        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // insertComment(newComment);
                finishComment();
                Intent i = new Intent(EditorActivity.this,CommentsActivity.class);
                startActivity(i);
            }
        });
    }

    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void finishComment(){
        String newComment = editor.getText().toString().trim();
        if(newComment.length() == 0){
            setResult(RESULT_CANCELED);
        }else{
            insertComment(newComment);
        }
        finish();
    }

    /**
     * Insert the comment to the comments table
     * @param commentText
     */
    private void insertComment(String commentText) {

        String userName = null;
        String[] parts = null;
        try {
            mUserData = mRef.getAuth().getProviderData();
            System.out.println("mUserData keyset" + mUserData.keySet());
            userName = (String) mUserData.get("email");
            parts = userName.split("@");

        } catch (Exception e) {
            //loadLoginView();
        }
        Comments comments = new Comments();
        comments.setCommentText(commentText);
        comments.setRating(ratingStatus);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        comments.setCreated(sdf.format(new Date()));
        comments.setUser(parts[0]);


        mCommentsRef.push()
                .setValue(comments);

    }





}
