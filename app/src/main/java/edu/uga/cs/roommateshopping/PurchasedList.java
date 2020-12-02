package edu.uga.cs.roommateshopping;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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
    private Long numRoommates;
    String realRoom = "0";
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
        settleCostButton = findViewById(R.id.settleCostButton);
        listView.setAdapter(adapter);

        // Settle Cost Click Listener
        settleCostButton.setOnClickListener(new SettleCostClickListener());

        myRef = FirebaseDatabase.getInstance().getReference();

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

                DataSnapshot rooms = null;
                realRoom = "0";

                // Get room num
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String currentRoom = ds.child(userID).child("room").getValue(String.class);
                    if (currentRoom != null) {
                        realRoom = currentRoom;
                    } else
                        rooms = ds;
                }

                numRoommates = snapshot.child("rooms").child(realRoom).child("roommates").getChildrenCount();

                for(DataSnapshot ds : snapshot.child("users").child(userID).child("purchased").getChildren()){
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
                    calculateTotal(itemsList);
                    adapter =  new ThreeColumn_Adapter(PurchasedList.this,R.layout.activity_three_column__adapter, itemsList);
                    listView.setAdapter(adapter);
                }
                catch(NullPointerException e){
                    ArrayList array = new ArrayList<String>();
                    array.add("There are no purchased Items");
                    adapter =  new ThreeColumn_Adapter(PurchasedList.this,R.layout.activity_three_column__adapter, array);
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
                PopupMenu popupMenu = new PopupMenu(PurchasedList.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.purchased_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.itemShop:
                                // case for moving item to shopping list

                                // add item to shopping list
                                Log.d(DEBUG_TAG, "Room Number: " + realRoom);
                                Log.d(DEBUG_TAG, "Item Name: " + itemsList.get(position).getName());
                                Log.d(DEBUG_TAG, "Item: " + itemsList.get(position));
                                myRef.child("lists").child(realRoom).child(itemsList.get(position).getName()).setValue(itemsList.get(position));
                                // remove item from purchased list
                                myRef.child("users").child(userID).child("purchased").child(itemsList.get(position).getName()).removeValue();

                                itemsList.remove(position);
                                calculateTotal(itemsList);

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

    private void calculateTotal(ArrayList<Item> itemsList) {
        for(Item item : itemsList) {
            total += item.getPrice();
        }
        totalText.setText("" + total);
    }

    private class SettleCostClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(DEBUG_TAG, "Num Roommates: " + numRoommates);
            Double individualCost = total / numRoommates;
            Log.d(DEBUG_TAG, "Indiv Cost: " + individualCost);

            FirebaseDatabase.getInstance().getReference().child("rooms").child(realRoom).child("roommates")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // @TODO : fix this shit
                                snapshot.child("payments").child(user.getEmail()).child("" + individualCost);
                                Log.d(DEBUG_TAG, "email: " + user.getEmail());
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }
    }
}