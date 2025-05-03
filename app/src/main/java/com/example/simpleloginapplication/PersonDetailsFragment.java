package com.example.simpleloginapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simpleloginapplication.data.database.AppDatabase;
import com.example.simpleloginapplication.data.entity.Person;
import com.example.simpleloginapplication.data.repository.PersonRepository;
import com.example.simpleloginapplication.databinding.FragmentPersonDetailsBinding;

public class PersonDetailsFragment extends Fragment {
    private static final String ARG_PERSON_ID = "person_id";
    private FragmentPersonDetailsBinding binding;
    private PersonRepository repository;

    public static PersonDetailsFragment newInstance(int personId) {
        PersonDetailsFragment fragment = new PersonDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PERSON_ID, personId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new PersonRepository(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPersonDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonBack.setOnClickListener(v -> {
            Log.d("PersonDetailsFragment", "Back button clicked");
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Get personId from arguments
        int personId = getArguments() != null ? getArguments().getInt(ARG_PERSON_ID, -1) : -1;
        if (personId == -1) {
            Log.e("PersonDetailsFragment", "Invalid person_id");
            requireActivity().getSupportFragmentManager().popBackStack();
            return;
        }

        // Query person from database
        new Thread(() -> {
            Person person = repository.getPersonById(personId);
            requireActivity().runOnUiThread(() -> {
                if (person != null) {
                    // Bind data to views
                    binding.textName.setText(person.getFirstName() + " " + person.getLastName());
                    binding.textPersonId.setText("ID: " + (person.getPersonId() != null ? person.getPersonId() : "Unknown"));
                    binding.textBirthDate.setText("Birth Date: " + (person.getBirthDate() != null ? person.getBirthDate() : "Unknown"));
                    binding.textDeathDate.setText("Death Date: " + (person.getDeathDate() != null ? person.getDeathDate() : "Unknown"));
                    binding.textGender.setText("Gender: " + (person.getGender() != null ? person.getGender() : "Unknown"));
                    binding.textBiography.setText("Biography: " + (person.getBiography() != null ? person.getBiography() : "No biography available"));
                } else {
                    Log.e("PersonDetailsFragment", "Person not found for ID: " + personId);
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}