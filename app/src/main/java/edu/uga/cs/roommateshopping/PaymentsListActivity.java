package edu.uga.cs.roommateshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PaymentsListActivity extends AppCompatActivity {

    public static final String DEBUG_TAG = "Payments_DEBUG";

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private String userID;
    private ListView listView;
    private TwoColumn_Adapter adapter;

    String realCurrentRoom;
    String roomNumber;
    String realRoom = "0";
    String userName;

    private ArrayList<Payment> paymentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_list);

        listView = findViewById(R.id.list_view);
        paymentsList = new ArrayList<Payment>();
        adapter =  new TwoColumn_Adapter(PaymentsListActivity.this,R.layout.activity_two_column__adapter, paymentsList);
        listView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        user = mAuth.getCurrentUser();
        userID = user.getUid();

        myRef.addListenerForSingleValueEvent( new ValueEventListener() {

            @Override
            public void onDataChange( DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, knowing that this is a list,
                // we need to iterate over the elements and place them on a List.

                DataSnapshot rooms = null;
                realRoom = "0";

                // Get room num and user's name
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String currentRoom = ds.child(userID).child("room").getValue(String.class);
                    String currentName = ds.child(userID).child("firstName").getValue(String.class);
                    if (currentRoom != null) {
                        realRoom = currentRoom;
                        userName = currentName;
                        Log.d(DEBUG_TAG, "Room number: " + realRoom);
                        Log.d(DEBUG_TAG, "Name of user: " + userName);
                    } else
                        rooms = ds;
                }

                for(DataSnapshot ds : snapshot.child("rooms").child(realRoom).child("roommates").child(userName).child("payments").getChildren()){
                    Log.d(DEBUG_TAG, "DS Key: " + ds.getKey());

                    // Get payments information
                    String name = ds.getKey();
                    Double payment = ds.child("moneyOwed").getValue(Double.class);
                    Log.d(DEBUG_TAG, "Name: " + name + ", Payment: " + payment);

                    // Add item to list
                    Payment owed = new Payment(name, payment);
                    paymentsList.add(owed);
                    Log.d(DEBUG_TAG, "Payments List: " + paymentsList);
                }
                try {
                    adapter =  new TwoColumn_Adapter(PaymentsListActivity.this,R.layout.activity_two_column__adapter, paymentsList);
                    listView.setAdapter(adapter);
                }
                catch(NullPointerException e){
                    ArrayList array = new ArrayList<String>();
                    array.add("There are no purchased Items");
                    adapter =  new TwoColumn_Adapter(PaymentsListActivity.this,R.layout.activity_two_column__adapter, array);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        } );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopupMenu popupMenu = new PopupMenu(PaymentsListActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.payments_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.paymentPaid:
                                // case for moving item to shopping list

                                // remove payment from list
                                myRef.child("rooms").child(realRoom).child("roommates").child(userName).child("payments").child(paymentsList.get(position).getPerson()).removeValue();

                                // remove payment from local list
                                paymentsList.remove(position);

                                // update adapter
                                adapter.notifyDataSetChanged();
                                break;
                        }

                        return true;
                    }
                });

                popupMenu.show();

            }
        });

    }
}