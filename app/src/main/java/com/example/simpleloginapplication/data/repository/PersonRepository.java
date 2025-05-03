package com.example.simpleloginapplication.data.repository;

import android.content.Context;

import com.example.simpleloginapplication.data.dao.PersonDao;
import com.example.simpleloginapplication.data.database.AppDatabase;
import com.example.simpleloginapplication.data.entity.Person;

import java.util.List;

public class PersonRepository {
    private final PersonDao personDao;

    public PersonRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        personDao = db.personDao();
    }

    public List<Person> getAllPeople() {
        return personDao.getAllPeople();
    }

    public Person getPersonById(int personId) {
        return personDao.getPersonById(personId);
    }
}
