package com.project.android.hobbyist.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.android.hobbyist.Model.User;
import com.project.android.hobbyist.R;

import es.dmoral.toasty.Toasty;

/**
 * Created by hassa on 7/8/2018.
 */

public class FirebaseMethods {
    private static final String TAG = FirebaseMethods.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private Context context;

    private String userId;

    public FirebaseMethods(Context context) {
        this.context = context;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        //if user is already sign in.
        if (mFirebaseAuth.getCurrentUser() != null){
            //get current user id from firebase Auth.
            userId = mFirebaseAuth.getCurrentUser().getUid();

        }
    }

    public void registerNewEmail(final String email, final String password){
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if auth is successful.
                        if (task.isSuccessful()){
                            //show success toast.
                            Toasty.success(context, context.getResources().getString(R.string.sign_in_successfully)
                                    , Toast.LENGTH_SHORT,true).show();
                            //get current user id.
                            userId = mFirebaseAuth.getCurrentUser().getUid();
                            Log.v(TAG, "onAuthCompleteListener: Auth successful: " + userId);


                        }else if (!task.isSuccessful()){
                            //show error toast.
                            Toasty.error(context,context.getResources().getString(R.string.auth_failed)
                                    ,Toast.LENGTH_SHORT,true).show();
                            Log.v(TAG, "onAuthCompleteListener: Auth Failed: "
                                    + task.getException().getMessage());
                        }
                    }
                });

    }


    public void saveUserInfo(User user){
        mRef.child("users")
                .child(userId)
                .setValue(user)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.v(TAG,"onSavingCompleteListener: save user info successfully");
                }else {
                    Log.v(TAG, "onSavingCompleteListener: save user info error: "
                            + task.getException().getMessage());
                }
            }
        });

    }
}
