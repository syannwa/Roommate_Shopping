package edu.uga.cs.roommateshopping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{

    public static final String TAG = "Sign in Activity";

    private FirebaseAuth mAuth;
    private Button login;

    private EditText emailText;
    private EditText passwordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.loginButton);
        emailText = findViewById(R.id.emailText2);
        passwordText = findViewById(R.id.passwordText2);

        login.setOnClickListener(new LoginActivity.ButtonClickListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();
            signInUser(email, password);
        }
    }

    private void signInUser(String email, String password) {
        Log.d(TAG, "signInUser:" + email);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    public void updateUI(FirebaseUser account){

        if(account != null){
            Toast.makeText(this,"Signed in successfully.",Toast.LENGTH_LONG).show();
            //CHANGE THIS TO WHEREVER THE APP NEEDS TO GO NEXT
            //startActivity(new Intent(this, MainActivity.class));

        }else {
            Toast.makeText(this,"Error signing in: Could not find this user.",Toast.LENGTH_LONG).show();
        }

    }


}
