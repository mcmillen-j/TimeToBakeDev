package com.example.testproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.testproject.databinding.ActivityMainBinding;
import com.example.testproject.ui.TimerFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
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

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    private Menu menu;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private String currentUserID;
    private String currentRecipeName;
    private String retrieveFavStatus;
    private String recipeNameToPass, recipeTimeToPass, recipeLevelToPass;

    private ImageButton speakButton;
    private EditText inputtedText;
    private int count = 0;

    private SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        RootRef = FirebaseDatabase.getInstance().getReference();

        checkIfRecipeAdded();

        speakButton = (ImageButton) findViewById(R.id.micBtn);
        inputtedText = (EditText)findViewById(R.id.inputText);

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    speakButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_mic_24));
                    //start listening
                    speechRecognizer.startListening(speechRecognizerIntent);
                    count = 1;
                } else {
                    speakButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_mic_off_24));
                    //stop listening
                    speechRecognizer.stopListening();
                    count = 0;
                }
            }
        });
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {
                String message;
                switch (error) {
                    case SpeechRecognizer.ERROR_AUDIO:
                        message = "Audio recording error";
                        break;
                    case SpeechRecognizer.ERROR_CLIENT:
                        message = "Client side error";
                        break;
                    case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                        message = "Insufficient permissions";
                        break;
                    case SpeechRecognizer.ERROR_NETWORK:
                        message = "Network error";
                        break;
                    case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                        message = "Network timeout";
                        break;
                    case SpeechRecognizer.ERROR_NO_MATCH:
                        message = "No match";
                        break;
                    case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                        message = "RecognitionService busy";
                        break;
                    case SpeechRecognizer.ERROR_SERVER:
                        message = "error from server";
                        break;
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                        message = "No speech input";
                        break;
                    default:
                        message = "Didn't understand, please try again.";
                        break;
                }
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);
                inputtedText.setText(data.get(0));
                if (data.get(0).equals("Macaroons")) {

                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

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

        // pass the open and close toggle for the drawer layout listener to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        writeNewRecipe("Macaroons", "Customise and bake your own macaroons by following this easy recipe. Substitute your own flavour or colour to be how you want.", "R.drawable.macaroons", "Pastries",
//        "30m", "Beginner",  "No");
//        writeNewRecipe("Vegan Angel Cake", "Colourful", "src/main/res/drawable-hdpi/angel_cake.png", "Cakes",
//                "1h30m", "Intermediate", "Yes");
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
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
        currentUserID = mAuth.getCurrentUser().getUid();
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
                                Toast.makeText(MainActivity.this, "Your account has been deleted.", Toast.LENGTH_SHORT).show();
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

    public void setLevelSelected(String recipeLevel) {
        recipeLevelToPass = recipeLevel;
        Log.d("RecipeLevelReceived: ", recipeLevel);
    }

    public String getRecipeTimeToDisplay() {
        return recipeTimeToPass;
    }

    public String getRecipeLevelToDisplay() {
        return recipeLevelToPass;
    }

    public void updateFavouriteClick(View view, String recipeNameClicked) {
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef.child("Favourites").child(currentUserID).child(recipeNameClicked)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        retrieveFavStatus = dataSnapshot.child("Favourite").getValue().toString();
                        if (retrieveFavStatus.contentEquals("Yes")) {
                            RootRef.child("Favourites").child(currentUserID).child(recipeNameClicked).child("Favourite").setValue("No");
                            Toast.makeText(MainActivity.this, "Recipe removed from favourites.", Toast.LENGTH_SHORT).show();
                        }
                        if (retrieveFavStatus.contentEquals("No")) {
                            RootRef.child("Favourites").child(currentUserID).child(recipeNameClicked).child("Favourite").setValue("Yes");
                            Toast.makeText(MainActivity.this, "Recipe added to favourites.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void checkIfRecipeAdded() {
        RootRef.child("Recipes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SendNotification();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SendNotification() {
        // Explicit intent to open activity from notification
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notify")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Recipe Added")
                .setContentText("A new recipe has been added. Click to open app.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
        // notificationId is a unique int for each notification
        notificationManager.notify(1, builder.build());
    }
}