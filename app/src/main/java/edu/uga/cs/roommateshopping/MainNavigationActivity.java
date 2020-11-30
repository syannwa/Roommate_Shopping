package edu.uga.cs.roommateshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainNavigationActivity extends AppCompatActivity {

    private static final String TAG = "Main Nav Activity";
    private Button roommates;
    private Button groupShop;
    private Button purchased;
    private Button cashOut;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);

        roommates = findViewById(R.id.roommatesListButton);
        groupShop = findViewById(R.id.groupShoppingButton);
        purchased = findViewById(R.id.purchasesButton);
        cashOut = findViewById(R.id.cashOutButton);
        logout = findViewById(R.id.logoutButton);

        roommates.setOnClickListener(new RoommatesClickListener());
        groupShop.setOnClickListener(new GroupListClickListener());
        purchased.setOnClickListener(new PurchasesClickListener());
        cashOut.setOnClickListener(new CashOutClickListener());
        logout.setOnClickListener(new LogOutClickListener());
    }

    private class RoommatesClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //Display Roommates List
            Intent intent = new Intent(v.getContext(), RoommateViewActivity.class);
            v.getContext().startActivity(intent);

        }
    }

    private class GroupListClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), GroupShoppingListActivity.class);
            v.getContext().startActivity(intent);
        }
    }

    private class PurchasesClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //Display Purchased List
        }
    }

    private class CashOutClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //Display Cash out screen
        }
    }

    private class LogOutClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.d(TAG, "Logged out");
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            v.getContext().startActivity(intent);
        }
    }
}