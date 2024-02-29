package br.dev.nina.sprachklang.dictionarysearch.data.source.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "Entries")
@JsonClass(generateAdapter = true)
data class EntryEntity(
    @PrimaryKey
    @ColumnInfo(name = "entry_id")
    val id: Int? = null,
    @ColumnInfo(name = "word")
    val word: String,
    @ColumnInfo(name = "pos")
    val pos: String,
    @ColumnInfo(name = "lang_code")
    val langCode: String,
    @ColumnInfo(name = "ipas")
    val ipas: List<String>?,
    @ColumnInfo(name = "ogg_urls")
    val audioUrls: List<String>?
)
