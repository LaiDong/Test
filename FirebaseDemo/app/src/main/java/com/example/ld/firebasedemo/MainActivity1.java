package com.example.ld.firebasedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ld on 24/01/2018.
 */

public class MainActivity1 extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView txtDetails;
    private EditText inputName, inputEmail;
    private Button btnSave, btnRemote;
    private DatabaseReference mFirebaseDatabase, myRef;
    private FirebaseDatabase mFirebaseInstance;
    String value = null;
    double latitude, longtitude;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        // Displaying toolbar icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        txtDetails = (TextView) findViewById(R.id.txt_user);
        inputName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnRemote = findViewById(R.id.btn_remote);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        myRef = mFirebaseInstance.getReference("message");

        // app_title change listener
        mFirebaseInstance.getReference("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");

                value = dataSnapshot.getValue(String.class);

                // update toolbar title
                //getSupportActionBar().setTitle(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

        //remote
        btnRemote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value == null || value.equals("0")){
                    myRef.setValue("1");

                    btnRemote.setText("Stop");
                }else{
                    myRef.setValue("0");
                    btnRemote.setText("Start");
                }
            }
        });

        GPSTracker gps = new GPSTracker(this);
        if(gps.canGetLocation()){
             latitude = gps.getLatitude();
             longtitude = gps.getLongitude();
            Log.d("donglv", "latitude: " + latitude + " - longtitude: " + longtitude);
        }else{
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }

        // Save / update the user
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check for already existed userId
                if (TextUtils.isEmpty(userId)) {
                    createUser(latitude, longtitude);
                } else {
                    updateUser(latitude, longtitude);
                }
            }
        });

        toggleButton();

        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
    }

    // Changing button text
    private void toggleButton() {
        if (TextUtils.isEmpty(userId)) {
            btnSave.setText("Save");
        } else {
            btnSave.setText("Update");
        }
    }

    /**
     * Creating new user node under 'users'
     */
    private void createUser(double latitude, double longtitude) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        User user = new User(latitude, longtitude);

        mFirebaseDatabase.child(userId).setValue(user);

        addUserChangeListener();
    }

    /**
     * User data change listener
     */
    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed! " + user.latitude + ", " + user.longtitude);

                // Display newly updated name and email
                txtDetails.setText(user.latitude + ", " + user.longtitude);

                // clear edit text
                inputEmail.setText("");
                inputName.setText("");

                toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    private void updateUser(Double latitude, Double longtitude) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(String.valueOf(latitude)))
            mFirebaseDatabase.child(userId).child("latitude").setValue(latitude);

        if (!TextUtils.isEmpty(String.valueOf(longtitude)))
            mFirebaseDatabase.child(userId).child("longtitude").setValue(longtitude);
    }
}

