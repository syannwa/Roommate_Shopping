package edu.uga.cs.roommateshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainNavigationActivity extends AppCompatActivity {

    private Button roommates;
    private Button groupShop;
    private Button purchased;
    private Button cashOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);

        roommates = findViewById(R.id.roommatesListButton);
        groupShop = findViewById(R.id.groupShoppingButton);
        purchased = findViewById(R.id.purchasesButton);
        cashOut = findViewById(R.id.cashOutButton);

        roommates.setOnClickListener(new RoommatesClickListener());
        groupShop.setOnClickListener(new GroupListClickListener());
        purchased.setOnClickListener(new PurchasesClickListener());
        cashOut.setOnClickListener(new CashOutClickListener());
    }

    private class RoommatesClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //Display Roommates List
        }
    }

    private class GroupListClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //Display Group Shopping List
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
}