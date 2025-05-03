package com.example.simpleloginapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoggedInActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_LOGGED_IN = "logged_in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        Button logoutButton = findViewById(R.id.logoutButton);
        Button enterTree = findViewById(R.id.enterTree);
        var closeApp = findViewById(R.id.closeButton);

        closeApp.setOnClickListener(func -> {
            finish();
            System.exit(0);
        });

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        String username = sharedPreferences.getString(KEY_USERNAME, "User");
        welcomeTextView.setText("Welcome, to the family Tree, " + username + "!");

        logoutButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(KEY_LOGGED_IN, false);
            editor.apply();

            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        enterTree.setOnClickListener(v -> {
            startActivity(new Intent(this, PeopleActivity.class));
        });
    }
}
