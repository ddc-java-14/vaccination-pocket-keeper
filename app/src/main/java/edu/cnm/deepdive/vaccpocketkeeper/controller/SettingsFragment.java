package edu.cnm.deepdive.vaccpocketkeeper.controller;

import android.os.Bundle;
import edu.cnm.deepdive.vaccpocketkeeper.R;
import androidx.preference.PreferenceFragmentCompat;

/**
 * Instantiates the settings widgets as specified in teh res/xml/settings.xml file. Specifically,
 * instantiates a SeekBarPreference widget for the future nubmer of years to display.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.settings, rootKey);
  }
}
