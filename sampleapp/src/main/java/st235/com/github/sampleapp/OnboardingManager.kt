package st235.com.github.sampleapp

import android.content.Context

class OnboardingManager(
    private val context: Context
) {

    private companion object {
        const val SHARED_PREFS = "onboarding.prefs"

        const val KEY_WAS_SHOWN = "onoboarding_was_shown"
        const val WAS_SHOWN_DEFAULT_VALUE = false
    }

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

    val shouldShow: Boolean
    get() {
        val wasShown = sharedPreferences.getBoolean(KEY_WAS_SHOWN, WAS_SHOWN_DEFAULT_VALUE)
        return !wasShown
    }

    fun updateWasShown() {
        val editor = sharedPreferences.edit()

        editor.putBoolean(KEY_WAS_SHOWN, true)

        editor.apply()
    }

}