package com.example.simpleloginapplication.data.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import com.example.simpleloginapplication.data.entity.Person;

import java.util.List;

@Dao
public interface PersonDao {
    @Query("SELECT * FROM People")
    List<Person> getAllPeople();

    @Query("SELECT * FROM People WHERE person_id = :personId")
    Person getPersonById(int personId);

    @Update
    void updatePerson(Person person);
}
