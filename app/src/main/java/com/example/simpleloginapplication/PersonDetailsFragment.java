package com.example.simpleloginapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.simpleloginapplication.data.entity.Person;
import com.example.simpleloginapplication.data.repository.PersonRepository;
import com.example.simpleloginapplication.databinding.FragmentPersonDetailsBinding;
import com.example.simpleloginapplication.ui.viewmodels.PeopleViewModel;
import com.example.simpleloginapplication.ui.viewmodels.PeopleViewModelFactory;

public class PersonDetailsFragment extends Fragment {
    private static final String ARG_PERSON_ID = "person_id";
    private FragmentPersonDetailsBinding binding;
    private PersonRepository repository;
    private Person currentPerson;
    private PeopleViewModel peopleViewModel;

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
        // Initialize PeopleViewModel
        PeopleViewModelFactory factory = new PeopleViewModelFactory(requireContext());
        peopleViewModel = new ViewModelProvider(requireActivity(), factory).get(PeopleViewModel.class);
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

        binding.buttonEdit.setOnClickListener(v -> showEditDialog());

        // Get personId from arguments
        int personId = getArguments() != null ? getArguments().getInt(ARG_PERSON_ID, -1) : -1;
        if (personId == -1) {
            Log.e("PersonDetailsFragment", "Invalid person_id");
            requireActivity().getSupportFragmentManager().popBackStack();
            return;
        }

        // Query person from database
        loadPersonData(personId);
    }

    private void loadPersonData(int personId) {
        new Thread(() -> {
            currentPerson = repository.getPersonById(personId);
            requireActivity().runOnUiThread(() -> {
                if (currentPerson != null) {
                    updateUI(currentPerson);
                } else {
                    Log.e("PersonDetailsFragment", "Person not found for ID: " + personId);
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }).start();
    }

    private void updateUI(Person person) {
        binding.textName.setText(person.getFirstName() + " " + person.getLastName());
        binding.textPersonId.setText("ID: " + (person.getPersonId() != null ? person.getPersonId() : "Unknown"));
        binding.textBirthDate.setText("Birth Date: " + (person.getBirthDate() != null ? person.getBirthDate() : "Unknown"));
        binding.textDeathDate.setText("Death Date: " + (person.getDeathDate() != null ? person.getDeathDate() : "Unknown"));
        binding.textGender.setText("Gender: " + (person.getGender() != null ? person.getGender() : "Unknown"));
        binding.textBiography.setText("Biography: " + (person.getBiography() != null ? person.getBiography() : "No biography available"));
    }

    private void showEditDialog() {
        if (currentPerson == null) {
            Toast.makeText(requireContext(), "Person data not loaded", Toast.LENGTH_SHORT).show();
            return;
        }

        // Inflate the dialog layout
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_person, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);

        // Initialize dialog fields
        EditText editFirstName = dialogView.findViewById(R.id.edit_first_name);
        EditText editLastName = dialogView.findViewById(R.id.edit_last_name);
        EditText editGender = dialogView.findViewById(R.id.edit_gender);
        EditText editBirthDate = dialogView.findViewById(R.id.edit_birth_date);
        EditText editDeathDate = dialogView.findViewById(R.id.edit_death_date);
        EditText editBiography = dialogView.findViewById(R.id.edit_biography);

        // Pre-fill fields with current person data
        editFirstName.setText(currentPerson.getFirstName());
        editLastName.setText(currentPerson.getLastName());
        editGender.setText(currentPerson.getGender());
        editBirthDate.setText(currentPerson.getBirthDate());
        editDeathDate.setText(currentPerson.getDeathDate());
        editBiography.setText(currentPerson.getBiography());

        // Create and show the dialog
        AlertDialog dialog = builder.create();

        // Set up Save button
        dialogView.findViewById(R.id.button_save).setOnClickListener(v -> {
            // Get updated values
            String firstName = editFirstName.getText().toString().trim();
            String lastName = editLastName.getText().toString().trim();
            String gender = editGender.getText().toString().trim();
            String birthDate = editBirthDate.getText().toString().trim();
            String deathDate = editDeathDate.getText().toString().trim();
            String biography = editBiography.getText().toString().trim();

            // Basic validation
            if (firstName.isEmpty() || lastName.isEmpty()) {
                Toast.makeText(requireContext(), "First and Last Name are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update person object
            currentPerson.setFirstName(firstName);
            currentPerson.setLastName(lastName);
            currentPerson.setGender(gender.isEmpty() ? null : gender);
            currentPerson.setBirthDate(birthDate.isEmpty() ? null : birthDate);
            currentPerson.setDeathDate(deathDate.isEmpty() ? null : deathDate);
            currentPerson.setBiography(biography.isEmpty() ? null : biography);

            // Update database
            new Thread(() -> {
                repository.updatePerson(currentPerson);
                requireActivity().runOnUiThread(() -> {
                    // Refresh UI with updated data
                    updateUI(currentPerson);
                    // Notify PeopleViewModel to refresh the list
                    peopleViewModel.loadPeople();
                    Toast.makeText(requireContext(), "Person updated successfully", Toast.LENGTH_SHORT).show();
                });
            }).start();

            dialog.dismiss();
        });

        // Set up Cancel button
        dialogView.findViewById(R.id.button_cancel).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}