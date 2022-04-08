package com.example.testproject.ui.cakes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.testproject.MainActivity;
import com.example.testproject.R;
import com.example.testproject.recipe;
import com.example.testproject.recipeCakesAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CakesFragment extends Fragment {

    private View cakesFragmentView;

    private RecyclerView recyclerView;
    private Button thirtyBtn, oneHourBtn, oneHourThirtyBtn, twoHourBtn;
    recipeCakesAdapter adapter; // Create Object of the Adapter class

    private DatabaseReference RecipeRef, FavouritesRef;

    public String timeSelected = "No time selected";

    public static CakesFragment newInstance() {
        return new CakesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        cakesFragmentView = inflater.inflate(R.layout.fragment_cakes, container, false);

        RecipeRef = FirebaseDatabase.getInstance().getReference().child("Recipes");
        FavouritesRef = FirebaseDatabase.getInstance().getReference().child("Favourites");


        recyclerView = (RecyclerView) cakesFragmentView.findViewById(R.id.recyclerView);

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
        adapter = new recipeCakesAdapter(options, getContext());

        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);

        Button thirtyBtn = (Button) cakesFragmentView.findViewById(R.id.thirtyMins);
        Button oneHourBtn = (Button) cakesFragmentView.findViewById(R.id.oneHour);
        Button oneHourThirtyBtn = (Button) cakesFragmentView.findViewById(R.id.oneHourThirtyMins);
        Button twoHourBtn = (Button) cakesFragmentView.findViewById(R.id.twoHours);

        MainActivity activity = (MainActivity) getActivity();

        thirtyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSelected = "30m";
                Log.d("TimeClicked: ", timeSelected);
                activity.setTimeSelected(timeSelected);
                recyclerView.setAdapter(adapter);
            }
        });
        oneHourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSelected = "1h";
                Log.d("TimeClicked: ", timeSelected);
                activity.setTimeSelected(timeSelected);
                recyclerView.setAdapter(adapter);
            }
        });
        oneHourThirtyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSelected = "1h30m";
                Log.d("TimeClicked: ", timeSelected);
                activity.setTimeSelected(timeSelected);
                recyclerView.setAdapter(adapter);
            }
        });
        twoHourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSelected = "2h";
                Log.d("TimeClicked: ", timeSelected);
                activity.setTimeSelected(timeSelected);
                recyclerView.setAdapter(adapter);
            }
        });

        activity.setTimeSelected(timeSelected);

        return cakesFragmentView;
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