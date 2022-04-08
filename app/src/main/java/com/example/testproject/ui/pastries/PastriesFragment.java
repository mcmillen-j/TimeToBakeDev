package com.example.testproject.ui.pastries;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.testproject.MainActivity;
import com.example.testproject.R;
import com.example.testproject.recipe;
import com.example.testproject.recipePastriesAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PastriesFragment extends Fragment {

    private View pastriesFragmentView;

    private RecyclerView recyclerView;
    recipePastriesAdapter adapter; // Create Object of the Adapter class

    private DatabaseReference RecipeRef;

    public static PastriesFragment newInstance() {
        return new PastriesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        pastriesFragmentView = inflater.inflate(R.layout.fragment_pastries, container, false);

        RecipeRef = FirebaseDatabase.getInstance().getReference().child("Recipes");

        recyclerView = (RecyclerView) pastriesFragmentView.findViewById(R.id.recyclerView);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<recipe> options
                = new FirebaseRecyclerOptions.Builder<recipe>()
                .setQuery(RecipeRef, recipe.class)
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new recipePastriesAdapter(options, getContext());

        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);

        return pastriesFragmentView;
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override public void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override public void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

}