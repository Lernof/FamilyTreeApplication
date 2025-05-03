package com.example.simpleloginapplication.ui.adapters;

import com.example.simpleloginapplication.PersonDetailsFragment;
import com.example.simpleloginapplication.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpleloginapplication.data.entity.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {
    private List<Person> people = new ArrayList<>();

    public void setPeople(List<Person> people) {
        this.people = people;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_person, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person person = people.get(position);
        holder.nameTextView.setText(person.getFirstName() + " " + person.getLastName());
        holder.birthDateTextView.setText(person.getBirthDate() != null ? person.getBirthDate() : "Unknown");
        holder.genderTextView.setText(person.getGender() != null ? person.getGender() : "Unknown");
        holder.biographyTextView.setText(person.getBiography() != null ? person.getBiography() : "No biography available");

        // Optional: Load profile photo (placeholder for now)
        // holder.profilePhoto.setImageResource(R.drawable.ic_person_placeholder);

        // Handle item click to show fragment
        holder.itemView.setOnClickListener(v -> {
            FragmentActivity activity = (FragmentActivity) holder.itemView.getContext();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, PersonDetailsFragment.newInstance(person.getPersonId()))
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    static class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView birthDateTextView;
        TextView genderTextView;
        TextView biographyTextView;
        ImageView profilePhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_name);
            birthDateTextView = itemView.findViewById(R.id.text_birth_date);
            genderTextView = itemView.findViewById(R.id.text_gender);
            biographyTextView = itemView.findViewById(R.id.text_biography);
            profilePhoto = itemView.findViewById(R.id.profile_photo);
        }
    }
}
