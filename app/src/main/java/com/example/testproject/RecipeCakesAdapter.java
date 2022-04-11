package com.example.testproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

// FirebaseRecyclerAdapter is a class provided by FirebaseUI. it provides functions to bind, adapt and show database contents in a Recycler View
public class RecipeCakesAdapter extends FirebaseRecyclerAdapter<
        Recipe, RecipeCakesAdapter.recipesViewholder> {

    private Context mContext;
    private String timeSelected = "No time selected";
    private String levelSelected = "No level selected";

    public RecipeCakesAdapter(
            @NonNull FirebaseRecyclerOptions<Recipe> options, Context context) {
        super(options);
        this.mContext = context;
    }

    // Function to bind the view in Card view(here "recipe.xml") with data in model class(here "recipe.class")
    @Override
    protected void
    onBindViewHolder(@NonNull recipesViewholder holder,
                     int position, @NonNull Recipe model) {

        if(mContext instanceof MainActivity){
            MainActivity activity = (MainActivity)mContext;
            timeSelected = activity.getRecipeTimeToDisplay();
            Log.d("Time in adapter: ", timeSelected);
            levelSelected = activity.getRecipeLevelToDisplay();
            Log.d("Level in adapter: ", levelSelected);
        }

        if ((model.getCategory().contentEquals("Cakes") && (timeSelected.contentEquals("No time selected"))) && (levelSelected.contentEquals("No level selected"))) {
                // Add recipe details from model class (here "recipe.class")to appropriate view in Card view (here "recipe.xml")
                holder.recipeName.setText(model.getRecipeName());
                holder.recipeTime.setText(model.getTime());
                holder.recipeLevel.setText(model.getLevel());
                String link = model.getImage();
                Picasso.get().load(link).into(holder.recipeImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String recipeID = getRef(position).getKey();
                        Log.d("RecipeID clicked: ", recipeID);
                        if (mContext instanceof MainActivity) {
                            ((MainActivity) mContext).displayRecipeClick(v, recipeID);
                        }
                    }
                });
            } else if (model.getCategory().contentEquals("Cakes") && (model.getTime().contentEquals(timeSelected)) && (levelSelected.contentEquals("No level selected"))) {
            holder.recipeName.setText(model.getRecipeName());
            holder.recipeTime.setText(model.getTime());
            holder.recipeLevel.setText(model.getLevel());
            String link = model.getImage();
            Picasso.get().load(link).into(holder.recipeImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String recipeID = getRef(position).getKey();
                    Log.d("RecipeID clicked: ", recipeID);
                    if (mContext instanceof MainActivity) {
                        ((MainActivity) mContext).displayRecipeClick(v, recipeID);
                    }
                }
            });
        } else if (model.getCategory().contentEquals("Cakes") && (timeSelected.contentEquals("No time selected")) && (model.getLevel().contentEquals(levelSelected))) {
            holder.recipeName.setText(model.getRecipeName());
            holder.recipeTime.setText(model.getTime());
            holder.recipeLevel.setText(model.getLevel());
            String link = model.getImage();
            Picasso.get().load(link).into(holder.recipeImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String recipeID = getRef(position).getKey();
                    Log.d("RecipeID clicked: ", recipeID);
                    if (mContext instanceof MainActivity) {
                        ((MainActivity) mContext).displayRecipeClick(v, recipeID);
                    }
                }
            });
        } else if (model.getCategory().contentEquals("Cakes") && (model.getTime().contentEquals(timeSelected)) && (model.getLevel().contentEquals(levelSelected))) {
            holder.recipeName.setText(model.getRecipeName());
            holder.recipeTime.setText(model.getTime());
            holder.recipeLevel.setText(model.getLevel());
            String link = model.getImage();
            Picasso.get().load(link).into(holder.recipeImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String recipeID = getRef(position).getKey();
                    Log.d("RecipeID clicked: ", recipeID);
                    if (mContext instanceof MainActivity) {
                        ((MainActivity) mContext).displayRecipeClick(v, recipeID);
                    }
                }
            });
        } else {
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
    // Function to tell the class about the Card view (here "recipe.xml")in which the data will be shown
    @NonNull
    @Override
    public recipesViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe, parent, false);
        return new RecipeCakesAdapter.recipesViewholder(view);

    }

    // Sub Class to create references of the views in Card view (here "recipe.xml")
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

