package com.geetgobindingh.githubrepoapp.data.prefs

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

class SharedPrefHelper @Inject
constructor(private val mSharedPreferences: SharedPreferences) {

    fun clear() {
        mSharedPreferences.edit().clear().apply()
    }

    fun saveData(key: String, value: String) {
        mSharedPreferences.edit().putString(key, value).apply()
    }

    fun saveDataInstantly(key: String, value: String) {
        mSharedPreferences.edit().putString(key, value).commit()
    }

    fun saveData(key: String, value: Boolean) {
        mSharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun saveData(key: String, value: Float) {
        mSharedPreferences.edit().putFloat(key, value).apply()
    }

    fun saveData(key: String, value: Int) {
        mSharedPreferences.edit().putInt(key, value).apply()
    }

    fun saveData(key: String, value: Long) {
        mSharedPreferences.edit().putLong(key, value).apply()
    }

    fun saveData(key: String, value: Set<String>) {
        mSharedPreferences.edit().putStringSet(key, value).apply()
    }

    fun getString(key: String, defaultValue: String?): String? {
        return mSharedPreferences.getString(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return mSharedPreferences.getBoolean(key, defaultValue)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return mSharedPreferences.getLong(key, defaultValue)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return mSharedPreferences.getInt(key, defaultValue)
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return mSharedPreferences.getFloat(key, defaultValue)
    }

    fun getStringSet(key: String, defaultValue: Set<String>?): Set<String>? {
        return mSharedPreferences.getStringSet(key, defaultValue)
    }

    companion object {
        val PREF_FILE_NAME = "user_app_pref_file"
    }
}