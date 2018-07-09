package com.project.android.hobbyist;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;

public class HomeActivity extends AppCompatActivity {

    private SpaceNavigationView mNavigationSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




    }

    private void initView() {
        mNavigationSpace = (SpaceNavigationView) findViewById(R.id.navigation_space);
        mNavigationSpace.addSpaceItem(new SpaceItem(getResources().getString(R.string.explore),R.drawable.ic_explore_black_24dp));
        mNavigationSpace.addSpaceItem(new SpaceItem(getResources().getString(R.string.messages),R.drawable.ic_chat_black_24dp));
        mNavigationSpace.addSpaceItem(new SpaceItem(getResources().getString(R.string.notifications),R.drawable.ic_notifications_black_24dp));
        mNavigationSpace.addSpaceItem(new SpaceItem(getResources().getString(R.string.profile),R.drawable.ic_person_black_24dp));
        mNavigationSpace.setSpaceBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigationSpace.onSaveInstanceState(outState);
    }
}
