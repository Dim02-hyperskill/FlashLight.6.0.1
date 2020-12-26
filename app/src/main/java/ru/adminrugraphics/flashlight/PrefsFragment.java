package ru.adminrugraphics.flashlight;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public static class PrefsFragment extends PreferenceFragment {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}