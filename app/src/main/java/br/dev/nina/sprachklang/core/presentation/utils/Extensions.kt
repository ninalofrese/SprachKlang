package br.dev.nina.sprachklang.core.presentation.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.withStyle

fun String.localize(langCode: String): AnnotatedString {
    return buildAnnotatedString {
        withStyle(SpanStyle(localeList = LocaleList(langCode))) {
            append(this@localize)
        }
    }
}

private val posAbbreviationMap = mapOf(
    "adj" to "adjective",
    "adv" to "adverb",
    "conj" to "conjunction",
    "det" to "determiner",
    "intj" to "interjection",
    "num" to "numeral",
    "postp" to "postposition",
    "prep" to "preposition",
    "prep_phrase" to "prepositional phrase",
    "pron" to "pronoun",
)

fun getFullPosName(abbreviation: String): String =
    posAbbreviationMap.getOrDefault(abbreviation, abbreviation)
