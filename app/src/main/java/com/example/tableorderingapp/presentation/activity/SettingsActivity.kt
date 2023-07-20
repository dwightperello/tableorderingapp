package com.example.tableorderingapp.presentation.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.tableorderingapp.R
import com.example.tableorderingapp.databinding.SettingsActivityBinding
import com.example.tableorderingapp.util.GlobalVariable

class SettingsActivity : AppCompatActivity() {
    lateinit var _binding:SettingsActivityBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= SettingsActivityBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)
        setupActionBar()
        sharedPreferences = applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        GlobalVariable.tablenumber= sharedPreferences.getInt("tablenumber",0)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }



    }

    private fun setupActionBar() {
        setSupportActionBar(_binding.toolbarSettings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        _binding.toolbarSettings.setNavigationOnClickListener { onBackPressed() }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onPreferenceTreeClick(preference: Preference): Boolean {
            return super.onPreferenceTreeClick(preference)
        }
    }
}