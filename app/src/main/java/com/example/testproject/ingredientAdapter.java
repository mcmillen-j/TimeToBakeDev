package com.example.testproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class ingredientAdapter extends FirebaseRecyclerAdapter<
        ingredient, ingredientAdapter.ingredientsViewholder> {

    public ingredientAdapter(
            @NonNull FirebaseRecyclerOptions<ingredient> options) {
        super(options);
    }

    // Function to bind the view in Card view(here
    // "ingredient.xml") with data in
    // model class(here "ingredient.class")
    @Override
    protected void
    onBindViewHolder(@NonNull ingredientsViewholder holder,
                     int position, @NonNull ingredient model) {

//        if (model.getRecipeName().toString().contentEquals("Macaroons")) {

            // Add time from model class (here
            // "ingredient.class")to appropriate view in Card
            // view (here "ingredient.xml")
            holder.checkBox.setText(model.getItem());
//        } else {
//            holder.itemView.setVisibility(View.GONE);
//        }
    }

    // Function to tell the class about the view (here
    // "ingredient.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public ingredientsViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient, parent, false);
        return new ingredientAdapter.ingredientsViewholder(view);

    }

    // Sub Class to create references of the views in Card
    // view (here "ingredient.xml")
    class ingredientsViewholder
            extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public ingredientsViewholder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}

