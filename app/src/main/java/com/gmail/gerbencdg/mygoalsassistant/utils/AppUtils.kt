package com.gmail.gerbencdg.mygoalsassistant.utils

import android.content.Context
import android.content.pm.PackageManager
import android.preference.PreferenceManager
import android.util.Log

class AppUtils {

    enum class AppStart {
        FIRST_TIME, FIRST_TIME_VERSION, NORMAL
    }

    companion object {
        private const val LAST_APP_VERSION = "last_app_version"

        fun checkAppStart(context: Context): AppStart {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            var appStart = AppStart.NORMAL

            try {
                val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                val lastVersionCode = sharedPreferences
                    .getInt(LAST_APP_VERSION, -1)
                val currentVersionCode = pInfo.versionCode
                appStart = checkAppStart(currentVersionCode, lastVersionCode)

                // Update version in preferences
                sharedPreferences.edit().putInt(LAST_APP_VERSION, currentVersionCode).apply()

            } catch (e: PackageManager.NameNotFoundException) {
                Log.e(
                    context.javaClass.name,
                    "Unable to determine current app version from package manager. Therefore assuming normal app start."
                )
            }
            return appStart
        }

        private fun checkAppStart(currentVersionCode: Int, lastVersionCode: Int): AppStart = when {
            lastVersionCode == -1 -> AppStart.FIRST_TIME
            currentVersionCode > lastVersionCode -> AppStart.FIRST_TIME_VERSION
            else -> AppStart.NORMAL
        }
    }

}