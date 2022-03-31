package com.example.testproject.ui.favourites;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testproject.MainActivity;
import com.example.testproject.R;
import com.example.testproject.favouriteAdapter;
import com.example.testproject.recipe;
import com.example.testproject.recipeAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FavouritesFragment extends Fragment {

    private View favouritesFragmentView;
    private View view;


    private RecyclerView recyclerView;
    favouriteAdapter adapter; // Create Object of the Adapter class

    private TextView recipeNameText, recipeTimeText, recipeLevelText;
    private ImageView recipeImage;
    private ImageButton favImageBtn;

    private DatabaseReference RecipeRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        favouritesFragmentView = inflater.inflate(R.layout.fragment_favourites, container, false);

        view = inflater.inflate(R.layout.favourite, container, false);

        RecipeRef = FirebaseDatabase.getInstance().getReference().child("Recipes");

        recyclerView = (RecyclerView) favouritesFragmentView.findViewById(R.id.recyclerView);

        favImageBtn = (ImageButton) view.findViewById(R.id.favouriteImg);

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
        adapter = new favouriteAdapter(options);

        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);

        MainActivity activity = (MainActivity) getActivity();
//        activity.updateFavouriteClick("Macaroons");
//        Toast.makeText(activity, "Clicked", Toast.LENGTH_SHORT).show();

        favImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.updateFavouriteClick("Macaroons");
                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });


        return favouritesFragmentView;
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
