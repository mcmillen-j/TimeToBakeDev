package com.example.testproject.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.testproject.MainActivity;
import com.example.testproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DisplayRecipesFragment extends Fragment {

    private TextView recipeNameText, recipeTimeText, recipeLevelText, recipeDescriptionText, overallRatingText, recipeServesText;
    private ImageView recipeImage;
    private Button ingredientsBtn, methodBtn;
    private ImageButton favImageBtn;
    private RatingBar ratingBar;

    private DatabaseReference RecipeRef, FavouritesRef, RatingRef;

    private FirebaseAuth mAuth;

    private String currentUserID, currentRecipeName;

    public static DisplayRecipesFragment newInstance() {
        return new DisplayRecipesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_recipe, null);

        MainActivity activity = (MainActivity) getActivity();
        currentRecipeName = activity.getRecipeNameClicked();

        recipeNameText = (TextView) view.findViewById(R.id.recipeName);
        recipeLevelText = (TextView) view.findViewById(R.id.recipeLevel);
        recipeTimeText = (TextView) view.findViewById(R.id.recipeTime);
        recipeDescriptionText = (TextView) view.findViewById(R.id.recipeDescription);
        recipeServesText = (TextView) view.findViewById(R.id.recipeServes);

        recipeImage = (ImageView) view.findViewById(R.id.recipeImage);
        favImageBtn = (ImageButton) view.findViewById(R.id.favouriteImg);
        ingredientsBtn = (Button) view.findViewById(R.id.ingredientsBtn);
        methodBtn = (Button) view.findViewById(R.id.methodBtn);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar); // initiate a rating bar

        overallRatingText = (TextView) view.findViewById(R.id.ratingText);

        RecipeRef = FirebaseDatabase.getInstance().getReference();
        FavouritesRef = FirebaseDatabase.getInstance().getReference().child("Favourites");
        RatingRef = FirebaseDatabase.getInstance().getReference().child("Ratings");

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        RecipeRef.child("Recipes").child(currentRecipeName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String retrieveRecipeName = dataSnapshot.child("recipeName").getValue().toString();
                        String retrieveLevel = dataSnapshot.child("level").getValue().toString();
                        String retrieveTime = dataSnapshot.child("time").getValue().toString();
                        String retrieveDescription = dataSnapshot.child("description").getValue().toString();
                        String retrieveImage = dataSnapshot.child("image").getValue().toString();
                        String retrieveServes = dataSnapshot.child("serves").getValue().toString();

                        recipeNameText.setText(retrieveRecipeName);
                        recipeLevelText.setText(retrieveLevel);
                        recipeTimeText.setText(retrieveTime);
                        recipeDescriptionText.setText(retrieveDescription);
                        Picasso.get().load(retrieveImage).into(recipeImage);
                        recipeServesText.setText(retrieveServes);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FavouritesRef.child(currentUserID).child(currentRecipeName).child("Favourite")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String retrieveFavStatus = dataSnapshot.getValue().toString();
                        if (retrieveFavStatus.contentEquals("Yes")) {
                            favImageBtn.setImageResource(R.drawable.ic_baseline_favorite_24);
                        } if (retrieveFavStatus.contentEquals("No")) {
                            favImageBtn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        RatingRef.child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if ((dataSnapshot.exists())) {
                            if ((dataSnapshot.child(currentRecipeName).exists())) {
                                String retrieveRating = dataSnapshot.child(currentRecipeName).getValue().toString();
                                ratingBar.setRating(Float.parseFloat(retrieveRating));
                            } else {
                                RatingRef.child(currentUserID).child(currentRecipeName).setValue("0.0");
                            }
                        } else {
                            RatingRef.child(currentUserID).child(currentRecipeName).setValue("0.0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        ingredientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.navigation_ingredients);
            }
        });

        methodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.navigation_method);
            }
        });

        favImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.updateFavouriteClick(view, currentRecipeName);
            }
        });

        addListenerOnRatingBar();

        return view;
    }

    public void addListenerOnRatingBar() {

        //if rating value is changed, update database
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                String s = Float.toString(rating);
                RatingRef.child(currentUserID).child(currentRecipeName).setValue(s);
            }
        });
    }

}