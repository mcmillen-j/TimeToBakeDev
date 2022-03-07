package com.example.testproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.testproject.ui.favourites.FavouritesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.view.MenuItem;

import com.example.testproject.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    String recipeNameValue;
    String levelValue;
    String timeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_recipes, R.id.navigation_discover, R.id.navigation_favourites, R.id.navigation_shoppingList, R.id.navigation_community)
                .setDrawerLayout(drawerLayout).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navigationView, navController);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // drawer layout instance to toggle the menu icon to open drawer and back button to close drawer
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference recipeName = database.getReference("recipeName");
        DatabaseReference mDatabase = database.getReference();
        DatabaseReference recipeName = mDatabase.child("recipes").child("1").child("recipeName");
        DatabaseReference level = mDatabase.child("recipes").child("1").child("level");
        DatabaseReference time = mDatabase.child("recipes").child("1").child("time");

        //Read from the database
        recipeName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                recipeNameValue = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + recipeNameValue);
//                TextView textView = findViewById(R.id.recipeName);
//                textView.setText(recipeNameValue);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        level.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String levelValue = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + levelValue);
//                TextView textView = findViewById(R.id.level);
//                textView.setText(levelValue);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        time.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String timeValue = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + timeValue);
//                TextView textView = findViewById(R.id.time);
//                textView.setText(timeValue);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
    public void writeNewRecipe(String recipeId, String name, String description, String image, String category,
                               String time, String level, String method, String favourite, String recipeOfWeek) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference recipeName = database.getReference("name");
        DatabaseReference mDatabase = database.getReference();
        mDatabase.child("recipes").child(recipeId).child("recipeName").setValue(name);
        mDatabase.child("recipes").child(recipeId).child("description").setValue(description);
        mDatabase.child("recipes").child(recipeId).child("image").setValue(image);
        mDatabase.child("recipes").child(recipeId).child("category").setValue(category);
        mDatabase.child("recipes").child(recipeId).child("time").setValue(time);
        mDatabase.child("recipes").child(recipeId).child("level").setValue(level);
        mDatabase.child("recipes").child(recipeId).child("method").setValue(method);
        mDatabase.child("recipes").child(recipeId).child("favourite").setValue(favourite);
        mDatabase.child("recipes").child(recipeId).child("recipeOfWeek").setValue(recipeOfWeek);
    }

//    /** Called when the user taps the Send button */
//    public void sendMessage(View view) {
//        Intent intent = new Intent(this, DisplayMessageActivity.class);
//        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
//        // Write a message to the database
////        FirebaseDatabase database = FirebaseDatabase.getInstance();
////        DatabaseReference recipeName = database.getReference("name");
////        DatabaseReference mDatabase = database.getReference();
//
//        writeNewRecipe("1", "Macaroons", "Colourful", "imagepath", "Pastries",
//        "30m", "Beginner", "1.Separate egg whites", "Yes", "Yes");
//        writeNewRecipe("2", "Vegan Angel Cake", "Colourful", "imagepath", "Cakes",
//                "1h30m", "Intermediate", "1.Mix together ingredients", "Yes", "No");
//
//    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getRecipeName() {
        return recipeNameValue;
    }
    public String getRecipeLevel() {
        return levelValue;
    }
    public String getRecipeTime() {
        return timeValue;
    }

    public void fbClick(View view) {
        startActivity(getOpenFacebookIntent());
    }
    public Intent getOpenFacebookIntent() {
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/groups/2866819223610700"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/groups/2866819223610700"));
        }
    }
}