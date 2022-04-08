package com.example.testproject.ui.recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.testproject.MainActivity;
import com.example.testproject.R;
import com.example.testproject.databinding.FragmentRecipesBinding;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class RecipesFragment extends Fragment {

    private FragmentRecipesBinding binding;

    private DatabaseReference RecipeRef;

    private TextView recipeName, recipeTime, recipeLevel;
    private ImageButton recipeWeekBtn;

    private static final String TAG = "RecipesFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipes, container, false);

        recipeName = (TextView) view.findViewById(R.id.recipeName);
        recipeLevel = (TextView) view.findViewById(R.id.recipeLevel);
        recipeTime = (TextView) view.findViewById(R.id.recipeTime);
        recipeWeekBtn = (ImageButton) view.findViewById(R.id.recipeWeekBtn);

        RecipeRef = FirebaseDatabase.getInstance().getReference();

        RecipeRef.child("Recipes")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String retrieveRecipeName = snapshot.child("recipeName").getValue().toString();
                            String retrieveLevel = snapshot.child("level").getValue().toString();
                            String retrieveTime = snapshot.child("time").getValue().toString();
                            String retrieveImage = snapshot.child("image").getValue().toString();
                            String retrieveRecipeWeek = snapshot.child("recipeOfWeek").getValue().toString();

                            if (retrieveRecipeWeek.contentEquals("Yes")) {
                                recipeName.setText(retrieveRecipeName);
                                recipeLevel.setText(retrieveLevel);
                                recipeTime.setText(retrieveTime);
                                Picasso.get().load(retrieveImage).into(recipeWeekBtn);

                                recipeWeekBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        MainActivity activity = (MainActivity) getActivity();
                                        activity.displayRecipeClick(v, retrieveRecipeName);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

        return view;
            View view = inflater.inflate(R.layout.fragment_recipes, container, false);
            TextView recipeName = (TextView) view.findViewById(R.id.recipeNameFav);



            MainActivity activity = (MainActivity) getActivity();
            String recipeNameLoaded = activity.getRecipeName();

            recipeName.setText(recipeNameLoaded);

            return view;
        }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}