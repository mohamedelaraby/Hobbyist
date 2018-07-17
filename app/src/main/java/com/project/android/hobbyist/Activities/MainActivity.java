package com.project.android.hobbyist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.android.hobbyist.R;
import com.project.android.hobbyist.Utils.Common;

import es.dmoral.toasty.Toasty;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ExtendedEditText mEmailInput;
    private ExtendedEditText mPasswordInput;
    private Button mSignInBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setupFirebaseAuth();

    }

    private void setupFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }


    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void initView() {
        mEmailInput = findViewById(R.id.email_input);
        mPasswordInput = findViewById(R.id.password_input);
        mSignInBtn = findViewById(R.id.sign_in_btn);
        mSignInBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_btn:
                signInUser();

                break;
        }
    }

    private void signInUser() {
        String email = mEmailInput.getText().toString();
        String password = mPasswordInput.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailInput.setError("Enter your email");
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordInput.setError("Enter your password");
            return;
        }

        if (Common.isConnectedToInternet(this)) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (task.isSuccessful()) {
                                Log.v(TAG, "onComplete: user is signed in");
                                try {
                                    if (user.isEmailVerified()) {
                                        Toasty.success(MainActivity.this, "Welcome to Hobbyist App. Have fun!", Toast.LENGTH_SHORT, true).show();
                                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        Log.v(TAG, "onSignInWithEmailCompleter: email is verified? " + user.isEmailVerified());
                                    } else {
                                        Toasty.warning(MainActivity.this, "Email is not verified \n please check you email inbox.", Toast.LENGTH_SHORT, true).show();
                                        mAuth.signOut();
                                    }
                                } catch (NullPointerException e) {
                                    Log.v(TAG, "onSignInWithEmailComplete: NullPointerException:  " + e.getMessage());
                                }
                            } else {
                                Toasty.warning(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT, true).show();
                                Log.v(TAG, "onComplete: Authentication failed: " + task.getException().getMessage());
                            }
                        }
                    });

        } else {
            Toasty.error(this, "Check your internet connection.", Toast.LENGTH_SHORT, true).show();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            if (currentUser.isEmailVerified()) {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);

            }

        }
    }
}
