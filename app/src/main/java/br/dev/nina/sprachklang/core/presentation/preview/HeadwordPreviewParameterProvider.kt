package br.dev.nina.sprachklang.core.presentation.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import br.dev.nina.sprachklang.core.domain.dictionary.model.Definition
import br.dev.nina.sprachklang.core.domain.dictionary.model.Entry
import br.dev.nina.sprachklang.core.domain.dictionary.model.Headword

class HeadwordPreviewParameterProvider: PreviewParameterProvider<List<Headword>> {
    override val values: Sequence<List<Headword>> = sequenceOf(PreviewParameterData.headwords)
}

object PreviewParameterData {

    private val randomWords = mapOf(
        "genau" to "adv",
        "kühlgemäßigt" to "verb",
        "Erklärungsnot" to "adj",
        "Fröhlich" to "name",
        "Freundschaftsbezeugung" to "conj",
        "Rücken" to "det",
        "erfroren sind schon viele, aber erstunken ist noch keiner" to "prep_phrase",
        "Backpfeifengesicht" to "conj",
        "Rechts" to "prep",
        "Rinderkennzeichnungsfleischetikettierungsüberwachungsaufgabenübertragungsgesetz" to "noun"
    )

    val headwords = (1..45).map {
        val random = randomWords.keys.random()
        Headword(it, "$random $it", "${randomWords[random]}", "de")
    }

    val entry = Entry(
        word = "Genau",
        langCode = "de",
        id = 30,
        pos = "adj",
        ipas = listOf("/ɡəˈnaʊ̯/"),
        audioUrls = listOf("https://upload.wikimedia.org/wikipedia/commons/e/e4/De-genau.ogg"),
        definitions = listOf(
            Definition(
                glosses = listOf("exact", "just, exactly"),
                synonyms = listOf("exakt"),
                related = listOf("stimmt")
            )
        )
    )

}
