package com.example.testproject;

import android.Manifest;
import android.app.ActionBar;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testproject.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnInitListener{

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    private Menu menu;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private String currentRecipeName;
    private String retrieveFavStatus;
    private String recipeNameToPass, recipeTimeToPass;

    private int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech myTTS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        RootRef = FirebaseDatabase.getInstance().getReference();

        Button speakButton = (Button) findViewById(R.id.speak);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText enteredText = (EditText)findViewById(R.id.enter);
                String words = enteredText.getText().toString();
//                speakWords(words);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify", "notify", NotificationManager.IMPORTANCE_DEFAULT);
            String description = "Hello";
            channel.setDescription(description);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        //centre app title in Action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
        navigationView.setItemTextAppearance(R.style.MenuTextStyle);

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_recipes, R.id.navigation_discover, R.id.navigation_favourites, R.id.navigation_shoppingList, R.id.navigation_community, R.id.navigation_pastries, R.id.navigation_conversionChart, R.id.navigation_timer)
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


//
//        writeNewRecipe("Macaroons", "Customise and bake your own macaroons by following this easy recipe. Substitute your own flavour or colour to be how you want.", "R.drawable.macaroons", "Pastries",
//        "30m", "Beginner", "1.Separate egg whites", "No", "Yes");
//        writeNewRecipe("Vegan Angel Cake", "Colourful", "src/main/res/drawable-hdpi/angel_cake.png", "Cakes",
//                "1h30m", "Intermediate", "1.Mix together ingredients", "No", "No");

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser == null) {
            SendUserToLogInActivity();
        } else {
            VerifyUserExistence();
        }
    }

    private void VerifyUserExistence() {
        String currentUserID = mAuth.getCurrentUser().getUid();
        RootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.child("firstname").exists())) {
                    String retrieveFirstName = dataSnapshot.child("firstname").getValue().toString();
                    Toast.makeText(MainActivity.this, "Welcome " + retrieveFirstName, Toast.LENGTH_SHORT).show();
                } else {
                    SendUserToAccountActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SendUserToLogInActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void SendUserToAccountActivity() {
        Intent accountIntent = new Intent(MainActivity.this, AccountActivity.class);
        accountIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(accountIntent);
        finish();
    }

    public void writeNewRecipe(String name, String description, String image, String category,
                               String time, String level, String recipeOfWeek) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference recipeName = database.getReference("name");
        DatabaseReference mDatabase = database.getReference();
        mDatabase.child("Recipes").child(name).child("recipeName").setValue(name);
        mDatabase.child("Recipes").child(name).child("description").setValue(description);
        mDatabase.child("Recipes").child(name).child("image").setValue(image);
        mDatabase.child("Recipes").child(name).child("category").setValue(category);
        mDatabase.child("Recipes").child(name).child("time").setValue(time);
        mDatabase.child("Recipes").child(name).child("level").setValue(level);
        mDatabase.child("Recipes").child(name).child("recipeOfWeek").setValue(recipeOfWeek);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.account_option) {
            SendUserToAccountActivity();
        }

        if (item.getItemId() == R.id.main_logout_option) {
            mAuth.signOut();
            SendUserToLogInActivity();
        }

        if (item.getItemId() == R.id.delete_account_option) {
            currentUser.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User account deleted.");
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(MainActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            SendUserToLogInActivity();
        }

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    public String getRecipeName() {
//        return currentRecipeName;
//    }

    public void fbClick(View view) {
        startActivity(getOpenFacebookIntent());
    }

    public void converterClick(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.thecalculatorsite.com/cooking/cooking-calculator.php"));
        startActivity(browserIntent);
    }

    public void startTimerClick(View view) {
        Navigation.findNavController(view).navigate(R.id.navigation_timer);
    }

    public void pastriesClick(View view) {
        Navigation.findNavController(view).navigate(R.id.navigation_pastries);
    }

    public void cakesClick(View view) {
        Navigation.findNavController(view).navigate(R.id.navigation_cakes);
    }

    public void displayRecipeClick(View view, String recipeNameClicked) {
        Navigation.findNavController(view).navigate(R.id.navigation_display_recipe);
        currentRecipeName = recipeNameClicked;
    }

    public void updateFavouriteClick(String recipeNameRetrieved) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();

        RootRef.child("Recipes").child(recipeNameRetrieved)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        retrieveFavStatus = dataSnapshot.child("favourite").getValue().toString();
                        if (retrieveFavStatus.contentEquals("Yes")) {
                            RootRef.child("Recipes").child(recipeNameRetrieved).child("favourite").setValue("No");
                        } if (retrieveFavStatus.contentEquals("No")) {
                            RootRef.child("Recipes").child(recipeNameRetrieved).child("favourite").setValue("Yes");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public Intent getOpenFacebookIntent() {
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/groups/2866819223610700"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/groups/2866819223610700"));
        }
    }

    public void checkMyPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.menu = menu;

        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    public String getRecipeNameClicked() {
        return currentRecipeName;
    }

    public void sendNotification() {
        // Explicit intent to open activity from notification
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notify")
                .setSmallIcon(R.drawable.ic_baseline_access_alarms_24)
                .setContentTitle("Timer Ended")
                .setContentText("Baking time is up, check the oven.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification
        notificationManager.notify(1, builder.build());
    }

    @Override
    public void onInit(int initStatus) {

        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            myTTS.setLanguage(Locale.US);
            myTTS.speak("Hello World", TextToSpeech.QUEUE_FLUSH, null);

//            if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
//                myTTS.setLanguage(Locale.US);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

//    private void speakWords(String speech) {
//        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
//    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                myTTS = new TextToSpeech(this, this);
            } else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    public void setRecipeNameToDisplay(String recipeName) {
        recipeNameToPass = recipeName;
        Log.d("RecipeNameReceived: ", recipeName);
    }

    public String getRecipeNameToDisplay() {
        return recipeNameToPass;
    }

    public void setTimeSelected(String recipeTime) {
        recipeTimeToPass = recipeTime;
        Log.d("RecipeTimeReceived: ", recipeTime);
    }

    public String getRecipeTimeToDisplay() {
        return recipeTimeToPass;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}