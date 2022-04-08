package com.example.testproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class favouritesAdapter extends FirebaseRecyclerAdapter<
        recipe, favouritesAdapter.favouritesViewholder> {

    private Context mContext;

    private String recipeName = "No favourites";


    public favouritesAdapter(
            @NonNull FirebaseRecyclerOptions<recipe> options, Context context) {
        super(options);
        this.mContext = context;
    }

    // Function to bind the view in Card view(here
    // "favourite.xml") with data in
    // model class(here "recipe.class")
    @Override
    protected void
    onBindViewHolder(@NonNull favouritesViewholder holder,
                     int position, @NonNull recipe model) {

        if(mContext instanceof MainActivity){
            MainActivity activity = (MainActivity)mContext;
            recipeName = activity.getRecipeNameToDisplay();
            Log.d("Recipe in adapter: ", recipeName);
        }

        if (model.getRecipeName().contentEquals(recipeName)) {
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

            holder.favImage.setImageResource(R.drawable.ic_baseline_favorite_24);

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
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    // Function to tell the class about the Card view (here
    // "recipe.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public favouritesViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourite, parent, false);
        return new favouritesAdapter.favouritesViewholder(view);
    }

    // Sub Class to create references of the views in Card
    // view (here "recipe.xml")
    class favouritesViewholder
            extends RecyclerView.ViewHolder {
        TextView recipeName, recipeTime, recipeLevel;
        ImageView recipeImage;
        ImageButton favImage;

        public favouritesViewholder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeTime = itemView.findViewById(R.id.recipeTime);
            recipeLevel = itemView.findViewById(R.id.recipeLevel);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            favImage = itemView.findViewById(R.id.favouriteImg);
        }
    }
}

