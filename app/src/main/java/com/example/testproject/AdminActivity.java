package com.example.testproject;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminActivity extends AppCompatActivity {

    private Button AddRecipeButton, LogOutButton;

    private DatabaseReference RootRef;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        RootRef = FirebaseDatabase.getInstance().getReference();

        InitializeFields();

        LogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToLoginActivity();
            }
        });

        AddRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewRecipe();
            }
        });

        SendNotification();
    }

    private void CreateNewRecipe() {
        SendNotification();
        SendUserToLoginActivity();
    }

    private void InitializeFields() {
        AddRecipeButton = (Button) findViewById(R.id.addRecipeButton);
        LogOutButton = (Button) findViewById(R.id.logOutButton);
        loadingBar = new ProgressDialog(this);
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

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(AdminActivity.this);
        // notificationId is a unique int for each notification
        notificationManager.notify(1, builder.build());
    }

    private void SendUserToLoginActivity() {
        Intent LoginIntent = new Intent(AdminActivity.this, LoginActivity.class);
        startActivity(LoginIntent);
    }
}