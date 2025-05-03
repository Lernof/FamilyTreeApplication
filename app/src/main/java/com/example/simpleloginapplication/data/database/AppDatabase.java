package com.example.simpleloginapplication.data.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.simpleloginapplication.data.dao.PersonDao;
import com.example.simpleloginapplication.data.entity.Person;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Database(entities = {Person.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PersonDao personDao();

    private static volatile AppDatabase INSTANCE;
    private static final String DATABASE_NAME = "mysql.sqlite3"; // Updated name

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    copyDatabaseFromAssets(context);
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, DATABASE_NAME)
                            .build();
                    // Verify database contents after creation
                    Log.d("AppDatabase", "Database initialized. Checking People table...");
                    new Thread(() -> {
                        try {
                            List<Person> people = INSTANCE.personDao().getAllPeople();
                            Log.d("AppDatabase", "People table contains " + people.size() + " rows");
                        } catch (Exception e) {
                            Log.e("AppDatabase", "Error querying People table", e);
                        }
                    }).start();
                }
            }
        }
        return INSTANCE;
    }

    private static void copyDatabaseFromAssets(Context context) {
        File databasePath = context.getDatabasePath(DATABASE_NAME);
        if (databasePath.exists()) {
            Log.d("AppDatabase", "Database already exists at: " + databasePath);
            return;
        }

        Log.d("AppDatabase", "Copying database from assets...");
        databasePath.getParentFile().mkdirs();
        try {
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);
            OutputStream outputStream = new FileOutputStream(databasePath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
            Log.d("AppDatabase", "Database copied successfully");
        } catch (IOException e) {
            Log.e("AppDatabase", "Failed to copy database", e);
            throw new RuntimeException("Failed to copy database from assets", e);
        }
    }
}
