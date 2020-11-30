package edu.uga.cs.roommateshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShoppingList extends AppCompatActivity {

    public static final String DEBUG_TAG = "ShoppingList";

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private String userID;
    private ListView listView;
    private ArrayAdapter arrayAdapter;

    private List<Item> itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        itemsList = new ArrayList<Item>();
        listView = findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter(ShoppingList.this, android.R.layout.simple_list_item_1, itemsList);
        listView.setAdapter(arrayAdapter);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("list");

        // database stuff
        myRef.addListenerForSingleValueEvent( new ValueEventListener() {

            // GET THE PROPER SHOPPING LIST BELOW

            @Override
            public void onDataChange( DataSnapshot snapshot ) {
                // Once we have a DataSnapshot object, knowing that this is a list,
                // we need to iterate over the elements and place them on a List.
                ArrayList<String> array = new ArrayList<>();
                DataSnapshot rooms = null;
                DataSnapshot lists = null;
                String realCurrentRoom = "0";

                for(DataSnapshot ds : snapshot.getChildren()) {
                    String currentRoom = ds.child(userID).child("room").getValue(String.class);
                    if (currentRoom != null) {
                        realCurrentRoom = currentRoom;
                    } else
                        lists = ds;
                }

                for(DataSnapshot ds : lists.child(realCurrentRoom).getChildren()){
                    array.add(ds.getKey());
                }
                try {
//                    ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.textview, itemsList);
//                    listView.setAdapter(adapter);
                }
                catch(NullPointerException e){
                    array.add("There are no roommates.");
//                    ArrayAdapter adapt = new ArrayAdapter<String>(this, R.layout.list_item, R.id.textview, itemsList);
//                    listView.setAdapter(adapt);
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
                PopupMenu popupMenu = new PopupMenu(ShoppingList.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.itemUpdate:
                                // case for update
                                AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingList.this);
                                View v = LayoutInflater.from(ShoppingList.this).inflate(R.layout.item_dialog, null, false);
                                builder.setTitle("Update Item");
                                final EditText editText = v.findViewById(R.id.itemText);
                                editText.setText(itemsList.get(position).getName());

                                builder.setView(v);

                                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!editText.getText().toString().isEmpty()) {
                                            itemsList.get(position).setName(editText.getText().toString().trim());
                                            // update database
                                            arrayAdapter.notifyDataSetChanged();
                                            Toast.makeText(ShoppingList.this, "Item Updated!", Toast.LENGTH_SHORT).show();

                                        } else {
                                            editText.setError("Update the Item!");
                                        }
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                builder.show();

                                break;

                            case R.id.itemDelete:
                                // case for delete
                                Toast.makeText(ShoppingList.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                                itemsList.remove(position);
                                // update database
                                arrayAdapter.notifyDataSetChanged();
                                break;
                        }

                        return true;
                    }
                });

                popupMenu.show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                //function to add
                addItem();
                break;

        }
        return true;
    }

    private void addItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingList.this);
        builder.setTitle("Add New Item");

        View v = LayoutInflater.from(ShoppingList.this).inflate(R.layout.item_dialog, null, false);
        builder.setView(v);

        Item item = new Item();
        EditText etItem = v.findViewById(R.id.itemText);
        EditText etQuantity = v.findViewById(R.id.quantityText);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(etItem.getText().toString().isEmpty() || etQuantity.getText().toString().isEmpty()) {
                    etItem.setError("Fill in available fields");
                }
                else{
                    item.setName(etItem.getText().toString().trim());
                    item.setQuantity(Integer.parseInt(etQuantity.getText().toString()));
                    // add item to database here

                    itemsList.add(item);
                    arrayAdapter.notifyDataSetChanged();

                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}