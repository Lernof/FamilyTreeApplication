package com.example.simpleloginapplication.ui.viewmodels;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PeopleViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public PeopleViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PeopleViewModel.class)) {
            return (T) new PeopleViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
