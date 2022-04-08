package com.example.testproject;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testproject.ui.displayRecipes.DisplayRecipesFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class recipePastriesAdapter extends FirebaseRecyclerAdapter<
        recipe, recipePastriesAdapter.recipesViewholder> {

    private Context mContext;

    public recipePastriesAdapter(
            @NonNull FirebaseRecyclerOptions<recipe> options, Context context ) {
        super(options);
        this.mContext = context;
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
            // "recipe.class")to appropriate view in Card
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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String recipeID = getRef(position).getKey();
                    Log.d("RecipeID clicked: ", recipeID);
                    if (mContext instanceof MainActivity) {
                        ((MainActivity)mContext).displayRecipeClick(v, recipeID);
                    }
                }
            });
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
        return new recipePastriesAdapter.recipesViewholder(view);

    }

    // Sub Class to create references of the views in Card
    // view (here "recipe.xml")
    class recipesViewholder
            extends RecyclerView.ViewHolder {
        TextView recipeName, recipeTime, recipeLevel;
        ImageView recipeImage;

        public recipesViewholder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeTime = itemView.findViewById(R.id.recipeTime);
            recipeLevel = itemView.findViewById(R.id.recipeLevel);
            recipeImage = itemView.findViewById(R.id.recipeImage);
        }
    }
}

