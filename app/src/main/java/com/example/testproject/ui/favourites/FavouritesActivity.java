package com.example.testproject.ui.favourites;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.testproject.R;
import com.example.testproject.databinding.ActivityFavouritesBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
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

import android.os.Bundle;

public class FavouritesActivity extends AppCompatActivity {

    private ActivityFavouritesBinding binding;
    private static final String TAG = "FavouritesActivity";
    private static String displayFavourite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavouritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference recipeName = database.getReference("recipeName");
        DatabaseReference mDatabase = database.getReference();
        DatabaseReference recipeName = mDatabase.child("recipes").child("1").child("recipeName");
        DatabaseReference level = mDatabase.child("recipes").child("1").child("level");
        DatabaseReference favourite = mDatabase.child("recipes").child("1").child("favourite");

        //Read from the database
        favourite.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String favouriteValue = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + favouriteValue);
                if (favouriteValue.contentEquals("Yes")){
                    setVariable("Yes");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        if (getVariable() == "Yes") {

            TextView textView = findViewById(R.id.test);
            textView.setText(getVariable());
            //Read from the database
            recipeName.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Value is: " + value);
                    TextView textView = findViewById(R.id.recipeName);
                    textView.setText(value);
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
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Value is: " + value);
                    TextView textView = findViewById(R.id.level);
                    textView.setText(value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

        } else {
            Log.w(TAG, "Not a favourite");

        }

    }
    public void setVariable(String s)
    {
        this.displayFavourite = s;
    }

    public String getVariable()
    {
        return displayFavourite;
    }
}