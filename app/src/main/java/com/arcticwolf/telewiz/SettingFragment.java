package com.arcticwolf.telewiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        Preference preference = findPreference("feedback_key");

        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {


                switch (preference.getKey()) {
                    case "feedback_key":
                        Intent Email = new Intent(Intent.ACTION_SEND);
                        Email.setType("text/email");
                        Email.putExtra(Intent.EXTRA_EMAIL, new String[]{"misha-bg@ukr.net"});
                        Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                        startActivity(Intent.createChooser(Email, "Send Feedback:"));
                        break;
                    default:
                        break;
                }

                return true;

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key)
    {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }


}
