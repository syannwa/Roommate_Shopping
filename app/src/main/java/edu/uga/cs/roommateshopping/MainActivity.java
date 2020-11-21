/**
 * CSCI 4060 Final Project - Roommate Shopping List
 * Sydney Waters and Molly Hutchinson
 *
 * The goal of this app is to implement a shopping list that roommates can share and update.
 * Our app is implemented using Google Firebase for backend database management.
 */


package edu.uga.cs.roommateshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
* This MainActivity displays the initial splashscreen with the options to either login or
* register. Both buttons open separate child acitvities of this main activity.
 */
public class MainActivity extends AppCompatActivity {

    private Button registerButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerButton = findViewById(R.id.splashRegisterButton);
        loginButton = findViewById(R.id.splashLoginButton);

        // Assigning the onClickListener for the register button
        registerButton.setOnClickListener(new RegisterButtonClickListener());
        // Assigning the onClickListener for the login button
        loginButton.setOnClickListener(new LoginButtonClickListener());
    }

    /*
    * This class defines the onClickListener class for the Registration button. This listener
    * opens the RegisterActivity class.
     */
    public class RegisterButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), RegisterActivity.class);
            v.getContext().startActivity(intent);
        }
    }

    /*
     * This class defines the onClickListener class for the Login button. This listener
     * opens the LoginActivity class.
     */
    public class LoginButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), LoginActivity.class);
            v.getContext().startActivity(intent);
        }
    }
}