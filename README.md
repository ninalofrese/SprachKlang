# SprachKlang App 🇩🇪

Offline-first and accessibility-mindful app to learn the pronunciation of German words.

---

## Features

- Search the German word and get the entries offline
- Show the word definition in English
- Play the pronunciation audio (internet connection required)
- Create wordlists and add words to it

<p align="center" width="100%">
  <br/>
  <img src="https://github.com/ninalofrese/SprachKlang/assets/26123557/e709ca95-4cb1-47c4-b030-07e4db78db16" width=400 alt="Demo animated gif shows adding the new word Eichhörchen in the search bar, clicking on the pronunciation button to listen, then saving it to a new list named Animals and navigating back to show the wordlist" />
</p>

## Tech stack

- Minimum SDK level 24
- 100% Kotlin based + Coroutines + Flow for asynchronous
- Hilt for dependency injection
- Jetpack Compose for declarative UI + Material3
- Room for all the data
- MVVM Architecture + Repository pattern
- Custom pagination

## Credits

All the data is from [Kaikki](https://kaikki.org/dictionary/German/index.html)'s dump of [Wiktextract](https://github.com/tatuylonen/wiktextract) and it's available at [Wiktionary](https://de.wiktionary.org/wiki/Wiktionary:Hauptseite).

