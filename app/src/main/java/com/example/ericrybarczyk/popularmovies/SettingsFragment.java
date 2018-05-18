package com.example.ericrybarczyk.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

// TODO - cleanup unused code

public class SettingsFragment extends PreferenceFragmentCompat
        //implements SharedPreferences.OnSharedPreferenceChangeListener,
                   //Preference.OnPreferenceChangeListener
{

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_popular_movies); // API Docs: Inflates the given XML resource and adds the preference hierarchy to the current preference hierarchy.

//        PreferenceScreen prefScreen = getPreferenceScreen();
//        SharedPreferences sharedPreferences = prefScreen.getSharedPreferences();


        // remove for re-evaluation
//        Preference sortPreference = findPreference(getString(R.string.pref_sort_key));
//        sortPreference.setOnPreferenceChangeListener(this);

    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        Preference changedPreference = findPreference(key);
//        if (changedPreference != null) {
//
//        }
//    }
//
//    @Override
//    public boolean onPreferenceChange(Preference preference, Object newValue) {
//        return false;
//    }
}
