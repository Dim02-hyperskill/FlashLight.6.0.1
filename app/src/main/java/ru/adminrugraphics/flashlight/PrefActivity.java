package ru.adminrugraphics.flashlight;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class PrefActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pref);
        addPreferencesFromResource(R.xml.pref);
        //addContentView(R.xml.pref);
    }
}


