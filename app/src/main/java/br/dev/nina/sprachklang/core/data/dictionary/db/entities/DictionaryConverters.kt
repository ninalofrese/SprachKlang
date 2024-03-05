package br.dev.nina.sprachklang.core.data.db.entities

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi

object DictionaryConverters {

    private lateinit var moshi: Moshi

    fun initialize(moshi: Moshi){
        DictionaryConverters.moshi = moshi
    }

    @TypeConverter
    fun toListOfStrings(flatStringList: String): List<String> {
        return flatStringList.removeSurrounding("[", "]").split(", ")
    }
    @TypeConverter
    fun fromListOfStrings(listOfString: List<String>): String {
        return listOfString.joinToString(", ")
    }
}
