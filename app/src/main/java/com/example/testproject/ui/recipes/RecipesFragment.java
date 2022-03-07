package com.example.testproject.ui.recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.testproject.MainActivity;
import com.example.testproject.R;
import com.example.testproject.databinding.FragmentRecipesBinding;

public class RecipesFragment extends Fragment {

    private RecipeViewModel recipesViewModel;
    private FragmentRecipesBinding binding;

    private static final String TAG = "RecipesFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

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