package tr.com.cemre.imageindexer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import tr.com.cemre.imageindexer.MainActivity;
import tr.com.cemre.imageindexer.R;

public class LoginActivity extends AppCompatActivity {
    EditText etMailL, etPassL;
    TextView tvSignup;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etMailL = findViewById(R.id.etEmailLogin);
        etPassL = findViewById(R.id.etPassLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the email and password entered by the user
                String stremail = etMailL.getText().toString().trim();
                String strpassword = etPassL.getText().toString().trim();

                // Check if email and password are empty
                if (stremail.isEmpty() || strpassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Authenticate the user using Firebase Authentication
                    FirebaseAuth auth =  FirebaseAuth.getInstance();
                    auth.signInWithEmailAndPassword(stremail, strpassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Login success, redirect to the main activity
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish(); // Optional: Close this LoginActivity
                                    } else {
                                        // Login failed
                                        Toast.makeText(getApplicationContext(), "Login Failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}