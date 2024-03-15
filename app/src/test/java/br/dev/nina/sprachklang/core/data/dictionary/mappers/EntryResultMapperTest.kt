package br.dev.nina.sprachklang.core.data.dictionary.mappers

import br.dev.nina.sprachklang.core.data.database.entities.DefinitionEntity
import br.dev.nina.sprachklang.core.data.database.entities.EntryResult
import br.dev.nina.sprachklang.core.data.database.entities.HeadwordEntity
import br.dev.nina.sprachklang.core.data.database.mappers.asEntries
import br.dev.nina.sprachklang.core.domain.dictionary.model.Definition
import br.dev.nina.sprachklang.core.domain.dictionary.model.Entry
import com.google.common.truth.Truth.assertThat
import org.junit.Test


class EntryResultMapperTest {

    @Test
    fun `when EntryResults is mapped Then return Entries`() {

        val entryResults = listOf(
            EntryResult(
                headword = HeadwordEntity(
                    id = 1,
                    word = "Wort",
                    pos = "noun",
                    langCode = "de",
                    ipas = listOf("/vɔrt/"),
                    audioUrls = listOf("https://upload.wikimedia.org/wikipedia/commons/6/6e/De-Wort.ogg"),
                ),
                definitions = listOf(
                    DefinitionEntity(
                        id = "Wort-noun-De-8a920s13",
                        wordId = 1,
                        glosses = listOf(
                            "(plural Worte) utterance, word with context",
                        ),
                        synonyms = null,
                        related = listOf("Beiwort")
                    ),
                    DefinitionEntity(
                        id = "Wort-noun-De-9al5m5j3o",
                        wordId = 1,
                        glosses = listOf(
                            "(uncountable) promise, (figuratively) word",
                            "(biblical) the Word of God, Scripture, the scriptures (collectively)",
                        ),
                        synonyms = null,
                        related = listOf("Beiwort")
                    )
                )
            )
        )

        val entry = entryResults.asEntries()

        assertThat(entry).isEqualTo(
            listOf(
                Entry(
                    id = 1,
                    word = "Wort",
                    pos = "noun",
                    langCode = "de",
                    ipas = listOf("/vɔrt/"),
                    audioUrls = listOf("https://upload.wikimedia.org/wikipedia/commons/6/6e/De-Wort.ogg"),
                    definitions = listOf(
                        Definition(
                            glosses = listOf(
                                "(plural Worte) utterance, word with context",
                            ),
                            synonyms = null,
                            related = listOf("Beiwort")
                        ),
                        Definition(
                            glosses = listOf(
                                "(uncountable) promise, (figuratively) word",
                                "(biblical) the Word of God, Scripture, the scriptures (collectively)",
                            ),
                            synonyms = null,
                            related = listOf("Beiwort")
                        )
                    )
                )
            )
        )
    }
}
