package com.example.simpleloginapplication.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.simpleloginapplication.data.entity.Person;
import com.example.simpleloginapplication.data.repository.PersonRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeopleViewModel extends ViewModel {
    private final PersonRepository repository;
    private final MutableLiveData<List<Person>> people = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public PeopleViewModel(Context context) {
        repository = new PersonRepository(context);
    }

    public LiveData<List<Person>> getPeople() {
        return people;
    }

    public void loadPeople() {
        executor.execute(() -> {
            List<Person> personList = repository.getAllPeople();
            people.postValue(personList);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}