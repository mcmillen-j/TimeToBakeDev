package com.example.testproject.ui;

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
import android.widget.TextView;

import com.example.testproject.MainActivity;
import com.example.testproject.R;
import com.example.testproject.Recipe;
import com.example.testproject.RecipePastriesAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PastriesFragment extends Fragment {

    private View pastriesFragmentView;

    private RecyclerView recyclerView;
    RecipePastriesAdapter adapter; // Create Object of the Adapter class
    private Button thirtyBtn, oneHourBtn, oneHourThirtyBtn, twoHourBtn, beginnerBtn, intermediateBtn, advancedBtn, clearFiltersBtn;
    private TextView timeSelectedTxt, levelSelectedTxt;

    private DatabaseReference RecipeRef;

    public String timeSelected = "No time selected";
    public String levelSelected = "No level selected";

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

        // It is a class provide by the FirebaseUI to make a query in the database to fetch appropriate data
        FirebaseRecyclerOptions<Recipe> options
                = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(RecipeRef, Recipe.class)
                .build();
        // Connecting object of required Adapter class to the Adapter class itself
        adapter = new RecipePastriesAdapter(options, getContext());

        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);

        thirtyBtn = (Button) pastriesFragmentView.findViewById(R.id.thirtyMins);
        oneHourBtn = (Button) pastriesFragmentView.findViewById(R.id.oneHour);
        oneHourThirtyBtn = (Button) pastriesFragmentView.findViewById(R.id.oneHourThirtyMins);
        twoHourBtn = (Button) pastriesFragmentView.findViewById(R.id.twoHours);

        beginnerBtn = (Button) pastriesFragmentView.findViewById(R.id.beginner);
        intermediateBtn = (Button) pastriesFragmentView.findViewById(R.id.intermediate);
        advancedBtn = (Button) pastriesFragmentView.findViewById(R.id.advanced);
        clearFiltersBtn = (Button) pastriesFragmentView.findViewById(R.id.clearFilters);

        timeSelectedTxt = (TextView) pastriesFragmentView.findViewById(R.id.timeSelected);
        levelSelectedTxt = (TextView) pastriesFragmentView.findViewById(R.id.levelSelected);

        MainActivity activity = (MainActivity) getActivity();

        timeSelectedTxt.setText(timeSelected);
        levelSelectedTxt.setText(levelSelected);

        thirtyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSelected = "30m";
                timeSelectedTxt.setText(timeSelected);
                Log.d("TimeClicked: ", timeSelected);
                activity.setTimeSelected(timeSelected);
                recyclerView.setAdapter(adapter);
            }
        });
        oneHourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSelected = "1h";
                timeSelectedTxt.setText(timeSelected);
                Log.d("TimeClicked: ", timeSelected);
                activity.setTimeSelected(timeSelected);
                recyclerView.setAdapter(adapter);
            }
        });
        oneHourThirtyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSelected = "1h30m";
                timeSelectedTxt.setText(timeSelected);
                Log.d("TimeClicked: ", timeSelected);
                activity.setTimeSelected(timeSelected);
                recyclerView.setAdapter(adapter);
            }
        });
        twoHourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSelected = "2h";
                timeSelectedTxt.setText(timeSelected);
                Log.d("TimeClicked: ", timeSelected);
                activity.setTimeSelected(timeSelected);
                recyclerView.setAdapter(adapter);
            }
        });
        beginnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelSelected = "Beginner";
                levelSelectedTxt.setText(levelSelected);
                Log.d("Level clicked: ", levelSelected);
                activity.setLevelSelected(levelSelected);
                recyclerView.setAdapter(adapter);
            }
        });
        intermediateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelSelected = "Intermediate";
                levelSelectedTxt.setText(levelSelected);
                Log.d("Level clicked: ", levelSelected);
                activity.setLevelSelected(levelSelected);
                recyclerView.setAdapter(adapter);
            }
        });
        advancedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelSelected = "Advanced";
                levelSelectedTxt.setText(levelSelected);
                Log.d("Level clicked: ", levelSelected);
                activity.setLevelSelected(levelSelected);
                recyclerView.setAdapter(adapter);
            }
        });
        clearFiltersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSelected = "No time selected";
                levelSelected = "No level selected";
                timeSelectedTxt.setText(timeSelected);
                levelSelectedTxt.setText(levelSelected);
                activity.setTimeSelected(timeSelected);
                Log.d("Time clicked: ", timeSelected);
                activity.setLevelSelected(levelSelected);
                Log.d("Level clicked: ", levelSelected);
                recyclerView.setAdapter(adapter);
            }
        });

        activity.setTimeSelected(timeSelected);
        activity.setLevelSelected(levelSelected);

        return pastriesFragmentView;
    }

    // Function to tell the app to start getting data from database on starting of the activity
    @Override public void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting data from database on stopping of the activity
    @Override public void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

}