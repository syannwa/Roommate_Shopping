package edu.uga.cs.roommateshopping;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RoommateViewActivity extends AppCompatActivity {
    private static final String TAG = "RoommateViewActivity";

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private String userID;
    private ListView mListView;
    private TextView title;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roommate_view);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        user = mAuth.getCurrentUser();
        userID = user.getUid();
        mListView = findViewById(R.id.listview);
        title = findViewById(R.id.roomNumTitle);


        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                showData(snapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


    }

    private void showData(DataSnapshot dataSnapshot)
    {
        ArrayList<String> array = new ArrayList<>();
        DataSnapshot rooms = null;
        String realCurrentRoom = "0";

        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            User uInfo = new User();
            Log.d(TAG, "getRoommateData from database.");
            Log.d(TAG, "Current datasnap: " + ds);
            String currentRoom = ds.child(userID).child("room").getValue(String.class);
            if (currentRoom != null) {
                realCurrentRoom = currentRoom;
                Log.d(TAG, "currentRoom: " + realCurrentRoom);
                title.setText("Room Number: " + realCurrentRoom);

            } else
                rooms = ds;

        }

        for(DataSnapshot ds : rooms.child(realCurrentRoom).child("roommates").getChildren()){
            array.add(ds.getKey());
        }
        try {
            ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.textview, array);
            mListView.setAdapter(adapter);
        }
        catch(NullPointerException e){
            array.add("There are no roommates.");
            ArrayAdapter adapt = new ArrayAdapter<String>(this, R.layout.list_item, R.id.textview, array);
            mListView.setAdapter(adapt);
        }
    }


}