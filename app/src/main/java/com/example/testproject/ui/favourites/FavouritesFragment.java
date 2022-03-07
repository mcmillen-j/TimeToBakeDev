package com.example.testproject.ui.favourites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.testproject.MainActivity;
import com.example.testproject.R;
import com.example.testproject.databinding.FragmentFavouritesBinding;

public class FavouritesFragment extends Fragment {

    private FragmentFavouritesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        TextView recipeName = (TextView) view.findViewById(R.id.recipeNameFav);
        TextView recipeTime = (TextView) view.findViewById(R.id.recipeTimeFav);

        MainActivity activity = (MainActivity) getActivity();
        String recipeNameLoaded = activity.getRecipeName();
        String recipeTimeLoaded = activity.getRecipeTime();

        recipeName.setText(recipeNameLoaded);
        recipeTime.setText(recipeTimeLoaded);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
//        if (favouriteValue.contentEquals("Yes")){
//                setVariable("Yes");
//                }
//
//
//
//                if (getVariable() == "Yes") {
//
//                TextView textView = findViewById(R.id.test);
//                textView.setText(getVariable());
//
//                } else {
//                Log.w(TAG, "Not a favourite");
//
//                }
//
//                }
//public void setVariable(String s)
//        {
//        this.displayFavourite = s;
//        }
//
//public String getVariable()
//        {
//        return displayFavourite;
//        }