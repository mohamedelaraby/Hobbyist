package com.project.android.hobbyist;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.karan.churi.PermissionManager.PermissionManager;
import com.project.android.hobbyist.Model.User;
import com.project.android.hobbyist.Utils.FirebaseMethods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import xyz.belvi.permissiondialog.Permission.PermissionDetails;
import xyz.belvi.permissiondialog.Permission.SmoothPermission;
import xyz.belvi.permissiondialog.Rationale.Rationale;
import xyz.belvi.permissiondialog.Rationale.RationaleResponse;

public class RegisterActivity extends AppCompatActivity{

    private final String TAG = RegisterActivity.class.getSimpleName();
    ArrayList<String> days = new ArrayList<>();
    ArrayList<String> years = new ArrayList<>();
    List<String> hobbies = new ArrayList<>();
    User newUser;
    FirebaseMethods firebaseMethods;
    String email, password, fullName = "", dayOfBD, monthOfBD, yearOfBD, hobby, country, gender;
    String input = "";
    int genderSwitchPos;
    private CircleImageView mProfileImage;
    private ExtendedEditText mFullNameInput;
    private ExtendedEditText mEmailInput;
    private ExtendedEditText mPhoneNumberInput;
    private ExtendedEditText mPasswordInput;
    private ExtendedEditText mRepeatPasswordInput;
    private MaterialSpinner mDaySpinner;
    private MaterialSpinner mMonthSpinner;
    private MaterialSpinner mYearSpinner;
    private TextView mGender;
    private ToggleSwitch mGenderToggleSwitch;
    private CountryCodePicker mCountrySpinner;
    private MaterialSpinner mHobbySpinner;
    private FlatButton mDoneButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUser;
    private PermissionManager mPermissionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //create instance from FirebaseMethos.
        firebaseMethods = new FirebaseMethods(this);
        initView();
        setupFirebaseAuth();
        doneOnClick();
        getSelectedDate();
        getGenderToggleSwitch();

        newUser = new User();


        daySpinnerData();
        monthSpinnerData();
        yearSpinnerData();
        hobbiesSpinnerData();

