package com.example.testproject;

import android.net.Uri;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.w3c.dom.Text;

import java.util.List;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class recipeAdapter extends FirebaseRecyclerAdapter<
        recipe, recipeAdapter.recipesViewholder> {


    public recipeAdapter(
            @NonNull FirebaseRecyclerOptions<recipe> options) {
        super(options);
    }

    // Function to bind the view in Card view(here
    // "recipe.xml") with data in
    // model class(here "recipe.class")
    @Override
    protected void
    onBindViewHolder(@NonNull recipesViewholder holder,
                     int position, @NonNull recipe model) {

        if (model.getCategory().toString().contentEquals("Pastries")) {
            // Add recipe name from model class (here
            // "recipe.class")to appropriate view in Card
            // view (here "recipe.xml")
            holder.recipeName.setText(model.getRecipeName());

            // Add time from model class (here
            // "person.class")to appropriate view in Card
            // view (here "recipe.xml")
            holder.recipeTime.setText(model.getTime());

            // Add level from model class (here
            // "recipe.class")to appropriate view in Card
            // view (here "recipe.xml")
            holder.recipeLevel.setText(model.getLevel());

            // Add image from model class (here
            // "recipe.class")to appropriate view in Card
            // view (here "recipe.xml")
            String link = model.getImage();
            Picasso.get().load(link).into(holder.recipeImage);
        } else {
            holder.itemView.setVisibility(View.GONE);
        }
    }

    // Function to tell the class about the Card view (here
    // "recipe.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public recipesViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe, parent, false);
        return new recipeAdapter.recipesViewholder(view);
    }

    // Sub Class to create references of the views in Card
    // view (here "recipe.xml")
    class recipesViewholder
            extends RecyclerView.ViewHolder {
        TextView recipeName, recipeTime, recipeLevel;
        ImageButton recipeImage;

        public recipesViewholder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeTime = itemView.findViewById(R.id.recipeTime);
            recipeLevel = itemView.findViewById(R.id.recipeLevel);
            recipeImage = itemView.findViewById(R.id.recipeImage);
        }
    }
}

