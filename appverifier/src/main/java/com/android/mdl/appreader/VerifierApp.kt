package com.android.mdl.appreader

import android.app.Application
import androidx.preference.PreferenceManager
import com.android.mdl.appreader.settings.UserPreferences
import com.google.android.material.color.DynamicColors

class VerifierApp : Application() {

    private val userPreferences by lazy {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        UserPreferences(sharedPreferences)
    }

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        userPreferencesInstance = userPreferences
    }

    companion object {

        private lateinit var userPreferencesInstance: UserPreferences

        fun isDebugLogEnabled(): Boolean {
            return userPreferencesInstance.isDebugLoggingEnabled()
        }
    }
}