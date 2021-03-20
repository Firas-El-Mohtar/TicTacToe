package com.example.testingthings.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.testingthings.R
import com.example.testingthings.ui.tictactoe.GameHeaderFragment

class MySettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
   companion object{
       fun newInstance() = MySettingsFragment()
   }

}