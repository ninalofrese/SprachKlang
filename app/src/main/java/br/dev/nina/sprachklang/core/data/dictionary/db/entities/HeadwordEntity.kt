package br.dev.nina.sprachklang.core.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "Headwords")
@JsonClass(generateAdapter = true)
data class HeadwordEntity(
    @PrimaryKey
    val id: Int,
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
