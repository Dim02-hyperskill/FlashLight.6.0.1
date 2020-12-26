package ru.adminrugraphics.flashlight;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class PrefActivity extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences (Bundle savedInstanceState, String rootKey) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pref);
       // addPreferencesFromResource(R.xml.pref);
        //addContentView(R.xml.pref);
        setPreferencesFromResource(R.xml.pref, rootKey);

    }
}


