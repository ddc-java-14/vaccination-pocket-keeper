package edu.cnm.deepdive.vaccpocketkeeper.controller;

import android.os.Bundle;
import edu.cnm.deepdive.vaccpocketkeeper.R;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.settings, rootKey);
  }
}
