package br.dev.nina.sprachklang.core.data.testdoubles

import br.dev.nina.sprachklang.core.data.database.entities.DefinitionEntity
import br.dev.nina.sprachklang.core.data.database.entities.EntryResult
import br.dev.nina.sprachklang.core.data.database.entities.HeadwordEntity
import br.dev.nina.sprachklang.core.domain.dictionary.model.Headword

fun dummyHeadwordEntity(id: Int) = HeadwordEntity(
    id = id,
    pos = "adv",
    word = "genau",
    langCode = "de",
    ipas = null,
    audioUrls = null
)

fun dummyEntryResults(size: Int = 1, wordId: Int = 1) = (1..size).map { definitionId ->
    EntryResult(
        dummyHeadwordEntity(wordId),
        listOf(DefinitionEntity(
            id = "genau-adv-de-${definitionId}",
            wordId = wordId,
            glosses = listOf("Exact", "exactly")
        ))
    )
}

fun dummyHeadword(id: Int) = Headword(
    id = id,
    pos = "adv",
    word = "genau",
    langCode = "de"
)
