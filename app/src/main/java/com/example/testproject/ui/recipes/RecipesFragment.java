package com.example.testproject.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testproject.MainActivity;
import com.example.testproject.R;
import com.example.testproject.databinding.FragmentRecipesBinding;
import com.example.testproject.ui.favourites.FavouritesActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecipesFragment extends Fragment {

    private RecipeViewModel recipesViewModel;
    private FragmentRecipesBinding binding;

    private static final String TAG = "RecipesFragment";

    private View recipesView;

    private TextView textViewNew;
    private RecyclerView recipesRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        recipesViewModel =
                new ViewModelProvider(this).get(RecipeViewModel.class);

        binding = FragmentRecipesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textRecipes;
        recipesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
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