package br.dev.nina.sprachklang.dictionarysearch.data.source.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Definitions")
data class DefinitionEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo("word_id")
    val wordId: Int,
    val glosses: List<String>?,
    val synonyms: List<String>? = null,
    @ColumnInfo(name = "related_words")
    val related: List<String>? = null
)
