package com.example.simpleloginapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.simpleloginapplication.databinding.ActivityPeopleBinding;
import com.example.simpleloginapplication.ui.adapters.PersonAdapter;
import com.example.simpleloginapplication.ui.viewmodels.PeopleViewModel;
import com.example.simpleloginapplication.ui.viewmodels.PeopleViewModelFactory;

public class PeopleActivity extends AppCompatActivity {
    private ActivityPeopleBinding binding;
    private PersonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPeopleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup RecyclerView
        adapter = new PersonAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        // Setup ViewModel with Factory
        PeopleViewModelFactory factory = new PeopleViewModelFactory(this);
        PeopleViewModel viewModel = new ViewModelProvider(this, factory).get(PeopleViewModel.class);
        viewModel.getPeople().observe(this, people -> {
            adapter.setPeople(people);
            Log.d("PeopleActivity", "Received " + people.size() + " people");
            // Show/hide empty state and RecyclerView
            if (people.isEmpty()) {
                binding.emptyTextView.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
                binding.fragmentContainer.setVisibility(View.GONE);
            } else {
                binding.emptyTextView.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.fragmentContainer.setVisibility(View.GONE);
            }
        });
        viewModel.loadPeople();

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.fragmentContainer.setVisibility(View.GONE);
                Log.d("PeopleActivity", "Back stack empty, showing RecyclerView");
            } else {
                binding.recyclerView.setVisibility(View.GONE);
                binding.fragmentContainer.setVisibility(View.VISIBLE);
                Log.d("PeopleActivity", "Back stack not empty, showing FragmentContainer");
            }
        });
    }
}
