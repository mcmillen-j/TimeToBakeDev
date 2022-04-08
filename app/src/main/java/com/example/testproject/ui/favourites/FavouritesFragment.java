package com.example.testproject.ui.favourites;

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

import com.example.testproject.AccountActivity;
import com.example.testproject.MainActivity;
import com.example.testproject.R;
import com.example.testproject.favouritesAdapter;
import com.example.testproject.recipe;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FavouritesFragment extends Fragment {

    private View favouritesFragmentView;

    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    favouritesAdapter adapter; // Create Object of the Adapter class

    private TextView recipeNameText, recipeTimeText, recipeLevelText;
    private ImageView recipeImage;
    private ImageButton favImageBtn;

    private DatabaseReference RecipeRef, FavouriteRef;

    public String recipeNameToDisplay = "No favourites";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        favouritesFragmentView = inflater.inflate(R.layout.fragment_favourites, container, false);

        RecipeRef = FirebaseDatabase.getInstance().getReference().child("Recipes");
        FavouriteRef = FirebaseDatabase.getInstance().getReference().child("Favourites");

        mAuth = FirebaseAuth.getInstance();
        String currentUserID = mAuth.getCurrentUser().getUid();

        recyclerView = (RecyclerView) favouritesFragmentView.findViewById(R.id.recyclerView);

//        favImageBtn = (ImageButton) view.findViewById(R.id.favouriteImg);

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
        adapter = new favouritesAdapter(options, getContext());

        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);

        MainActivity activity = (MainActivity) getActivity();
//        activity.updateFavouriteClick("Macaroons");
//        Toast.makeText(activity, "Clicked", Toast.LENGTH_SHORT).show();

//        favImageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                activity.updateFavouriteClick("Macaroons");
//                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });

        FavouriteRef.child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String favValue = dataSnapshot.child("Macaroons").child("Favourite").getValue().toString();
                        if (favValue.contentEquals("Yes")) {
                            recipeNameToDisplay = "Macaroons";
                            activity.setRecipeNameToDisplay(recipeNameToDisplay);

                            // Connecting Adapter class with the Recycler view*/
                            recyclerView.setAdapter(adapter);
                        }
                        String favValue2 = dataSnapshot.child("Vegan Angel Cake").child("Favourite").getValue().toString();
                        if (favValue2.contentEquals("Yes")) {
                            recipeNameToDisplay = "Vegan Angel Cake";
                            activity.setRecipeNameToDisplay(recipeNameToDisplay);

                            // Connecting Adapter class with the Recycler view*/
//                            recyclerView.setAdapter(adapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//        FavouriteRef.child(currentUserID).child("Vegan Angel Cake")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        String favValue = dataSnapshot.child("Favourite").getValue().toString();
//                        if (favValue.contentEquals("Yes")) {
//                            recipeNameToDisplay = "Vegan Angel Cake";
//                            activity.setRecipeNameToDisplay(recipeNameToDisplay);
//
//                            // Connecting Adapter class with the Recycler view*/
//                            recyclerView.setAdapter(adapter);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

//        activity.setRecipeNameToDisplay(recipeNameToDisplay);

        return favouritesFragmentView;
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
