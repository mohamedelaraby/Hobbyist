package com.project.android.hobbyist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.project.android.hobbyist.Fragments.ChatFragment;
import com.project.android.hobbyist.Fragments.HomeFragment;
import com.project.android.hobbyist.Fragments.NotificationFragment;
import com.project.android.hobbyist.Fragments.ProfileFragment;
import com.project.android.hobbyist.R;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity {

    private SpaceNavigationView mNavigationSpace;
    private FirebaseAuth mAuth;

    Fragment home, notification, chat, profile;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        FragmentManager manager = getSupportFragmentManager();

        home = new HomeFragment();
        notification = new NotificationFragment();
        chat = new ChatFragment();
        profile = new ProfileFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, home)
                .commit();




    }

    private void initView() {
        mNavigationSpace = findViewById(R.id.navigation_space);
        mNavigationSpace.addSpaceItem(new SpaceItem(null,R.drawable.ic_explore_black_24dp));
        mNavigationSpace.addSpaceItem(new SpaceItem(null,R.drawable.ic_chat_black_24dp));
        mNavigationSpace.addSpaceItem(new SpaceItem(null,R.drawable.ic_notifications_black_24dp));
        mNavigationSpace.addSpaceItem(new SpaceItem(null,R.drawable.ic_person_black_24dp));
        mNavigationSpace.setSpaceBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mNavigationSpace.showIconOnly();

        mNavigationSpace.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Toasty.success(HomeActivity.this, "Center button is clicked", Toast.LENGTH_SHORT, true).show();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch (itemIndex){
                    case 0:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, home).commit();
                        break;
                    case 1:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, chat).commit();
                        break;
                    case 3:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, notification).commit();
                        break;
                    case 4:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, home).commit();
                        break;




                }

            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigationSpace.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.menu_sign_out:
                mAuth.signOut();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();

        }
        return super.onOptionsItemSelected(item);
    }

}
