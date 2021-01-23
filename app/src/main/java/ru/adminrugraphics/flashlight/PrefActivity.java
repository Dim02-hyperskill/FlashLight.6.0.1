package ru.adminrugraphics.flashlight;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;


public class PrefActivity extends PreferenceActivity {
    SwitchPreference svPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

    }
}


