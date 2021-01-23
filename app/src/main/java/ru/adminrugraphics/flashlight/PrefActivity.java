package ru.adminrugraphics.flashlight;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.view.View;


public class PrefActivity extends PreferenceActivity {
    SwitchPreference svPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);





/*        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), R.string.message_screen_lock, duration);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();*/
    }
}


