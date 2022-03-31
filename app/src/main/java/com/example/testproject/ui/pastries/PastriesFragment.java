package com.example.testproject.ui.pastries;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testproject.LoginActivity;
import com.example.testproject.MainActivity;
import com.example.testproject.R;
import com.example.testproject.RegisterActivity;
import com.example.testproject.recipe;
import com.example.testproject.recipeAdapter;
import com.example.testproject.ui.displayRecipes.DisplayRecipesFragment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PastriesFragment extends Fragment {

    private View pastriesFragmentView;

    private RecyclerView recyclerView;
    recipeAdapter adapter; // Create Object of the Adapter class

    private DatabaseReference RecipeRef;

    public static PastriesFragment newInstance() {
        return new PastriesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        pastriesFragmentView = inflater.inflate(R.layout.fragment_pastries, container, false);

        RecipeRef = FirebaseDatabase.getInstance().getReference().child("Recipes");

//        ImageButton recipeClickBtn = (ImageButton) recipeView.findViewById(R.id.recipeImage);
//        Navigation.findNavController(recipeView).navigate(R.id.navigation_display_recipe);
//
//        recipeClickBtn.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
////                Toast.makeText(getActivity(), "Clicked!", Toast.LENGTH_SHORT).show();
//
//            }
//        });

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
        adapter = new recipeAdapter(options);

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