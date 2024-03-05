package br.dev.nina.sprachklang.core.data.dictionary.db


import br.dev.nina.sprachklang.core.data.db.entities.DefinitionEntity
import br.dev.nina.sprachklang.core.data.db.entities.HeadwordEntity
import br.dev.nina.sprachklang.core.domain.dictionary.model.Definition
import br.dev.nina.sprachklang.core.domain.dictionary.model.Entry
import br.dev.nina.sprachklang.core.domain.dictionary.model.Headword

fun HeadwordEntity.asHeadword() = Headword(
    id = id,
    word = word,
    pos = pos,
    langCode = langCode
)

fun DefinitionEntity.asDefinition() = Definition(
    glosses = glosses,
    synonyms = synonyms,
    related = related
)

fun Map<HeadwordEntity, List<DefinitionEntity>>.asEntry(): Entry? {
    if (this.entries.size != 1) return null

    val entry = this.entries.first()

    return with(entry.key) {
        Entry(
            id = id,
            word = word,
            pos = pos,
            ipas = ipas ?: emptyList(),
            audioUrls = audioUrls,
            langCode = langCode,
            definitions = entry.value.map { it.asDefinition() }
        )
    }
}

fun Map<HeadwordEntity, List<DefinitionEntity>>.asEntries(): List<Entry> {
    return this.map { (headWordEntity, definitionsEntity) ->
        with(headWordEntity) {
            Entry(
                id = id,
                word = word,
                pos = pos,
                ipas = ipas ?: emptyList(),
                audioUrls = audioUrls,
                langCode = langCode,
                definitions = definitionsEntity.map { it.asDefinition() }
            )
        }
    }
}
