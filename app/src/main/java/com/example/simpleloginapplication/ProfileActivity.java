package com.example.simpleloginapplication;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.simpleloginapplication.data.entity.Person;
import com.example.simpleloginapplication.data.repository.PersonRepository;
import com.example.simpleloginapplication.databinding.ActivityProfileBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email"; // New field
    private static final String KEY_JOIN_DATE = "join_date"; // New field
    private PersonRepository personRepository;
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        personRepository = new PersonRepository(this);

        // Load user data
        updateUI();

        // Back button
        binding.backButton.setOnClickListener(v -> finish());

        // Edit button
        binding.editButton.setOnClickListener(v -> showEditDialog());

        // Load family tree stats
        loadFamilyTreeStats();
    }

    private void updateUI() {
        String username = sharedPreferences.getString(KEY_USERNAME, "User");
        String email = sharedPreferences.getString(KEY_EMAIL, "No email provided");
        long joinDateMillis = sharedPreferences.getLong(KEY_JOIN_DATE, System.currentTimeMillis());
        String joinDate = new SimpleDateFormat("MMM dd, yyyy", Locale.US).format(new Date(joinDateMillis));

        binding.profileUsername.setText(username);
        binding.profileEmail.setText("Email: " + email);
        binding.profileJoinDate.setText("Joined: " + joinDate);

        // Optional: Load profile photo (placeholder for now)
        // binding.profilePhoto.setImageResource(R.drawable.ic_person_placeholder);
    }

    private void showEditDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_profile, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        EditText editUsername = dialogView.findViewById(R.id.edit_username);
        EditText editEmail = dialogView.findViewById(R.id.edit_email);

        editUsername.setText(sharedPreferences.getString(KEY_USERNAME, ""));
        editEmail.setText(sharedPreferences.getString(KEY_EMAIL, ""));

        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.button_save).setOnClickListener(v -> {
            String username = editUsername.getText().toString().trim();
            String email = editEmail.getText().toString().trim();

            if (username.isEmpty()) {
                Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_USERNAME, username);
            editor.putString(KEY_EMAIL, email);
            editor.apply();

            updateUI();
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialogView.findViewById(R.id.button_cancel).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void loadFamilyTreeStats() {
        new Thread(() -> {
            List<Person> people = personRepository.getAllPeople();
            runOnUiThread(() -> {
                binding.profileTreeStats.setText("Family Tree: " + people.size() + " members");
            });
        }).start();
    }
}