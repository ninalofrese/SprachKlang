package br.dev.nina.sprachklang.core


object PrePopulatedDictionary {
    const val headwordInsert = "INSERT INTO Headwords (id, word, pos, lang_code, ipas, ogg_urls) VALUES (?, ?, ?, ?, ?, ?)"
    val headword1 = arrayOf(11, "umsonst", "adv", "de", "[/ʊmˈzɔnst/]", "[https://upload.wikimedia.org/wikipedia/commons/d/d0/De-umsonst.ogg]")
    val headword2 = arrayOf(15, "aboral", "adj", "de", "null", "[https://upload.wikimedia.org/wikipedia/commons/3/3c/De-aboral.ogg]")
    val headword3 = arrayOf(12, "abdominales", "adj", "de", "null", "[https://upload.wikimedia.org/wikipedia/commons/f/f4/De-abdominales.ogg]")
    val headword4 = arrayOf(16, "genau", "adj", "de", "null", "[https://upload.wikimedia.org/wikipedia/commons/f/f4/De-abdominales.ogg]")


    const val definitionInsert = "INSERT OR IGNORE INTO Definitions (def_id, word_id, glosses, synonyms, related_words) VALUES (?, ?, ?, ?, ?)"
    val definition1 = arrayOf("umsonst-de-adv-i-wfpBXp", "11", "[free of charge, gratis]", "null", "null")
    val definition1_2 = arrayOf("umsonst-de-adv-Rkvq6kwO", "11", "[in vain, without success]", "null", "null")
    val definition1_3 = arrayOf("umsonst-de-adv-7meMaXEs", "11", "[for nothing; for the sake of doing it (without expecting reply)]", "null", "null")
    val definition2 = arrayOf("aboral-de-adj-oiy4qlDQ", "15", "[aboral]", "null", "null")
    val definition3 = arrayOf("abdominales-de-adj-uUOn2Gg-", "12", "[strong/mixed nominative/accusative neuter singular of abdominal]", "null", "null")
}
