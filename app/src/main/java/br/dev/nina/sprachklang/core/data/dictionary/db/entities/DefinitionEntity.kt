package br.dev.nina.sprachklang.core.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Definitions",
    foreignKeys = [ForeignKey(
        entity = HeadwordEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("word_id"),
        onDelete = ForeignKey.NO_ACTION
    )]
)
data class DefinitionEntity(
    @PrimaryKey
    @ColumnInfo("def_id")
    val id: String,
    @ColumnInfo("word_id")
    val wordId: Int,
    val glosses: List<String>?,
    val synonyms: List<String>? = null,
    @ColumnInfo(name = "related_words")
    val related: List<String>? = null
)
