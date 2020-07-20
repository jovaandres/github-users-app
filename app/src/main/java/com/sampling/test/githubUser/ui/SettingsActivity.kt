package com.sampling.test.githubUser.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import com.sampling.test.githubUser.AlarmReceiver
import com.sampling.test.githubUser.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager.commit {
            replace(R.id.settings, PreferenceFragment())
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class PreferenceFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener,
        PreferenceManager.OnPreferenceTreeClickListener {

        private lateinit var reminder: String
        private lateinit var language: String
        private lateinit var favorite: String

        private lateinit var reminderPreference: SwitchPreference
        private lateinit var languagePreference: Preference
        private lateinit var favoritePreference: Preference

        private lateinit var alarmReceiver: AlarmReceiver

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.preference)

            init()
        }

        private fun init() {
            reminder = "reminder"
            language = "language"
            favorite = "favorite"

            reminderPreference = findPreference<SwitchPreference?>(reminder) as SwitchPreference
            languagePreference = findPreference<Preference?>(language) as Preference
            favoritePreference = findPreference<Preference?>(favorite) as Preference

            val sh = preferenceManager.sharedPreferences
            reminderPreference.setDefaultValue(sh.getBoolean(reminder, false))
            alarmReceiver = AlarmReceiver()
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
            when (key) {
                reminder -> {
                    reminderPreference.setDefaultValue(
                        sharedPreferences.getBoolean(
                            reminder,
                            false
                        )
                    )
                    if (reminderPreference.isChecked) {
                        alarmReceiver.setAlarm(
                            context, getString(R.string.app_name), getString(
                                R.string.message
                            )
                        )
                    } else {
                        alarmReceiver.cancelAlarm(context)
                    }
                }
            }
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            return when (preference?.key) {
                language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                favorite -> {
                    startActivity(Intent(context, FavoriteActivity::class.java))
                    true
                }
                else -> true
            }
        }
    }
}