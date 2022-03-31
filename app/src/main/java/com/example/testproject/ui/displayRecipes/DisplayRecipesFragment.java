package com.example.testproject.ui.displayRecipes;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testproject.AccountActivity;
import com.example.testproject.MainActivity;
import com.example.testproject.R;
import com.example.testproject.recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DisplayRecipesFragment extends Fragment {

    private TextView recipeNameText, recipeTimeText, recipeLevelText, recipeDescriptionText, recipeIngredientsText, recipeMethodText;
    private ImageView recipeImage;
    private ImageButton favImageBtn;
    private RatingBar ratingBar;

    private DatabaseReference RecipeRef;

    public static DisplayRecipesFragment newInstance() {
        return new DisplayRecipesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_recipe, null);

        MainActivity activity = (MainActivity) getActivity();
        final String[] recipeName = {activity.getRecipeNameFromFragment().toString()};

        recipeNameText = (TextView) view.findViewById(R.id.recipeName);
        recipeLevelText = (TextView) view.findViewById(R.id.recipeLevel);
        recipeTimeText = (TextView) view.findViewById(R.id.recipeTime);
        recipeDescriptionText = (TextView) view.findViewById(R.id.recipeDescription);
        recipeMethodText = (TextView) view.findViewById(R.id.recipeMethod);

        recipeImage = (ImageView) view.findViewById(R.id.recipeImage);
        favImageBtn = (ImageButton) view.findViewById(R.id.favouriteImg);

        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar); // initiate a rating bar

        RecipeRef = FirebaseDatabase.getInstance().getReference();
        final String[] retrieveRecipeName = new String[1];

        RecipeRef.child("Recipes").child(recipeName[0])
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        retrieveRecipeName[0] = dataSnapshot.child("recipeName").getValue().toString();
                        String retrieveLevel = dataSnapshot.child("level").getValue().toString();
                        String retrieveTime = dataSnapshot.child("time").getValue().toString();
                        String retrieveDescription = dataSnapshot.child("description").getValue().toString();
                        String retrieveImage = dataSnapshot.child("image").getValue().toString();
                        String retrieveRating = dataSnapshot.child("rating").getValue().toString();

                        String retrieveFavStatus = dataSnapshot.child("favourite").getValue().toString();

                        recipeNameText.setText(retrieveRecipeName[0]);
                        recipeLevelText.setText(retrieveLevel);
                        recipeTimeText.setText(retrieveTime);
                        recipeDescriptionText.setText(retrieveDescription);
                        recipeMethodText.setText(retrieveFavStatus);
                        ratingBar.setRating(Float.parseFloat(retrieveRating));
                        Picasso.get().load(retrieveImage).into(recipeImage);

                        if (retrieveFavStatus.contentEquals("Yes")) {
                            favImageBtn.setImageResource(R.drawable.ic_baseline_favorite_24);
                        } else if (retrieveFavStatus.contentEquals("No")) {
                            favImageBtn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        favImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.updateFavouriteClick(retrieveRecipeName[0]);
                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


}