        permissions();







    }

    private void permissions() {
        mPermissionManager = new PermissionManager() {};
        mPermissionManager.checkAndRequestPermissions(this);
    }

    private void getGenderToggleSwitch() {
        mGenderToggleSwitch.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                Log.v(TAG, "onGenderToggleSwitchListener: " + mGenderToggleSwitch.getCheckedTogglePosition());
                //get checked toggle position.
                genderSwitchPos = position;
                //save gender.
                newUser.setGender(convertToString(position));

            }
        });
    }

    private String convertToString(int position) {
        switch (position) {
            case 0:
                return "Male";
            case 1:
                return "Female";
            default:
                return "Other";

        }
    }

    private void doneOnClick() {
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validate data of edit text.
                submit();
                //get data from edit texts.
                //user email.
                email = mEmailInput.getText().toString();
                //save email
                newUser.setEmail(email);
                //user name
                fullName = mFullNameInput.getText().toString();
                //save user name
                newUser.setFullName(fullName);
                //user password
                password = mPasswordInput.getText().toString();

                //create new account using email and password.
                firebaseMethods.registerNewEmail(email, password);

                permissions();

                firebaseMethods.saveUserInfo(newUser);




            }
        });
    }

    private void hobbiesSpinnerData() {
        hobbies = Arrays.asList(getResources().getStringArray(R.array.hobbies));
        Collections.sort(hobbies);

        mHobbySpinner.setItems(hobbies);
    }

    private void yearSpinnerData() {
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 1970; i--) {
            years.add(String.valueOf(i));
        }

        mYearSpinner.setItems(years);
    }

    private void monthSpinnerData() {
        List<String> months = Arrays.asList(getResources().getStringArray(R.array.months));
        mMonthSpinner.setItems(months);


    }

    private void daySpinnerData() {
        for (int i = 1; i <= 31; i++) {
            days.add(String.valueOf(i));
        }

        mDaySpinner.setItems(days);

    }

    private void initView() {
        mProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        mFullNameInput = (ExtendedEditText) findViewById(R.id.full_name_input);
        mEmailInput = (ExtendedEditText) findViewById(R.id.email_input);
        mPhoneNumberInput = (ExtendedEditText) findViewById(R.id.phone_number_input);
        mPasswordInput = (ExtendedEditText) findViewById(R.id.password_input);
        mRepeatPasswordInput = (ExtendedEditText) findViewById(R.id.repeat_password_input);
        mDaySpinner = (MaterialSpinner) findViewById(R.id.day_spinner);
        mMonthSpinner = (MaterialSpinner) findViewById(R.id.month_spinner);
        mYearSpinner = (MaterialSpinner) findViewById(R.id.year_spinner);
        mGender = (TextView) findViewById(R.id.gender);
        mGenderToggleSwitch = (ToggleSwitch) findViewById(R.id.gender_toggle_switch);
        mCountrySpinner = (CountryCodePicker) findViewById(R.id.country_spinner);
        mHobbySpinner = (MaterialSpinner) findViewById(R.id.hobby_spinner);
        mDoneButton = findViewById(R.id.done_btn);


    }

    private void submit() {
        // validate
        input = mFullNameInput.getText().toString().trim();
        if (TextUtils.isEmpty(input)) {
            mFullNameInput.setError("Enter your name...");
        }

        input = mEmailInput.getText().toString().trim();
        if (TextUtils.isEmpty(input)) {
            mEmailInput.setError("Enter your email...");

        }

        input = mPasswordInput.getText().toString().trim();
        if (TextUtils.isEmpty(input)) {
            mPasswordInput.setError("Enter your password...");
        }

        input = mRepeatPasswordInput.getText().toString().trim();
        if (TextUtils.isEmpty(input)) {
            mRepeatPasswordInput.setError("Repeat password...");

        }

        if (!mPasswordInput.getText().toString().equals(mRepeatPasswordInput.getText().toString())) {
            Toasty.error(this, "Password doesn't match. Please repeat your password correctly", Toast.LENGTH_LONG, true).show();
        }

        // TODO validate success, do something


    }

    private void getSelectedDate() {
        mDaySpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Log.v(TAG, "onDaySelectedItem: " + item.toString());
                //get selected day.
                dayOfBD = item.toString();
                //save selected day.
                newUser.setDayOfDateBirth(dayOfBD);

            }
        });

        mMonthSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Log.v(TAG, "onMonthSelectedItem: " + item.toString());
                //get selected month.
                monthOfBD = item.toString();
                //save month.
                newUser.setMonthOfDateBirth(monthOfBD);

            }
        });

        mYearSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Log.v(TAG, "onYearSelectedItem: " + item.toString());
                //get selected year.
                yearOfBD = item.toString();
                //save year.
                newUser.setYearOfDateBirth(yearOfBD);
            }
        });

        mHobbySpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Log.v(TAG, "onHobbySelectedItem: " + item.toString());
                //get selected hobby.
                hobby = item.toString();
                //save hobby.
                newUser.setHobby(hobby);
            }
        });

        mCountrySpinner.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Log.v(TAG, "onCountrySelectedItem: " + mCountrySpinner.getSelectedCountryName());
                //get selected country.
                country = mCountrySpinner.getSelectedCountryName();
                //save country.
                newUser.setCountry(country);


            }
        });


    }

    private void setupFirebaseAuth() {
        Log.v(TAG, "onSetupFirebaseAuth: setting up firebase auth.");
        //get instance of firebase Auth.
        mAuth = FirebaseAuth.getInstance();
        //get instance of firebase database.
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //get user reference from firebase database.
        mUser = mFirebaseDatabase.getReference().child("users");

        //create new instance of AuthStateListener.
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //get current user
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                //check whether current user != null or not.
                if (currentUser != null) {
                    //user is signed in.
                    Log.v(TAG, "onAuthStateListener: user is signed in: " + currentUser.getUid());
                    //add value to user table.
                    mUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //add new user to firebase.
                            firebaseMethods.saveUserInfo(newUser);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Log.v(TAG, "onAuthStateListener: user is signed out: ");
                }

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        //add AuthStateListener to Firebase auth.
        mAuth.addAuthStateListener(mAuthStateListener);



    }

    @Override
    protected void onStop() {
        super.onStop();
        //remove AuthStateListener.
        mAuth.removeAuthStateListener(mAuthStateListener);



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionManager.checkResult(requestCode,permissions,grantResults);

        ArrayList<String> grantedPermission = mPermissionManager.getStatus().get(0).granted;
        ArrayList<String> deniedPermission = mPermissionManager.getStatus().get(0).denied;

    }
}


