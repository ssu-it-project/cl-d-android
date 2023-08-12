package com.seumulseumul.cld.sharedpref

import android.annotation.SuppressLint
import android.app.Activity
import android.content.SharedPreferences
import com.seumulseumul.cld.ClDApplication
import org.json.JSONObject
import java.lang.Exception
import java.util.InputMismatchException

object PrefData {

    private fun getPref(): SharedPreferences =
        ClDApplication.applicationContext().let{
            it.getSharedPreferences(it.packageName, Activity.MODE_PRIVATE)
        }

    fun remove(key: String) = getPref().edit().remove(key).apply()

    fun clear() = getPref().edit().clear().apply()

    @SuppressLint("CommitPrefEdits")
    fun put(pair: Pair<Any?, String>) = with(getPref().edit()) {
        val value = pair.first
        val key = pair.second
        when (value) {
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            is HashMap<*, *> -> {
                val jsonObject = JSONObject(value)
                val jsonString: String = jsonObject.toString()
                val editor: SharedPreferences.Editor = getPref().edit()
                editor.remove(key).apply()
                editor.putString(key, jsonString)
                editor.apply()
            }
            null -> remove(key)
            else -> InputMismatchException("Only primitive types can be stored in SharedPreferences")
        }

        apply()
    }

    fun getString(key: String, defaultValue: String = ""): String = getPref().getString(key, defaultValue)!!
    fun getBoolean(key: String, defaultValue: Boolean = true): Boolean =  getPref().getBoolean(key, defaultValue)
    fun getInt(key: String, defaultValue: Int): Int =  getPref().getInt(key, defaultValue)
    fun getFloat(key: String, defaultValue: Float): Float =  getPref().getFloat(key, defaultValue)
    fun getLong(key: String, defaultValue: Long): Long =  getPref().getLong(key, defaultValue)
    fun getBooleanHashMap(key: String): HashMap<String, Boolean> {
        val outputMap = HashMap<String, Boolean>()
        try {
            val jsonString = getPref().getString(key, JSONObject().toString())
            if (jsonString != null) {
                val jsonObject = JSONObject(jsonString)
                val keysItr = jsonObject.keys()
                while (keysItr.hasNext()) {
                    val hashKey = keysItr.next()
                    val value = jsonObject[hashKey] as Boolean
                    outputMap[hashKey] = value
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return outputMap
    }
}