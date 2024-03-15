package br.dev.nina.sprachklang.core.data.database.mappers

import br.dev.nina.sprachklang.core.data.database.entities.DefinitionEntity
import br.dev.nina.sprachklang.core.data.database.entities.EntryResult
import br.dev.nina.sprachklang.core.data.database.entities.HeadwordEntity
import br.dev.nina.sprachklang.core.data.database.entities.WordlistEntity
import br.dev.nina.sprachklang.core.domain.dictionary.model.Definition
import br.dev.nina.sprachklang.core.domain.dictionary.model.Entry
import br.dev.nina.sprachklang.core.domain.dictionary.model.Headword
import br.dev.nina.sprachklang.core.domain.dictionary.model.Wordlist

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

fun List<EntryResult>.asEntries(): List<Entry> {
    return this.groupBy { it.headword.id }
        .mapNotNull { (_, results) ->
            val headword = results.first().headword
            val definitions = results
                .flatMap { it.definitions }
                .distinctBy { it.id }

            with(headword) {
                Entry(
                    id = id,
                    word = word,
                    langCode = langCode,
                    pos = pos,
                    audioUrls = audioUrls,
                    ipas = ipas ?: emptyList(),
                    definitions = definitions.map { it.asDefinition() }
                )
            }
        }
}

fun WordlistEntity.asWordlist(): Wordlist = Wordlist(
    id = id,
    name = name
)

fun Wordlist.asWordlistEntity(): WordlistEntity = WordlistEntity(
    id = id,
    name = name
)

fun List<WordlistEntity>.asWordlists(): List<Wordlist> {
    return this.map { it.asWordlist() }
}
