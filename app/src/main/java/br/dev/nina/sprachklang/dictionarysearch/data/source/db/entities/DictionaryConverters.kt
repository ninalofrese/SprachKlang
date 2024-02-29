package br.dev.nina.sprachklang.dictionarysearch.data.source.db.entities

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi

object DictionaryConverters {

    private lateinit var moshi: Moshi

    fun initialize(moshi: Moshi){
        DictionaryConverters.moshi = moshi
    }

    @TypeConverter
    fun toListOfStrings(flatStringList: String): List<String> {
        return flatStringList.split(",")
    }
    @TypeConverter
    fun fromListOfStrings(listOfString: List<String>): String {
        return listOfString.joinToString(",")
    }
}
