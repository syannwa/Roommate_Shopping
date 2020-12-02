package edu.uga.cs.roommateshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PurchasedList extends AppCompatActivity {

    public static final String DEBUG_TAG = "PurchasedList_DEBUG";

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private String userID;
    private ListView listView;
    private ThreeColumn_Adapter adapter;

    private Button settleCostButton;
    private TextView totalText;

    private Double total = 0.0;
    String roomNumber;
    private ArrayList<Item> itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_list);

        // Initialize all relevant variables
        itemsList = new ArrayList<Item>();
        listView = findViewById(R.id.list_view);
        adapter =  new ThreeColumn_Adapter(PurchasedList.this,R.layout.activity_three_column__adapter, itemsList);
        totalText = findViewById(R.id.totalText);
        settleCostButton = findViewById(R.id.paybackButton);
        listView.setAdapter(adapter);

        myRef = FirebaseDatabase.getInstance().getReference("users");

        // Get user and User ID
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userID = user.getUid();

        // Get purchased items list from user
        myRef.addListenerForSingleValueEvent( new ValueEventListener() {

            @Override
            public void onDataChange( DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, knowing that this is a list,
                // we need to iterate over the elements and place them on a List.

                for(DataSnapshot ds : snapshot.child(userID).child("purchased").getChildren()){
                    Log.d(DEBUG_TAG, "DS Key: " + ds.getKey());

                    // Get item information
                    String name = ds.getValue(Item.class).getName();
                    Integer quantity = ds.getValue(Item.class).getQuantity();
                    Double price = ds.getValue(Item.class).getPrice();
                    Log.d(DEBUG_TAG, "Name: " + name + ", Quantity: " + quantity + ", Price: " + price);

                    // Add item to list
                    Item newItem = new Item(name, quantity, price);
                    itemsList.add(newItem);
                    Log.d(DEBUG_TAG, "Items List: " + itemsList);
                }
                try {
                    this.calculateTotal(itemsList);
                    adapter =  new ThreeColumn_Adapter(PurchasedList.this,R.layout.activity_three_column__adapter, itemsList);
                    listView.setAdapter(adapter);
                }
                catch(NullPointerException e){
                    ArrayList array = new ArrayList<String>();
                    array.add("There are no purchased Items");
                    adapter =  new ThreeColumn_Adapter(PurchasedList.this,R.layout.activity_three_column__adapter, itemsList);
                    listView.setAdapter(adapter);
                }
            }

            private void calculateTotal(ArrayList<Item> itemsList) {
                for(Item item : itemsList) {
                    total += item.getPrice();
                }
                totalText.setText("" + total);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        } );


    }
}