package com.project.android.hobbyist.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.project.android.hobbyist.Model.Posts;
import com.project.android.hobbyist.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container,false);
        ListView postRecyclerView = view.findViewById(R.id.posts_rv);

        String[] list = new String[]{"Hello","Welcome", "Hassan"};

        ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.posts_custom_view,R.id.post_text, list);
        postRecyclerView.setAdapter(adapter);



        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
