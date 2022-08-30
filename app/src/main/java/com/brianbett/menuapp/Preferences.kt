package com.brianbett.menuapp

class Preferences {
    private val preferences = context.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE)

    fun isWorkRunning(): Boolean {
        return preferences.getBoolean("notification_work", false)
    }

    fun setWorkRunning(running: Boolean) {
        preferences.edit {
            putBoolean("notification_work", running)
        }
    }
}