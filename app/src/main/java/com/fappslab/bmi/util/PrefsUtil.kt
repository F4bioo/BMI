package com.fappslab.bmi.util

import android.content.Context
import android.content.SharedPreferences
import com.fappslab.bmi.BuildConfig

object PrefsUtil {
    private const val PREF_LOCAL_APP = "${BuildConfig.APPLICATION_ID}.app"
    const val PREF_KEY_MODE_NIGHT = "prefModeNight"

    fun getPref(context: Context): SharedPreferences = context.getSharedPreferences(
        PREF_LOCAL_APP,
        Context.MODE_PRIVATE
    )
}
