package com.example.testproject.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
    private ImageView recipeWeekBtn, pastriesBtn, cakesBtn, breadBtn;

    private static final String TAG = "RecipesFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipes, container, false);

        recipeName = (TextView) view.findViewById(R.id.recipeName);
        recipeLevel = (TextView) view.findViewById(R.id.recipeLevel);
        recipeTime = (TextView) view.findViewById(R.id.recipeTime);
        recipeWeekBtn = (ImageView) view.findViewById(R.id.recipeWeekBtn);
        pastriesBtn = (ImageView) view.findViewById(R.id.pastriesBtn);
        cakesBtn = (ImageView) view.findViewById(R.id.cakesBtn);
        breadBtn = (ImageView) view.findViewById(R.id.breadBtn);

        String pastriesLink = "https://firebasestorage.googleapis.com/v0/b/test-87fff.appspot.com/o/pastries.webp?alt=media&token=04c80601-eb0c-4661-8272-f93e3efec22d";
        Picasso.get().load(pastriesLink).into(pastriesBtn);

        String cakeLink = "https://firebasestorage.googleapis.com/v0/b/test-87fff.appspot.com/o/cake.jpg?alt=media&token=f4fd6f7a-e162-4fee-8b01-a592f940628a";
        Picasso.get().load(cakeLink).into(cakesBtn);

        String breadLink = "https://firebasestorage.googleapis.com/v0/b/test-87fff.appspot.com/o/bread.jpg?alt=media&token=87963109-08bb-490a-bf52-938f33ce8633";
        Picasso.get().load(breadLink).into(breadBtn);

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