package com.example.simpleloginapplication.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(tableName = "People")
public class Person {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "person_id")
    private Integer personId;
    @ColumnInfo(name = "first_name")
    private String firstName;
    @ColumnInfo(name = "last_name")
    private String lastName;
    @ColumnInfo(name = "gender")
    private String gender;
    @ColumnInfo(name = "birth_date")
    private String birthDate;
    @ColumnInfo(name = "death_date")
    private String deathDate;
    @ColumnInfo(name = "biography")
    private String biography;
    @ColumnInfo(name = "profile_photo_url")
    private String profilePhotoUrl;
}
