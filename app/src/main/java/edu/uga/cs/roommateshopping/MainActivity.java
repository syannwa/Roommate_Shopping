package edu.uga.cs.roommateshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
*CSCI 4060 Final Project: Roommate Shopping App
* Molly Hutchinson and Sydney Waters
*
* This MainActivity displays the initial splashscreen with the options to either login or
* register. Both buttons open separate child activities of this main activity.
 */
public class MainActivity extends AppCompatActivity {

    private Button registerButton;
    private Button loginButton;

    /**
     * Creates the main menu with login/register for the app
     *
     * @param savedInstanceState the bundled saved state of the application
     */
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

    /**
    * This class defines the onClickListener class for the Registration button. This listener
    * opens the RegisterActivity class.
     */
    public class RegisterButtonClickListener implements View.OnClickListener {
        /**
         * Runs when register is clicked. When it is clicked, it opens a new intent for the page
         * that contains the register activity.
         *
         * @param v the button view that has called the method
         */
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), RegisterActivity.class);
            v.getContext().startActivity(intent);
        }
    }

    /**
     * This class defines the onClickListener class for the Login button. This listener
     * opens the LoginActivity class.
     */
    public class LoginButtonClickListener implements View.OnClickListener {
        /**
         * Runs when login is clicked. When it is clicked, it opens a new intent for the page
         * that contains the login page.
         *
         * @param v the button view that has called the method
         */
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), LoginActivity.class);
            v.getContext().startActivity(intent);
        }
    }
}