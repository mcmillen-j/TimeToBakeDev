package com.example.testproject.ui.displayRecipes;

import androidx.activity.OnBackPressedCallback;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DisplayRecipesFragment extends Fragment {

    private TextView recipeNameText, recipeTimeText, recipeLevelText, recipeDescriptionText, overallRatingText;
    private ImageView recipeImage;
    private Button ingredientsBtn, methodBtn;
    private ImageButton favImageBtn;
    private RatingBar ratingBar;

    private DatabaseReference RecipeRef, RatingRef;

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

        recipeImage = (ImageView) view.findViewById(R.id.recipeImage);
        favImageBtn = (ImageButton) view.findViewById(R.id.favouriteImg);
        ingredientsBtn = (Button) view.findViewById(R.id.ingredientsBtn);
        methodBtn = (Button) view.findViewById(R.id.methodBtn);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar); // initiate a rating bar

        overallRatingText = (TextView) view.findViewById(R.id.ratingText);

        RecipeRef = FirebaseDatabase.getInstance().getReference();
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
                        String retrieveFavStatus = dataSnapshot.child("favourite").getValue().toString();

                        recipeNameText.setText(retrieveRecipeName);
                        recipeLevelText.setText(retrieveLevel);
                        recipeTimeText.setText(retrieveTime);
                        recipeDescriptionText.setText(retrieveDescription);
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
                activity.updateFavouriteClick(currentRecipeName);
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