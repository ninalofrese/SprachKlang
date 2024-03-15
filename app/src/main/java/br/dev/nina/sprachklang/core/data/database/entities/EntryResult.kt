package br.dev.nina.sprachklang.core.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class EntryResult(
    @Embedded
    val headword: HeadwordEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "word_id"
    )
    val definitions: List<DefinitionEntity>
)
