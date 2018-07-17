package com.project.android.hobbyist.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.project.android.hobbyist.Model.User;
import com.project.android.hobbyist.R;
import com.project.android.hobbyist.Utils.Common;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private final int PICK_IMAGE_REQUEST_CODE = 1;
    private final int LOCATION_REQUEST_CODE = 2018;
    private final int GPS_SETTINGS = 3;
    private final int INTERVAL = 1000;
    private final String TAG = RegisterActivity.class.getSimpleName();

    ArrayList<String> days = new ArrayList<>();
    ArrayList<String> years = new ArrayList<>();
    List<String> hobbies = new ArrayList<>();

    User newUser;

    Uri saveImageUri = null;

    String dayOfBD, monthOfBD, yearOfBD, hobby, gender;
    String userUID;

    int genderSwitchPos;
    GoogleApiClient mGoogleApiClient;
    PendingResult<LocationSettingsResult> result;
    Intent intent;
    boolean GPSStatue;
    LocationManager locationManager;
    private CircleImageView mProfileImage;
    private ExtendedEditText mFullNameInput;
    private ExtendedEditText mEmailInput;
    private ExtendedEditText mPhoneNumberInput;
    private ExtendedEditText mPasswordInput;
    private ExtendedEditText mRepeatPasswordInput;
    private MaterialSpinner mDaySpinner;
    private MaterialSpinner mMonthSpinner;
    private MaterialSpinner mYearSpinner;
    private MaterialSpinner mHobbySpinner;
    private ToggleSwitch mGenderToggleSwitch;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private FirebaseUser user;
    private DatabaseReference mUser;
    private StorageReference storageReference;
    private FlatButton mDoneBtn;
    private FusedLocationProviderClient mFusedLocationClient;

    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseSetup();

        newUser = new User();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        buildGoogleApiClient();


        initView();
        getSelectedDate();
        getGenderToggleSwitch();
        daySpinnerData();
        monthSpinnerData();
        yearSpinnerData();
        hobbiesSpinnerData();


    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    private void firebaseSetup() {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUser = mFirebaseDatabase.getReference()
                .child("users");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("UPI");

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {


                }

            }
        };
    }

    private void getGenderToggleSwitch() {
        mGenderToggleSwitch.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                Log.v(TAG, "onGenderToggleSwitchListener: " + mGenderToggleSwitch.getCheckedTogglePosition());
                //get checked toggle position.
                genderSwitchPos = position;

                gender = convertToString(position);

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
        mProfileImage = findViewById(R.id.profile_image);
        mFullNameInput = findViewById(R.id.full_name_input);
        mEmailInput = findViewById(R.id.email_input);
        mPhoneNumberInput = findViewById(R.id.phone_number_input);
        mPasswordInput = findViewById(R.id.password_input);
        mRepeatPasswordInput = findViewById(R.id.repeat_password_input);
        mDaySpinner = findViewById(R.id.day_spinner);
        mMonthSpinner = findViewById(R.id.month_spinner);
        mYearSpinner = findViewById(R.id.year_spinner);
        mGenderToggleSwitch = findViewById(R.id.gender_toggle_switch);
        mHobbySpinner = findViewById(R.id.hobby_spinner);


        mDoneBtn = findViewById(R.id.done_btn);
        mDoneBtn.setOnClickListener(this);
        mProfileImage.setOnClickListener(this);
    }

    private void getSelectedDate() {
        mDaySpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Log.v(TAG, "onDaySelectedItem: " + item.toString());
                //get selected day.
                dayOfBD = item.toString();

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


    }

    @Override
    protected void onStart() {
        super.onStart();
        //add AuthStateListener to Firebase auth.
        mAuth.addAuthStateListener(mAuthStateListener);

        mGoogleApiClient.connect();


    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        super.onStop();
        //remove AuthStateListener.
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);


        }




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_btn:
                if (Common.isConnectedToInternet(this)) {
                    createNewAccount();
                } else {
                    Toasty.warning(this,
                            "Please check your internet connection.",
                            Toast.LENGTH_SHORT,
                            true).show();
                }
                break;

            case R.id.profile_image:
                selectImageProfile();
        }
    }

    private void address(double lat, double lng) {
        Geocoder geocoder;
        List<Address> addresses;

        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
           /* String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();*/
            String country = addresses.get(0).getCountryName();
            String city = addresses.get(0).getSubAdminArea();

            newUser.setCountry(country);
            newUser.setCity(city);

            Log.v(TAG, "City: " + city);
            Log.v(TAG, "Country: " + country);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectImageProfile() {
        Intent selectImage = new Intent(Intent.ACTION_GET_CONTENT);
        selectImage.setType("image/*");
        startActivityForResult(Intent.createChooser(selectImage, "Select Image"), PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null) {

            saveImageUri = data.getData();

            CropImage.activity(saveImageUri)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri cropImageUri = result.getUri();

                Picasso.get()
                        .load(cropImageUri)
                        .into(mProfileImage);

                StorageReference filePath = storageReference.child(System.currentTimeMillis() + ".jpg");
                filePath.putFile(cropImageUri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Log.v(TAG, "onComplete: Upload image to Storage is success.");
                                    String imageUrl = String.valueOf(task.getResult());
                                    if (imageUrl != null) {
                                        newUser.setImageUrl(imageUrl);
                                    } else {
                                        newUser.setImageUrl(null);
                                    }


                                } else {
                                    Log.v(TAG, "onComplete: Upload image to Storage is failure: " + task.getException().getMessage());
                                }
                            }
                        });


            } else {
                Log.v(TAG, "onActivityResult: Image cannot be cropped.");
            }

        }

        Log.v(TAG, "onActivityResult: Image is selected successfully");

    }

    private void turnOnGPS() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        builder.setAlwaysShow(true);
        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.v(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(RegisterActivity.this, GPS_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        GPSStatue = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


    }

    private void createNewAccount() {
        String email = mEmailInput.getText().toString();
        String password = mPasswordInput.getText().toString();
        String repeatPass = mRepeatPasswordInput.getText().toString();
        String fullName = mFullNameInput.getText().toString();
        String phoneNumber = mPhoneNumberInput.getText().toString();

        if (TextUtils.isEmpty(email))
            mEmailInput.setError("Enter your email");
        if (TextUtils.isEmpty(password))
            mPasswordInput.setError("Enter your password");
        if (TextUtils.isEmpty(repeatPass))
            mRepeatPasswordInput.setError("Confirm your password");
        if (!password.equals(repeatPass))
            Toasty.error(this,
                    "Your password don't match with your confirm password.",
                    Toast.LENGTH_SHORT,
                    true).show();

        newUser.setEmail(email);
        newUser.setFullName(fullName);
        newUser.setPhoneNumber(phoneNumber);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        userUID = mAuth.getCurrentUser().getUid();
                        user = mAuth.getCurrentUser();

                        if (task.isSuccessful()) {
                            if (user != null) {
                                emailVerification(user);
                                saveUserInformation(userUID);
                                Log.v(TAG, "onAuthCompleterListener: success");

                            }


                        } else {
                            Log.v(TAG, "onAuthCompleterListener: failure", task.getException());
                        }

                    }
                });


    }

    private void emailVerification(final FirebaseUser user) {
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toasty.success(RegisterActivity.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT, true).show();
                     /*        user.reload();

                             if (user.isEmailVerified()){
                                 Log.v(TAG, "isEmailVerified: " + user.isEmailVerified());
                                 saveUserInformation(userUID);
                                 Intent in = new Intent(RegisterActivity.this, HomeActivity.class);
                                 startActivity(in);


                             }*/

                                finish();

                            } else {
                                Log.v(TAG, "onEmailVerificationSuccess: couldn't send verification email.", task.getException());
                                Toasty.error(RegisterActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    });
        }

    }

    private void saveUserInformation(String UID) {
        mUser.child(UID).setValue(newUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toasty.success(RegisterActivity.this,
                                    "Congratulations, you are member now. Have Fun!",
                                    Toast.LENGTH_LONG,
                                    true).show();

                        }

                    }
                });
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, "onLocationChanged: " + location);

        double lat = location.getLatitude();
        double lng = location.getLongitude();

        newUser.setLat(lat);
        newUser.setLng(lng);

        address(lat, lng);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);

            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);



    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v(TAG, "onConnectionFailed: error : " + connectionResult.getErrorMessage());

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission was granted.");
                    turnOnGPS();
                }else {
                    Log.v(TAG, "permission denied.");
                }

        }
    }
}
