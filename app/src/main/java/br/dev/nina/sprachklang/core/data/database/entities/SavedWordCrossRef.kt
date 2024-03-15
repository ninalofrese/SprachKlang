package br.dev.nina.sprachklang.core.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "SavedWords",
    primaryKeys = ["word_id", "collection_id"],
    foreignKeys = [
        ForeignKey(
        entity = HeadwordEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("word_id"),
        onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = WordlistEntity::class,
            parentColumns = arrayOf("list_id"),
            childColumns = arrayOf("collection_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SavedWordCrossRef(
    @ColumnInfo(name = "word_id", index = true)
    val wordId: Int,
    @ColumnInfo(name = "collection_id", index = true)
    val wordlistId: Int
)
