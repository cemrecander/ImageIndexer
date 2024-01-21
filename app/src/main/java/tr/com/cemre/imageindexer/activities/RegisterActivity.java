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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import tr.com.cemre.imageindexer.R;
import tr.com.cemre.imageindexer.model.User;

public class RegisterActivity extends AppCompatActivity {
    EditText etNameS, etMailS, etPassS;
    Button btnSignup;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNameS = findViewById(R.id.etNameSignup);
        etMailS = findViewById(R.id.etEmailSignup);
        etPassS = findViewById(R.id.etPassSignup);
        btnSignup = findViewById(R.id.btnSignup);
        tvLogin = findViewById(R.id.tvLogin);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = etNameS.getText().toString().trim();
                String strEmail = etMailS.getText().toString().trim();
                String strPass = etPassS.getText().toString().trim();

                if (strName.isEmpty() || strEmail.isEmpty() || strPass.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(strEmail, strPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                String uid = task.getResult().getUser().getUid();
                                Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference ref = db.collection("users");

                                DocumentReference userRef = ref.document(uid);

                                User user = new User(strName, strEmail, userRef.getPath());

                                userRef.set(user)
                                        .addOnSuccessListener(aVoid -> {
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(getApplicationContext(), "Registration failed (Firebase)", Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        });
                            } else {
                                Toast.makeText(getApplicationContext(),"Registration failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}