package br.dev.nina.sprachklang.core.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Wordlists")
data class WordlistEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "list_id")
    val id: Int = 0,
    val name: String
)
