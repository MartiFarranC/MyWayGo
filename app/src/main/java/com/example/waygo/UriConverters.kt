package com.example.waygo

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UriConverters {

    @TypeConverter
    fun fromUriList(uriList: List<Uri>?): String? {
        if (uriList == null) {
            return null
        }
        val gson = Gson()
        return gson.toJson(uriList.map { it.toString() })
    }

    @TypeConverter
    fun toUriList(uriListString: String?): List<Uri>? {
        if (uriListString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson<List<String>>(uriListString, type)?.map { Uri.parse(it) }
    }
}