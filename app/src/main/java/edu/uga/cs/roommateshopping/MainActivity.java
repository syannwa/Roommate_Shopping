package edu.uga.cs.roommateshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button registerButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerButton = findViewById(R.id.splashRegisterButton);
        loginButton = findViewById(R.id.splashLoginButton);

        registerButton.setOnClickListener(new RegisterButtonClickListener());
        loginButton.setOnClickListener(new LoginButtonClickListener());
    }

    public class RegisterButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), RegisterActivity.class);
            v.getContext().startActivity(intent);
        }
    }

    public class LoginButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), LoginActivity.class);
            v.getContext().startActivity(intent);
        }
    }
}